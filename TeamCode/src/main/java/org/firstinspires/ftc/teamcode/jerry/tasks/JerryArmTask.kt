package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryLift

/**
 * Autonomous operation arm control task for Jerry robot.
 */
class JerryArmTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val lift: JerryLift?,
    private val speed: Double,
    private val targetposition: Int
) : Task(opMode, time), TaskImpl {
    override fun init() {
        super.init()
        lift?.power = speed
        lift?.set(targetposition)
    }

    override fun run() {
        lift?.update()
    }
}