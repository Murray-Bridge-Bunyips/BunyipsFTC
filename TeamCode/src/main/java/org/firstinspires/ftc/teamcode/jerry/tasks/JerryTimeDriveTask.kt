package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// Base drive task which will run XYR speed for a given time
// Only used for tests, do not use in actual OpMode as field positioning data is lost
class JerryTimeDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive?,
    private val x: Double,
    private val y: Double,
    private val r: Double
) : Task(opMode, time), TaskImpl {
    override fun run() {
        drive!!.setSpeedXYR(x, y, r)
        drive.update()
        if (isFinished()) {
            drive.deinit()
            return
        }
    }
}