package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm

/**
 * Autonomous operation arm control task for Jerry robot.
 */
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
        val res = arm.liftSetPosition(targetposition)
        if (!res) {
            throw IllegalArgumentException("Invalid target position: $targetposition")
        }
    }

    override fun run() {
        arm.update()
    }
}