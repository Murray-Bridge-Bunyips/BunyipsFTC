package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryLift

/**
 * Manual arm control used for calibration purposes, using gamepad2 left stick.
 */
@TeleOp(name = "<JERRY> Manual Arm Control")
class JerryArmControl : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var arm: JerryLift? = null

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        arm = JerryLift(
            this,
            config?.claw,
            config?.arm1,
            config?.arm2,
            config?.limit
        )
    }

    override fun activeLoop() {
        arm?.adjust(gamepad2.left_stick_y.toDouble())
        // Calculates the average position of the lift motors
        telemetry.addLine("Lift Position: ${(config?.arm1?.currentPosition!! + config?.arm2?.currentPosition!!) / 2}")
        arm?.update()
    }
}