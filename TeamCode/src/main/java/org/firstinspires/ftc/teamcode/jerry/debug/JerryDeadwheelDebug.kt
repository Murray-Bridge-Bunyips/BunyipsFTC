package org.firstinspires.ftc.teamcode.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Deadwheels
import org.firstinspires.ftc.teamcode.common.XYEncoder
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig

/**
 * Debug opmode for deadwheel readouts.
 */
@TeleOp(name = "<JERRY> Deadwheel Debug Data")
class JerryDeadwheelDebug : BunyipsOpMode() {
    private lateinit var config: JerryConfig
    private var pos: Deadwheels? = null

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap)
        logHardwareErrors(config.hardwareErrors)
        if (!config.hasHardwareErrors(config.fl, config.fr)) {
            pos = Deadwheels(this, config.fl!!, config.fr!!)
        }
        pos?.enableTracking(XYEncoder.Axis.BOTH)
    }

    override fun activeLoop() {
        addTelemetry("X Encoder: ${pos?.encoderReading(XYEncoder.Axis.X)}")
        addTelemetry("Y Encoder: ${pos?.encoderReading(XYEncoder.Axis.Y)}")
        addTelemetry("X MM: ${pos?.travelledMM(XYEncoder.Axis.X)}")
        addTelemetry("Y MM: ${pos?.travelledMM(XYEncoder.Axis.Y)}")
    }
}