package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig

@TeleOp(name = "<JERRY> Deadwheel Debug Data")
class JerryDeadwheelDebug : BunyipsOpMode() {
    private var config: JerryConfig? = null

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
//        config!!.fl?.enableTracking()
//        config!!.fr?.enableTracking()
        config!!.fr?.mode = DcMotor.RunMode.RUN_USING_ENCODER
        config!!.fl?.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    override fun activeLoop() {
        telemetry.addLine("X Deadwheel: ${config?.fl?.currentPosition}")
        telemetry.addLine("Y Deadwheel: ${config?.fr?.currentPosition}")
        telemetry.addLine("Mode of deadwheels: ${config?.fl?.mode} and ${config?.fr?.mode}")
        telemetry.addLine("deadwheels: ${config?.fl} and ${config?.fr}")
    }
}