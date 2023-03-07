package org.firstinspires.ftc.teamcode.jerry

import android.os.Message
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import java.util.ArrayDeque

class JerryAutoTest : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var drive: JerryDrive? = null
    private var Krankenhaus: GetAprilTagTask? = null
    private var arm: JerryArm? = null
    private var cam: CameraOp? = null
    private val tasks = ArrayDeque<TaskImpl>()

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        drive?.setToBrake()
        tasks.add(MessageTask(this, 1.0, "well here we are again, it's always such a pleasure"))

        arm = JerryArm(
            this,
            config?.claw1,
            config?.claw2,
            config?.arm1,
            config?.arm2,
            config?.limit
        )

        cam = CameraOp(
            this,
            config?.webcam, config!!.monitorID, CameraOp.CamMode.OPENCV
        )


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

    override fun onInitLoop(): Boolean {
        // i have been informed I COULD change the name to something more convenient
        // but this
        // this is funnier
        Krankenhaus?.run()
        return Krankenhaus?.isFinished() ?: true
    }


}