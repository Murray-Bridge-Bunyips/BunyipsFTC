package org.firstinspires.ftc.teamcode.common.tasks

/**
 * Implementation of task system, inherit when creating a task.
 */
interface TaskImpl {
    fun init()
    fun run()
    fun isFinished(): Boolean
}