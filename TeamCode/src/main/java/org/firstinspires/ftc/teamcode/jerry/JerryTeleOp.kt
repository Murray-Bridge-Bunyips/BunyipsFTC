package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.components.JerryLift

/**
 * Primary TeleOp for all of Jerry's functions. Uses gamepad1 for driving and gamepad2 for arm.
 * Standard controls involve gamepad1 left stick for driving, gamepad1 right stick for turning,
 * gamepad2 dpad up/down for lift, gamepad2 left bumper for recalibration, gamepad2 a/b for claw, use gamepad2 right bumper to cancel calibration.
 * gamepad2 right bumper + gamepad2 left stick y to control arm offset
 * @author Lucas Bubner - 2022
 * @author Lachlan Paul - 2023
 */
@TeleOp(name = "<JERRY> POWERPLAY TeleOp")
class JerryTeleOp : BunyipsOpMode() {
    private var drive: JerryDrive? = null
    private var lift: JerryLift? = null
    private var config: JerryConfig? = null

    override fun onInit() {
        // Configure drive and lift subsystems
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        drive?.setToBrake()
        lift = JerryLift(
            this,
            config?.claw1,
            config?.claw2,
            config?.arm1,
            config?.arm2,
            config?.limit
        )
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        // Set changing variables and gather raw data
        val x = gamepad1.left_stick_x.toDouble()
        val y = gamepad1.left_stick_y.toDouble()
        val r = gamepad1.right_stick_x.toDouble()
        val v = gamepad2.left_stick_y.toDouble()

//        telemetry.addLine(String.format(Locale.getDefault(),
//            "Controller: X: %.2f, Y: %.2f, R: %.2f", x, y, r))

        // Set speeds of motors and interpret any data
        drive?.setSpeedXYR(x, y, r)
        lift?.adjust(v)
        if (gamepad2.a) {
            lift?.open()
        } else if (gamepad2.b) {
            lift?.close()
        }

        if (gamepad2.left_bumper) {
            lift?.reset()
        }

        // Update live movements of all motors
        drive?.update()
        lift?.update()
    }
}