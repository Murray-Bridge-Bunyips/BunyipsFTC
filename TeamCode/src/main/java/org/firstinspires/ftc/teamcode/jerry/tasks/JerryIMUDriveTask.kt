package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// This tasks only uses the IMU and time in order to drive, to see the implementation of both deadwheel and IMU
// see the PrecisionDrive task
@Deprecated("Use JerryPrecisionDriveTask instead")
class JerryIMUDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive,
    private val imu: IMUOp,
    private val x: Double,
    private val y: Double,
    private val r: Double
) : Task(opMode, time), TaskImpl {
    override fun init() {
        super.init()
        imu.startCapture()
    }

    override fun run() {
        if (isFinished()) {
            drive.deinit()
            imu.resetCapture()
            return
        }
        drive.setSpeedXYR(x, -y, imu.getRPrecisionSpeed(r, 3))
        drive.update()
        imu.tick()
    }
}