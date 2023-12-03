package org.murraybridgebunyips.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.Vision
import org.murraybridgebunyips.jerry.components.JerryConfig

/**
 * Test opmode for new vision wrappers
 */
//@Disabled
@TeleOp(name = "Test Vision Wrapper")
class JerryNewVision : BunyipsOpMode() {
    private var config = JerryConfig()
    private var vision: Vision? = null

    override fun onInit() {
        config.init(this)
        vision = Vision(this, config.webcam!!)
//        vision?.init(Vision.Processors.APRILTAG)
//        vision?.start(Vision.Processors.APRILTAG)
    }

    override fun activeLoop() {
//        vision?.tick()
        addTelemetry("FPS: ${vision?.fps}")
        addTelemetry("Status: ${vision?.status}")
//        addTelemetry("TFOD: ${vision?.tfodData}")
//        addTelemetry("AprilTag: ${vision?.aprilTagData}")
    }
}