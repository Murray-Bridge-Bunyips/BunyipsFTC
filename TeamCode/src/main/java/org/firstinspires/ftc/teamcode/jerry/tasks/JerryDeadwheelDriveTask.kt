package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheel
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// Advanced drive task which will use the deadwheel encoders to X Y position on field
// For this robot, we don't actually need a precision IMU drive, and as such we don't need to
// implement one (although IMUOp has the methods available to make this work)
class JerryDeadwheelDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive,
    private val x: Deadwheel,
    private val y: Deadwheel,
    px_mm: Double,
    py_mm: Double,
    xspeed: Double,
    yspeed: Double
) : Task(opMode, time), TaskImpl {
    private val px_mm: Double
    private val py_mm: Double
    private val xspeed: Double
    private val yspeed: Double

    init {

        // Subtract 6 centimetres from the target distance to account for momentum
        this.px_mm = px_mm - 60
        this.py_mm = py_mm - 60
        this.xspeed = xspeed
        this.yspeed = yspeed
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || x.targetReached(px_mm) && y.targetReached(py_mm)
    }

    override fun run() {
        // Run x before y, moving until the goal is reached
        // Only start the encoder that needs to be tracked, to prevent false readings
        x.enableTracking()
        while (!x.targetReached(px_mm) && !isFinished()) {
            drive.setSpeedXYR(xspeed, 0.0, 0.0)
            drive.update()
        }
        x.disableTracking()
        y.enableTracking()
        while (!y.targetReached(py_mm) && !isFinished()) {
            drive.setSpeedXYR(0.0, yspeed, 0.0)
            drive.update()
        }
        y.disableTracking()
        if (isFinished()) {
            drive.deinit()
            return
        }
    }
}