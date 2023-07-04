package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// Rotate the robot to a specific degree angle. This cannot be done with deadwheel assistance due to configuration.
/**
 * Autonomous operation IMU rotation task for Jerry robot.
 * Turns to a specific angle using the IMU.
 * @param angle Positive = rotate left by x degrees (relative).
 * @author Lucas Bubner, 2023
 */
class JerryIMURotationTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val imu: IMUOp?,
    private val drive: JerryDrive?,
    // Angle information should be a degree of rotation relative to current angle where positive = cw
    private var angle: Double,
    private val speed: Double
) : Task(opMode, time), TaskImpl {
    // Enum to find out which way we need to be turning
    var direction: Direction = Direction.RIGHT

    enum class Direction {
        LEFT, RIGHT
    }

    override fun init() {
        super.init()
        imu?.tick()

        // Find out which way we need to turn based on the information provided
        direction = if (angle < 0.0) {
            // Turn left
            Direction.LEFT
        } else {
            // Turn right
            Direction.RIGHT
        }

        if (direction == Direction.LEFT) {
            drive?.setSpeedXYR(0.0, 0.0, speed)
        } else {
            drive?.setSpeedXYR(0.0, 0.0, -speed)
        }
        drive?.update()
    }

    // Stop turning when we reach the target angle
    override fun isFinished(): Boolean {
        val heading = imu?.heading
        return super.isFinished() || if (direction == Direction.LEFT) {
            // Angle will be decreasing
            heading != null && heading <= angle
        } else {
            // Angle will be increasing
            heading != null && heading >= angle
        }
    }

    override fun run() {
        if (isFinished()) {
            drive?.deinit()
            return
        }
        imu?.tick()
    }
}