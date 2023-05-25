package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheels
import org.firstinspires.ftc.teamcode.common.XYEncoder
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// Advanced drive task which will use the deadwheel encoders to X Y position on field
// This task DOES NOT use IMU tracking to correct orientation, and only runs on the deadwheels
@Deprecated("Use JerryPrecisionDriveTask instead")
class JerryDeadwheelDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive,
    private val pos: Deadwheels,
    private var px_mm: Double,
    private var py_mm: Double,
    private val xspeed: Double,
    private val yspeed: Double
) : Task(opMode, time), TaskImpl {

    init {
        // Subtract 6 centimetres from the target distance to account for momentum
        this.px_mm = px_mm - 60
        this.py_mm = py_mm - 60
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || pos.targetReached(XYEncoder.Axis.X, px_mm) && pos.targetReached(
            XYEncoder.Axis.Y,
            py_mm
        )
    }

    override fun run() {
        // Run x before y, moving until the goal is reached
        // Only start the encoder that needs to be tracked, to prevent false readings
        pos.enableTracking(XYEncoder.Axis.X)
        while (!pos.targetReached(XYEncoder.Axis.X, px_mm) && !isFinished()) {
            drive.setSpeedXYR(xspeed, 0.0, 0.0)
            drive.update()
        }
        pos.disableTracking(XYEncoder.Axis.X)
        pos.enableTracking(XYEncoder.Axis.Y)
        while (!pos.targetReached(XYEncoder.Axis.Y, py_mm) && !isFinished()) {
            drive.setSpeedXYR(0.0, yspeed, 0.0)
            drive.update()
        }
        pos.disableTracking(XYEncoder.Axis.Y)
        if (isFinished()) {
            drive.deinit()
            return
        }
    }
}