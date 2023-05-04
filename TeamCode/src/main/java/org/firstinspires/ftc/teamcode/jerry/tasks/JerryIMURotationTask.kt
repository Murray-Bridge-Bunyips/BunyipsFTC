package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import kotlin.math.abs

// Rotate the robot to a specific degree angle. This cannot be done with deadwheel assistance due to configuration.
/**
 * Autonomous operation IMU rotation task for Jerry robot.
 * Turns to a specific angle using the IMU.
 * @author Lucas Bubner, 2023
 */
class JerryIMURotationTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val imu: IMUOp?,
    private val drive: JerryDrive?,
    // Angle information should be a degree in the range of 0-360
    private var angle: Double,
    private val speed: Double
) : Task(opMode, time), TaskImpl {
    // Enum to find out which way we need to be turning
    var direction: Direction? = null

    enum class Direction {
        LEFT, RIGHT
    }

    override fun init() {
        super.init()
        imu?.tick()

        // Translate if a negative angle is provided
        if (angle < 0) angle += 360
        angle = abs(angle)

        // Ensure input angle is within 0-360 range
        if (angle > 360) angle %= 360

        val currentAngle = imu?.heading
        // If we can't get angle info, then use right as default, relying on time to stop the task
        if (currentAngle == null) {
            direction = Direction.RIGHT
            return
        }

        // Find out which way we need to turn based on the information provided
        direction = if (currentAngle < angle && abs(currentAngle - angle) < abs(currentAngle + 360 - angle)) {
            // Faster to turn right to get to the target. If the desired angle is equal distance both ways,
            // will also turn right (as it is equal, just mere preference)
            Direction.RIGHT
        } else {
            // Faster to turn left to get to the target
            Direction.LEFT
        }
    }

    // Stop turning when we reach the target angle
    override fun isFinished(): Boolean {
        return super.isFinished() || if (direction == Direction.LEFT) {
            // Angle will be decreasing
            imu?.heading!! <= angle
        } else {
            // Angle will be increasing
            imu?.heading!! >= angle
        }
    }

    override fun run() {
        if (isFinished()) {
            drive?.deinit()
            return
        }
        if (direction == Direction.LEFT)
            drive?.setSpeedXYR(0.0, 0.0, speed)
        else
            drive?.setSpeedXYR(0.0, 0.0, -speed)

        imu?.tick()
        drive?.update()
    }
}