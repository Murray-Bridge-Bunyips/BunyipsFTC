package org.murraybridgebunyips.jerry.tasks

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.CartesianMecanumDrive
import org.murraybridgebunyips.bunyipslib.tasks.bases.BunyipsTask
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask

/**
 * Base drive task which will run Cartesian XYR speed for a given time
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
) : BunyipsTask(opMode, time), RobotTask {
    override fun init() {
        return
    }

    override fun run() {
        drive?.setSpeedXYR(x, y, r)
        drive?.update()
    }

    override fun isTaskFinished(): Boolean {
        return false
    }

    override fun onFinish() {
        drive?.stop()
    }
}