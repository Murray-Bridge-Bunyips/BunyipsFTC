package org.firstinspires.ftc.teamcode.lisa.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

/**
 * Base drive task to use left-right power on time to control motors.
 */
@Deprecated("Use LisaPrecisionDriveTask instead")
class LisaTimeDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: LisaDrive,
    private val leftSpeed: Double,
    private val rightSpeed: Double
) : Task(opMode, time), TaskImpl {

    override fun run() {
        drive.setPower(-leftSpeed, -rightSpeed)
        drive.update()
        if (isFinished()) {
            drive.setPower(0.0, 0.0)
            drive.update()
            return
        }
    }
}