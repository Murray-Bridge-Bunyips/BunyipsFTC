package org.firstinspires.ftc.teamcode.lisa

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.lisa.components.LisaConfig
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

@TeleOp(name = "<LISA> TeleOp")
class LisaTeleOp : BunyipsOpMode() {
    private var config: LisaConfig? = null
    private var drive: LisaDrive? = null
    override fun onInit() {
        config = LisaConfig.newConfig(hardwareMap, telemetry)
        try {
            drive = LisaDrive(
                this,
                config?.left, config?.right
            )
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise motors.")
        }
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val leftPower =
            (gamepad1.left_trigger - gamepad1.right_trigger + gamepad1.left_stick_x).toDouble()
        val rightPower =
            (gamepad1.left_trigger - gamepad1.right_trigger - gamepad1.left_stick_x).toDouble()
        drive?.setPower(rightPower, leftPower)
        drive?.update()
    }
}