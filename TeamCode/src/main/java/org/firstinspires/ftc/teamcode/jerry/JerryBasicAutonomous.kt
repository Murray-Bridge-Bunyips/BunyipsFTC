package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryTimeDriveTask
import java.util.ArrayDeque

@Autonomous(name = "<JERRY> POWERPLAY Drive Forward Autonomous")
class JerryBasicAutonomous : BunyipsOpMode() {
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
        tasks.add(JerryTimeDriveTask(this, 1.0, drive, 0.0, 1.0, 0.0))
        telemetry.addLine("Ready to go. Parking position has been set to: CENTER")
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