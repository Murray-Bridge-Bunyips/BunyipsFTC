package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.ButtonControl
import org.firstinspires.ftc.teamcode.common.ButtonHashmap
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryTimeDriveTask
import java.util.ArrayDeque

/**
 * Basic autonomous that guarantees the robot will park in a corner or centre.
 * Incredibly simple, uses only time drive.
 */
@Autonomous(name = "<JERRY> POWERPLAY Auto Left-Right Park")
class JerryBasicGuaranteeAutonomous : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var drive: JerryDrive? = null
    private val tasks = ArrayDeque<TaskImpl>()
    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        try {
            drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Drive System.")
        }
        val selectedButton = ButtonHashmap.map(this, "Red Drive Left", "Blue Drive Right", "", "")
        when (selectedButton) {
            ButtonControl.A -> // Move left
                tasks.add(JerryTimeDriveTask(this, 1.5, drive, 1.0, 0.0, 0.0))

            ButtonControl.B -> // Move right
                tasks.add(JerryTimeDriveTask(this, 1.5, drive, -1.0, 0.0, 0.0))

            else -> {}
        }
        telemetry.addLine("Ready to go under config: $selectedButton")
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val currentTask = tasks.peekFirst() ?: return
        currentTask.run()
        if (currentTask.isFinished()) {
            tasks.removeFirst()
        }
        if (tasks.isEmpty()) {
            drive?.deinit()
        }
    }
}