package org.firstinspires.ftc.teamcode.common.tasks

import android.annotation.SuppressLint
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

class MessageTask(opMode: BunyipsOpMode, time: Double, private val message: String) :
    Task(opMode, time), TaskImpl {
    @SuppressLint("DefaultLocale")
    override fun run() {
        if (isFinished()) {
            return
        }
        opMode.telemetry.addLine(String.format("%s || %.2f", message, time))
    }
}