package org.firstinspires.ftc.team15215.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.team15215.jerry.components.JerryConfig
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.ftc.bunyipslib.OpenCVCam
import org.murraybridgebunyips.ftc.bunyipslib.tasks.GetSignalTask

/**
 * Debug OpMode to continually output what AprilTag position the robot is currently seeing.
 */
@TeleOp(name = "JERRY: PowerPlay Signal Debug", group = "JERRY")
class JerrySignalAnalyse : BunyipsOpMode() {
    private var config = JerryConfig()
    private var cam: OpenCVCam? = null
    private var task: GetSignalTask? = null

    override fun onInit() {
        config.init(this)
        cam = OpenCVCam(this, config.webcam, config.monitorID)
        task = cam?.let { GetSignalTask(this, it) }
    }

    override fun activeLoop() {
        task?.run()
        addTelemetry("Currently seeing position: ${task?.position ?: "NONE"}")
    }
}