package org.firstinspires.ftc.teamcode.bertie

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.bertie.components.BertieArm
import org.firstinspires.ftc.teamcode.bertie.components.BertieConfig
import org.firstinspires.ftc.teamcode.bertie.components.BertieDrive
import org.firstinspires.ftc.teamcode.bertie.tasks.BertieTimeDriveTask
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.ButtonControl
import org.firstinspires.ftc.teamcode.common.ButtonHashmap
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import java.util.ArrayDeque

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@Autonomous(name = "<BERTIE> Autonomous Testing")
@Disabled
class BertieAutonomous : BunyipsOpMode() {
    private var config: BertieConfig? = null
    private var drive: BertieDrive? = null
    private val lift: BertieArm? = null
    private val tasks = ArrayDeque<TaskImpl>()
    override fun onInit() {
        config = BertieConfig.newConfig(hardwareMap, telemetry)
        try {
            drive = BertieDrive(
                this,
                config!!.frontLeft, config!!.frontRight,
                config!!.backLeft, config!!.backRight,
                false
            )
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise motors.")
        }
        val selectedButton = ButtonHashmap.map(this, "Red", "Blue", "", "")
        when (selectedButton) {
            ButtonControl.A -> {
                tasks.add(MessageTask(this, 1.0, "Loaded red"))
                tasks.add(BertieTimeDriveTask(this, 0.5, drive, 1.0, 0.0, 0.0))
                tasks.add(BertieTimeDriveTask(this, 0.5, drive, 0.0, 1.0, 0.0))
                tasks.add(BertieTimeDriveTask(this, 1.0, drive, -1.0, 0.0, 0.0))
                tasks.add(BertieTimeDriveTask(this, 1.0, drive, 0.0, -1.0, 0.0))
            }

            ButtonControl.B -> tasks.add(MessageTask(this, 1.0, "Loaded blue"))

            else -> {}
        }
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val currentTask = tasks.peekFirst() ?: return
        currentTask.run()
        if (currentTask.isFinished()) {
            tasks.removeFirst()
        }
        if (tasks.isEmpty()) {
            drive!!.setSpeedXYR(0.0, 0.0, 0.0)
            drive!!.update()
        }
    }
}