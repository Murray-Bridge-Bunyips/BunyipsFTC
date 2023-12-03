package org.murraybridgebunyips.jerry.tasks

import org.murraybridgebunyips.bunyipslib.tasks.Task
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.IMUOp
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask

// This tasks only uses the IMU and time in order to drive, to see the implementation of both deadwheel and IMU
// see the PrecisionDrive task
@Deprecated("Use JerryPrecisionDriveTask instead")
class JerryIMUDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: CartesianMecanumDrive?,
    private val imu: IMUOp?,
    private val x: Double,
    private val y: Double,
    private val r: Double
) : Task(opMode, time), AutoTask {
    override fun init() {
        imu?.startCapture()
    }

    override fun run() {
        drive?.setSpeedUsingController(x, -y, imu?.getRPrecisionSpeed(r, 3.0) ?: 0.0)
        drive?.update()
        imu?.tick()
    }

    override fun isTaskFinished(): Boolean {
        return false
    }

    override fun onFinish() {
        drive?.stop()
        imu?.resetCapture()
    }
}