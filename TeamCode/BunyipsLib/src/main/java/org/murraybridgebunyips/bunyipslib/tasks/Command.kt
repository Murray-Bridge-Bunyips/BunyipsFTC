package org.murraybridgebunyips.bunyipslib.tasks

/**
 * Structure of a Command to execute through the Task system.
 */
interface Command {
    fun init()
    fun run()
    fun isFinished(): Boolean
    fun onFinish()
}