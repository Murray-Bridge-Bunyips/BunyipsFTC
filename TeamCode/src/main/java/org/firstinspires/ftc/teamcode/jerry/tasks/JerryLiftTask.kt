package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryLift

/**
 * Autonomous operation lift control task for Jerry robot.
 * Takes in a desired percentage of arm extension.
 */
class JerryLiftTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val lift: JerryLift?,
    private val percent: Int,
    private val power: Double? = null,
) : Task(opMode, time), TaskImpl {

    override fun init() {
        super.init()
        if (power != null) {
            lift?.power = power
        }
        lift?.set(percent)
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || lift?.isBusy() == false
    }

    override fun run() {
        if (isFinished()) return
        lift?.update()
    }
}