package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm

class JerryArmTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val arm: JerryArm,
    private val speed: Double,
    private val targetposition: Int
) : Task(opMode, time), TaskImpl {
    override fun init() {
        super.init()
        arm.liftSetPower(speed)
        arm.liftSetPosition(targetposition)
    }

    override fun run() {
        arm.update()
    }
}