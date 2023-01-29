package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheel
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

class JerryPrecisionDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive,
    private val imu: IMUOp,
    private val xe: Deadwheel,
    private val ye: Deadwheel,
    private val speed: Double,
    private val x: Double,
    private val y: Double
) : Task(opMode, time), TaskImpl {
    override fun init() {
        super.init()
        imu.startCapture()
        xe.enableTracking()
        ye.enableTracking()
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || xe.targetReached(x) && ye.targetReached(y)
    }

    override fun run() {
        if (isFinished()) {
            drive.deinit()
            imu.resetCapture()
            xe.disableTracking()
            ye.disableTracking()
            return
        }
        val xspeed = if (xe.travelledMM < x) speed else -speed
        val yspeed = if (ye.travelledMM < y) speed else -speed
        if (!xe.targetReached(x)) {
            drive.setSpeedXYR(xspeed, 0.0, imu.getRPrecisionSpeed(0.0, 3))
        } else if (!ye.targetReached(y)) {
            drive.setSpeedXYR(0.0, yspeed, imu.getRPrecisionSpeed(0.0, 3))
        }
        drive.update()
        imu.tick()
    }
}