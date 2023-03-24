package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.DeadwheelMotor
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

/**
 * Full-featured task for driving to a specific distance, with fail safes in case configuration is not available.
 * This supports movement throughout the 2D plane, and can be used to move in any one direction
 * @author Lucas Bubner, 2023
 */
class JerryPrecisionDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive?,
    private val imu: IMUOp?,
    private val x: DeadwheelMotor?,
    private val y: DeadwheelMotor?,
    private val distance_mm: Double,
    private val direction: Directions,
    private var power: Double,
    private val tolerance: Double = 2.0 // Optional tolerance can be specified if 2 degrees is inadequate
) : Task(opMode, time), TaskImpl {
    // Track the operating mode of the task to account for any robot faults
    private var operatingMode: OperatingMode = OperatingMode.NORM

    init {
        try {
            assert(drive != null)
        } catch (e: AssertionError) {
            opMode.telemetry.addLine("Failed to initialise a drive task as the drive system is unavailable.")
        }
        // Use absolute values of power to ensure that the robot moves correctly and is not fed with negative values
        // This is because the task will handle the power management and determine whether the value 
        this.power = Math.abs(power)
    }

    enum class OperatingMode {
        NORM, IMU_FAULT, DEADWHEEL_FAULT, CATASTROPHE
    }

    enum class Directions {
        LEFT, RIGHT, FORWARD, BACKWARD
    }

    override fun isFinished(): Boolean {
        // Check if the task is done by checking if it has timed out in the supercall or if the target has been reached
        // by the respective deadwheel. If the deadwheel is not available, then we cannot check if the target has been
        // reached, so we will just rely on the timeout.
        return super.isFinished() || if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            x?.targetReached(distance_mm) ?: false
        } else {
            y?.targetReached(distance_mm) ?: false
        }
    }

    override fun init() {
        super.init()
        // === BEGIN SELF TEST ===
        // Check if the IMU is working
        if (imu == null || !imu.selfTest()) {
            operatingMode = OperatingMode.IMU_FAULT
        }

        // Check if the deadwheels are online
        if (x == null || y == null) {
            operatingMode =
                if (operatingMode == OperatingMode.NORM) OperatingMode.DEADWHEEL_FAULT else OperatingMode.CATASTROPHE
        }
        // === END SELF TEST ===

        // We now have the limitations of the task and what we can use to accomplish the task
        /*
            NORM: Deadwheels, IMU, and time     [can use all params]
            IMU_FAULT: Deadwheels and time      [Z axis accuracy is limited]
            DEADWHEEL_FAULT: IMU and time       [cannot travel a specific distance]
            CATASTROPHE: Hellfire and brimstone [can only rely on time]
         */

        // Safe call all components to start their tracking and capture vectors
        imu?.startCapture()
        if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            // If moving along the X axis enable the X deadwheel
            x?.enableTracking()
        } else {
            // Otherwise we are moving along the Y axis, and we need to enable the Y deadwheel
            y?.enableTracking()
        }
    }

    override fun run() {
        if (isFinished()) {
            drive!!.deinit()
            x?.disableTracking()
            y?.disableTracking()
            return
        }

        when (operatingMode) {
            OperatingMode.NORM, OperatingMode.DEADWHEEL_FAULT -> {
                // Normal operation, use everything we can to drive to the target
                drive!!.setSpeedXYR(
                    if (direction == Directions.LEFT) -power else if (direction == Directions.RIGHT) power else 0.0,
                    if (direction == Directions.FORWARD) -power else if (direction == Directions.BACKWARD) power else 0.0,
                    imu?.getRPrecisionSpeed(0.0, 2) ?: 0.0
                )
            }

            OperatingMode.CATASTROPHE, OperatingMode.IMU_FAULT -> {
                // Everything is broken, please send help
                drive!!.setSpeedXYR(
                    if (direction == Directions.LEFT) -power else if (direction == Directions.RIGHT) power else 0.0,
                    if (direction == Directions.FORWARD) -power else if (direction == Directions.BACKWARD) power else 0.0,
                    0.0
                )
            }
        }

        // Update drive and tick IMU if possible. Deadwheels will continue to track if they are enabled.
        drive.update()
        imu?.tick()

        // Add telemetry of current operation
        opMode.telemetry.addLine("PrecisionDrive is active under operating mode: $operatingMode")
        opMode.telemetry.addLine("Distance progress: ${if (direction == Directions.LEFT || direction == Directions.RIGHT) x?.travelledMM ?: "N/A" else y?.travelledMM ?: "N/A"}/$distance_mm")
        opMode.telemetry.addLine(
            "Axis correction: ${imu?.capture?.minus(tolerance) ?: "N/A"} <= ${imu?.heading ?: "N/A"} <= ${
                imu?.capture?.plus(
                    tolerance
                ) ?: "N/A"
            }"
        )
    }
}