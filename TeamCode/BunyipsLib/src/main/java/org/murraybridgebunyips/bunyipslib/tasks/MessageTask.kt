package org.firstinspires.ftc.teamcode.common.tasks

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask

/**
 * Relay a message to the driver station for a specific time.
 */
class MessageTask(opMode: BunyipsOpMode, time: Double, private val message: String) :
    Task(opMode, time), AutoTask {
    override fun init() {
        return
    }

    override fun run() {
        opMode.addTelemetry(message)
    }

    override fun isTaskFinished(): Boolean {
        return false
    }

    override fun onFinish() {
        return
    }
}