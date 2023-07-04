package org.firstinspires.ftc.teamcode.lisa.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

/**
 * Task to rotate Lisa robot using the IMU readings.
 * @author Lucas Bubner, 2023
 */
class LisaIMURotationTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: LisaDrive?,
    private val imu: IMUOp?,
    // Angle is a degree of rotation relative to current angle where positive = cw
    private val angle: Double,
    private val speed: Double
) : Task(opMode, time), TaskImpl {
    var direction: Direction? = null

    enum class Direction {
        LEFT, RIGHT
    }

    override fun init() {
        super.init()
        imu?.tick()
        drive?.setToBrake()

        direction = if (angle < 0.0) {
            // Turn left
            Direction.LEFT
        } else {
            // Turn right
            Direction.RIGHT
        }

        if (direction == Direction.LEFT) {
            drive?.setPower(-speed, speed)
        } else {
            drive?.setPower(speed, -speed)
        }

        drive?.update()
    }

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
            drive?.setPower(0.0, 0.0)
            drive?.update()
            return
        }
        imu?.tick()
    }
}