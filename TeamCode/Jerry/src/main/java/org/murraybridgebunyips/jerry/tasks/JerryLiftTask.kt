package org.murraybridgebunyips.jerry.tasks

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.tasks.BunyipsTask
import org.murraybridgebunyips.bunyipslib.tasks.RobotTask
import org.murraybridgebunyips.bunyipslib.tasks.Task
import org.murraybridgebunyips.jerry.components.JerryLift

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
) : BunyipsTask(opMode, time), RobotTask {

    override fun init() {
        if (power != null) {
            lift?.power = power
        }
        lift?.set(percent)
    }

    override fun isTaskFinished(): Boolean {
        return lift?.isBusy() == false
    }

    override fun run() {
        lift?.update()
    }

    override fun onFinish() {
        return
    }
}