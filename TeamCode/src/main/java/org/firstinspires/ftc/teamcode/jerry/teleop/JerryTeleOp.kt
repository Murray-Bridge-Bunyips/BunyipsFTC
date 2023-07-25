package org.firstinspires.ftc.teamcode.jerry.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.RobotConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.jerry.components.JerryLift

/**
 * Primary TeleOp for all of Jerry's functions.
 *
 * Uses gamepad1 for drive control and gamepad2 for lift control.
 * > gamepad1 left stick for driving
 * > gamepad1 right stick for turning
 * > gamepad2 left stick for lift movement
 * > gamepad2 A to open claw
 * > gamepad2 B to close claw
 *
 * @author Lucas Bubner, 2022-2023
 */
@TeleOp(name = "JERRY: TeleOp", group = "JERRY")
class JerryTeleOp : BunyipsOpMode() {
    private var config = JerryConfig()
    private var drive: JerryDrive? = null
    private var lift: JerryLift? = null

    override fun onInit() {
        // Configure drive and lift subsystems
        config = RobotConfig.newConfig(this, config, hardwareMap) as JerryConfig
        if (config.assert(config.driveMotors)) {
            drive = JerryDrive(this, config.bl!!, config.br!!, config.fl!!, config.fr!!)
        }
        drive?.setToBrake()
        if (config.assert(config.armComponents)) {
            lift = JerryLift(
                this,
                JerryLift.ControlMode.MANUAL,
                config.claw!!,
                config.arm1!!,
                config.arm2!!,
                config.limit!!
            )
        }
    }

    override fun activeLoop() {
        // Set changing variables and gather raw data
        val x = gamepad1.left_stick_x.toDouble()
        val y = gamepad1.left_stick_y.toDouble()
        val r = gamepad1.right_stick_x.toDouble()
        val v = gamepad2.left_stick_y.toDouble()

//        addTelemetry(String.format(Locale.getDefault(),
//            "Controller: X: %.2f, Y: %.2f, R: %.2f", x, y, r))

        // Set speeds of motors and interpret any data
        drive?.setSpeedXYR(x, y, r)
        lift?.delta(v)
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
