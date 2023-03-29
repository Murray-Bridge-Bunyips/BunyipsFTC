package org.firstinspires.ftc.teamcode.common.tasks

import android.annotation.SuppressLint
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Relay a message to the driver station for a specific time.
 */
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