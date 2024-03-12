package org.murraybridgebunyips.jerry.tasks

import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task

// Rotate the robot to a specific degree angle. This cannot be done with deadwheel assistance due to configuration.
/**
 * Autonomous operation IMU rotation task for Jerry robot.
 * Turns to a specific angle using the IMU.
 * @param angle Positive = rotate left by x degrees (relative).
 * @author Lucas Bubner, 2023
 */
class JerryIMURotationTask(
    time: Double,
    private val imu: IMUOp?,
    private val drive: CartesianMecanumDrive?,
    // Angle information should be a degree of rotation relative to current angle where positive = cw
    private var angle: Double,
    private val speed: Double
) : Task(time), RobotTask {
    /**
     * Represents which way we need to turn
     */
    var direction: Direction = Direction.RIGHT

    /**
     * Which way we need to be turning
     */
    enum class Direction {
        /**
         * Rotate left
         */
        LEFT,

        /**
         * Rotate right
         */
        RIGHT
    }

    override fun init() {
        imu?.update()

        // Find out which way we need to turn based on the information provided
        direction = if (angle < 0.0) {
            // Turn left
            Direction.LEFT
        } else {
            // Turn right
            Direction.RIGHT
        }

        if (direction == Direction.LEFT) {
            drive?.setSpeedUsingController(0.0, 0.0, speed)
        } else {
            drive?.setSpeedUsingController(0.0, 0.0, -speed)
        }
        drive?.update()
    }

    // Stop turning when we reach the target angle
    override fun isTaskFinished(): Boolean {
        val heading = imu?.heading
        return if (direction == Direction.LEFT) {
            // Angle will be decreasing
            heading != null && heading <= angle
        } else {
            // Angle will be increasing
            heading != null && heading >= angle
        }
    }

    override fun periodic() {
        imu?.update()
    }

    override fun onFinish() {
        drive?.stop()
    }
}