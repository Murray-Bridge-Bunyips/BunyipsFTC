package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheels
import org.firstinspires.ftc.teamcode.common.XYEncoder
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import kotlin.math.abs

/**
 * Full-featured task for driving to a specific distance, with fail safes in case configuration is not available.
 * This supports movement throughout the 2D plane, and can be used to move in any one direction
 * @author Lucas Bubner, 2023
 */
// TODO: Does not work as intended, needs some debugging
class JerryPrecisionDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive?,
    private val imu: IMUOp?,
    private val pos: Deadwheels?,
    private val distance_mm: Double,
    private val direction: Directions,
    private var power: Double,
    private val tolerance: Double = 3.0 // Optional tolerance can be specified if 3 degrees is inadequate
) : Task(opMode, time), TaskImpl {

    init {
        try {
            assert(drive != null)
        } catch (e: AssertionError) {
            opMode.telemetry.addLine("Failed to initialise a drive task as the drive system is unavailable.")
        }
        // Use absolute values of power to ensure that the robot moves correctly and is not fed with negative values
        // This is because the task will handle the power management and determine whether the value 
        this.power = abs(power)
    }

    enum class Directions {
        LEFT, RIGHT, FORWARD, BACKWARD
    }

    override fun isFinished(): Boolean {
        // Check if the task is done by checking if it has timed out in the super call or if the target has been reached
        // by the respective deadwheel. If the deadwheel is not available, then we cannot check if the target has been
        // reached, so we will just rely on the timeout.
        return super.isFinished() || if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            pos?.targetReached(XYEncoder.Axis.X, distance_mm) ?: false
        } else {
            pos?.targetReached(XYEncoder.Axis.Y, distance_mm) ?: false
        }
    }

    override fun init() {
        super.init()

        // Safe call all components to start their tracking and capture vectors
        imu?.startCapture()
        if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            // If moving along the X axis enable the X deadwheel
            pos?.enableTracking(XYEncoder.Axis.X)
        } else {
            // Otherwise we are moving along the Y axis, and we need to enable the Y deadwheel
            pos?.enableTracking(XYEncoder.Axis.Y)
        }
    }

    override fun run() {
        if (isFinished()) {
            drive!!.deinit()
            pos?.resetTracking(XYEncoder.Axis.BOTH)
            return
        }

        imu?.tick()

        drive!!.setSpeedXYR(
            if (direction == Directions.LEFT) -power else if (direction == Directions.RIGHT) power else 0.0,
            if (direction == Directions.FORWARD) -power else if (direction == Directions.BACKWARD) power else 0.0,
            imu?.getRPrecisionSpeed(0.0, tolerance) ?: 0.0
        )

        // Deadwheels will continue to track if they are enabled.
        drive.update()

        // Add telemetry of current operation
        opMode.telemetry.addLine("PrecisionDrive is active.")
        opMode.telemetry.addLine(
            "Distance progress: ${
                if (direction == Directions.LEFT || direction == Directions.RIGHT) pos?.travelledMM(
                    XYEncoder.Axis.X
                ) ?: "N/A" else pos?.travelledMM(XYEncoder.Axis.Y) ?: "N/A"
            }/$distance_mm"
        )
        opMode.telemetry.addLine(
            "Axis correction: ${imu?.capture?.minus(tolerance) ?: "N/A"} <= ${imu?.heading ?: "N/A"} <= ${
                imu?.capture?.plus(
                    tolerance
                ) ?: "N/A"
            }"
        )
    }
}