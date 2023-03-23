package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

/**
 * Primary TeleOp for all of Jerry's functions. Uses gamepad1 for driving and gamepad2 for arm.
 * Standard controls involve gamepad1 left stick for driving, gamepad1 right stick for turning,
 * gamepad2 dpad up/down for lift, gamepad2 left bumper for reset, gamepad2 a/b for claw, use gamepad2 right bumper to cancel calibration.
 */
@TeleOp(name = "<JERRY> POWERPLAY TeleOp")
class JerryTeleOp : BunyipsOpMode() {
    private var drive: JerryDrive? = null
    private var arm: JerryArm? = null
    private var config: JerryConfig? = null
    private var up_pressed = false
    private var down_pressed = false
    private var drop_pressed = false

    override fun onInit() {
        // Configure drive and arm subsystems
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
        drive?.setToBrake()
        arm = JerryArm(
            this,
            config?.claw1,
            config?.claw2,
            config?.arm1,
            config?.arm2,
            config?.limit
        )

        // Run any additional configuration
        arm?.liftSetPower(0.2)
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        // Set changing variables and gather raw data
        val x = gamepad1.left_stick_x.toDouble()
        val y = gamepad1.left_stick_y.toDouble()
        val r = gamepad1.right_stick_x.toDouble()

//        telemetry.addLine(String.format(Locale.getDefault(),
//            "Controller: X: %.2f, Y: %.2f, R: %.2f", x, y, r))

        // Set speeds of motors and interpret any data
        drive?.setSpeedXYR(x, y, r)
        if (up_pressed && !gamepad2.dpad_up) {
            arm?.liftUp()
        } else if (down_pressed && !gamepad2.dpad_down) {
            arm?.liftDown()
        } else if (drop_pressed && !gamepad2.left_bumper) {
            arm?.liftReset()
        }

        // Update live movements of all motors
        if (gamepad2.a) {
            // "Green (un)for seen(consequences)"
            arm?.clawOpen()
        } else if (gamepad2.b) {
            // "Red for dead 2"
            arm?.clawClose()
        }

        // Prevent continued presses of buttons due to the nature of the active loop
        up_pressed = gamepad2.dpad_up
        down_pressed = gamepad2.dpad_down
        drop_pressed = gamepad2.left_bumper

        drive?.update()
        arm?.update()
    }
}