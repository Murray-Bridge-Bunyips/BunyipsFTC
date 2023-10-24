package org.firstinspires.ftc.teamcode.common.tasks

import android.annotation.SuppressLint
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Relay a message to the driver station for a specific time.
 */
class MessageTask(opMode: BunyipsOpMode, time: Double, private val message: String) :
    Task(opMode, time), AutoTask {
    @SuppressLint("DefaultLocale")
    override fun run() {
        opMode.addTelemetry(String.format("%s || %.2f", message, time))
        opMode.telemetry.update()
    }

    override fun onFinish() {
        return
    }
}