package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheel
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

class JerryIMUDeadwheelRotationTask(opMode: BunyipsOpMode, time: Double,
                                    private val drive: JerryDrive,
                                    private val x1: Deadwheel,
                                    private val x2: Deadwheel,
                                    private val y: Deadwheel) : Task(opMode, time), TaskImpl {
    override fun run() {
        // TODO: Write this deadwheel rotation task to use the new deadwheel config
    }
}