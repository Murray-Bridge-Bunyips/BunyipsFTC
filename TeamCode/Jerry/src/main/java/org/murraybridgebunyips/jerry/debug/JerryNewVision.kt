package org.murraybridgebunyips.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.bunyipslib.Vision
import org.murraybridgebunyips.bunyipslib.cameras.C920
import org.murraybridgebunyips.bunyipslib.vision.AprilTag
import org.murraybridgebunyips.jerry.components.JerryConfig

/**
 * Test opmode for new vision wrappers
 */
//@Disabled
@TeleOp(name = "Test Vision Wrapper")
class JerryNewVision : BunyipsOpMode() {
    private var config = JerryConfig()
    private var vision: Vision? = null
    private var aprilTag = AprilTag(C920())

    override fun onInit() {
        config.init(this)
        vision = Vision(this, config.webcam!!)
        vision?.init(aprilTag)
        vision?.start(aprilTag)
    }

    override fun activeLoop() {
        vision?.tickAll() // alternatively aprilTag.tick()
        addTelemetry("FPS: ${vision?.fps}")
        addTelemetry("Status: ${vision?.status}")
        addTelemetry("AprilTag: ${aprilTag.data}")
    }
}