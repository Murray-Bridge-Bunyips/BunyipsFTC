package org.murraybridgebunyips.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.OpenCVCam
import org.murraybridgebunyips.bunyipslib.tasks.GetSignalTask
import org.murraybridgebunyips.jerry.components.JerryConfig

/**
 * Debug OpMode to continually output what AprilTag position the robot is currently seeing.
 */
@TeleOp(name = "PowerPlay Signal Debug")
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