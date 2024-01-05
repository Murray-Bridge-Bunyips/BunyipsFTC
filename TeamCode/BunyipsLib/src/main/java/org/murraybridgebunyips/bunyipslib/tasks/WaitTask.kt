package org.murraybridgebunyips.bunyipslib.tasks

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode

/**
 * Task to wait for a specific amount of time.
 */
class WaitTask(private val opMode: BunyipsOpMode, time: Double) : Task(time), RobotTask {
    override fun init() {
        return
    }

    override fun run() {
        opMode.addTelemetry("Waiting % seconds...", time)
        opMode.idle()
    }

    override fun isTaskFinished(): Boolean {
        return false
    }

    override fun onFinish() {
        return
    }
}