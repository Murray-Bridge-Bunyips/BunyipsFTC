package org.firstinspires.ftc.teamcode.jerry

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive

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
        try {
            drive = JerryDrive(this, config?.bl, config?.br, config?.fl, config?.fr)
            drive?.setToBrake()
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Drive System.")
        }
        try {
            arm = JerryArm(
                this,
                config?.claw1,
                config?.claw2,
                config?.arm1,
                config?.arm2,
                config?.limit
            )
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise Arm System.")
        }
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        // Set changing variables and gather raw data
        val x = gamepad1.left_stick_x.toDouble()
        val y = gamepad1.left_stick_y.toDouble()
        val r = gamepad1.right_stick_x.toDouble()

        // Set speeds of motors and interpret any data
        drive?.setSpeedXYR(-x, -y, -r)
        arm?.liftSetPower(0.2)
        if (up_pressed && !gamepad2.dpad_up) {
            arm?.liftUp()
        } else if (down_pressed && !gamepad2.dpad_down) {
            arm?.liftDown()
        } else if (drop_pressed && !gamepad2.left_bumper) {
            arm?.liftReset()
        }

        // Update live movements of all motors
        if (gamepad2.a) {
            // "Green for seen"
            arm?.clawOpen()
        } else if (gamepad2.b) {
            // "Red for dead"
            arm?.clawClose()
        }
        up_pressed = gamepad2.dpad_up
        down_pressed = gamepad2.dpad_down
        drop_pressed = gamepad2.left_bumper
        drive?.update()
        arm?.update()
    }
}