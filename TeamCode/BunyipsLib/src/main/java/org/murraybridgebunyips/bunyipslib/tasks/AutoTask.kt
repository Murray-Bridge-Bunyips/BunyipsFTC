package org.murraybridgebunyips.bunyipslib.tasks

/**
 * Structure of an Autonomous Task to be used during the autonomous period.
 */
interface AutoTask {
    fun init()
    fun run()
    fun isFinished(): Boolean
    fun onFinish()
}