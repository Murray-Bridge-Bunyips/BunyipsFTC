package org.firstinspires.ftc.teamcode.lisa.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.lisa.components.LisaConfig
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

/**
 * Basic TeleOp to use controllers to operate the minibot.
 * gamepad1 triggers control acceleration, gamepad1 left stick controls turning.
 */
@TeleOp(name = "<LISA> TeleOp")
class LisaTeleOp : BunyipsOpMode() {
    private lateinit var config: LisaConfig
    private var drive: LisaDrive? = null
    override fun onInit() {
        config = LisaConfig.newConfig(hardwareMap)
        if (config.assert(config.motors)) {
            drive = LisaDrive(
                this,
                config.left!!, config.right!!
            )
        }
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val left =
            (gamepad1.left_trigger - gamepad1.right_trigger - gamepad1.left_stick_x).toDouble()
        val right =
            (gamepad1.left_trigger - gamepad1.right_trigger + gamepad1.left_stick_x).toDouble()
        drive?.setPower(left, right)
        drive?.update()
    }
}