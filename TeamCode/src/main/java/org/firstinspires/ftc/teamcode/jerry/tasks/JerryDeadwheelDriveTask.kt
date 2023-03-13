package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheel
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

// Drive task to move X and Y distances using deadwheels, starts with X and then Y
class JerryDeadwheelDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive,
    private val x1: Deadwheel,
    private val x2: Deadwheel,
    private val y: Deadwheel,
    px_mm: Double,
    py_mm: Double,
    xspeed: Double,
    yspeed: Double
) : Task(opMode, time), TaskImpl {
    init {
    }

    override fun run() {
        // TODO: Rewrite this deadwheel drive task to use the new deadwheel config
    }
}