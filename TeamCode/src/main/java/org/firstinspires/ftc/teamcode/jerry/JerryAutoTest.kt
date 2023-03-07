package org.firstinspires.ftc.teamcode.jerry

import android.os.Message
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import java.util.ArrayDeque

class JerryAutoTest : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var drive: JerryDrive? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        drive?.setToBrake()
        tasks.add(MessageTask(this, 1.0, "well here we are again"))
    }

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