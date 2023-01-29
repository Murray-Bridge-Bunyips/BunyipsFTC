package org.firstinspires.ftc.teamcode.bertie.tasks

import org.firstinspires.ftc.teamcode.bertie.components.BertieDrive
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
class BertieTimeDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: BertieDrive?,
    private val x: Double,
    private val y: Double,
    private val r: Double
) : Task(opMode, time), TaskImpl {

    override fun run() {
        drive!!.setSpeedXYR(x, y, r)
        drive.update()
        if (isFinished()) {
            drive.setSpeedXYR(0.0, 0.0, 0.0)
            drive.update()
            return
        }
    }
}