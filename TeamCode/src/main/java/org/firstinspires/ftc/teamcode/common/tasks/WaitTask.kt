package org.firstinspires.ftc.teamcode.common.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

class WaitTask(opMode: BunyipsOpMode, time: Double) : Task(opMode, time), TaskImpl {
    fun update() {}
    override fun run() {
        if (isFinished()) {
            return
        }
    }
}