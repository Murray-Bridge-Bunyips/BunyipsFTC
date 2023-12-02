package org.firstinspires.ftc.team15215.jerry.tasks

import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.ftc.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.ftc.bunyipslib.tasks.AutoTask

/**
 * Base drive task which will run XYR speed for a given time
 * Only used for tests and as a failsafe, do not use in actual OpMode as field positioning data is lost
 * Use JerryPrecisionDriveTask instead!
 * @see JerryPrecisionDriveTask
 */
class JerryTimeDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: CartesianMecanumDrive?,
    private val x: Double,
    private val y: Double,
    private val r: Double
) : Task(opMode, time), AutoTask {
    override fun init() {
        return
    }

    override fun run() {
        drive?.setSpeedUsingController(x, -y, r)
        drive?.update()
    }

    override fun isTaskFinished(): Boolean {
        return false
    }

    override fun onFinish() {
        drive?.stop()
    }
}