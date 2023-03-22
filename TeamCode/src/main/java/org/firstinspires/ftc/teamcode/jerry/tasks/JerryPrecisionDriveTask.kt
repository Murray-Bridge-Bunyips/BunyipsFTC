package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheel
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// Full-featured task for driving to a specific distance, with fail safes in case configuration is not available
class JerryPrecisionDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive,
    private val imu: IMUOp?,
    private val x: Deadwheel?,
    private val y: Deadwheel?,
    private val distance_mm: Double,
    private val direction: Directions,
    private val power: Double
) : Task(opMode, time), TaskImpl {
    private var capture: Double? = 0.0

    // Track the operating mode of the task to account for any robot faults
    private var operatingMode: OperatingMode = OperatingMode.NORM

    enum class OperatingMode {
        NORM, IMU_FAULT, DEADWHEEL_FAULT, CATASTROPHE
    }

    enum class Directions {
        LEFT, RIGHT, FORWARD, BACKWARD
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            x?.targetReached(distance_mm) ?: false
        } else {
            y?.targetReached(distance_mm)?: false
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

        imu?.startCapture()
        if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            x?.enableTracking()
        } else {
            y?.enableTracking()
        }
    }

    override fun run() {
        if (isFinished()) {
            drive.deinit()
            x?.disableTracking()
            y?.disableTracking()
            return
        }

        when (operatingMode) {
            OperatingMode.NORM, OperatingMode.DEADWHEEL_FAULT -> {
                // Normal operation, use everything we can to drive to the target
                drive.setSpeedXYR(
                    if (direction == Directions.LEFT) -power else power,
                    if (direction == Directions.FORWARD) -power else power,
                    imu?.getRPrecisionSpeed(0.0, 2) ?: 0.0
                )
            }

            OperatingMode.CATASTROPHE, OperatingMode.IMU_FAULT -> {
                // Everything is broken, please send help
                drive.setSpeedXYR(
                    if (direction == Directions.LEFT) -power else power,
                    if (direction == Directions.FORWARD) -power else power,
                    0.0
                )
            }
        }
        /*
            The term "bussin" is a slang term that originated in Black American Vernacular English (AAVE)
            and has since become popularized in mainstream culture. It is often used to describe something
            that is really good, enjoyable, or exciting, especially in the context of food, music,
            or social situations. For example, someone might say "This pizza is bussin'!" to express
            that the pizza is delicious, or "This party is bussin'!" to mean that the party is
            really fun and lively. Therefore, this code is 'bussin'
        */
        drive.update()
        imu?.tick()
    }
}