package org.firstinspires.ftc.teamcode.common.tasks

interface TaskImpl {
    fun init()
    fun run()
    fun isFinished(): Boolean
}