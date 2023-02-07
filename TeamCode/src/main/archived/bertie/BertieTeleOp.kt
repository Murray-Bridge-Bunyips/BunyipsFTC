package org.firstinspires.ftc.archived.bertie

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.archived.bertie.components.BertieArm
import org.firstinspires.ftc.archived.bertie.components.BertieConfig
import org.firstinspires.ftc.archived.bertie.components.BertieDrive
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@TeleOp(name = "<BERTIE> TeleOp")
@Disabled
class BertieTeleOp : BunyipsOpMode() {
    private var config: BertieConfig? = null
    private var drive: BertieDrive? = null
    private var lift: BertieArm? = null
    private var up_pressed = false
    private var down_pressed = false
    private var drop_pressed = false
    override fun onInit() {
        config = BertieConfig.newConfig(hardwareMap, telemetry)
        try {
            drive = BertieDrive(
                this,
                config?.frontLeft, config?.frontRight,
                config?.backLeft, config?.backRight,
                false
            )
            lift = BertieArm(this, config?.armMotor)
        } catch (e: Exception) {
            telemetry.addLine("Failed to initialise motors.")
        }
    }

    @Throws(InterruptedException::class)
    override fun activeLoop() {
        val x = gamepad1.right_stick_x.toDouble()
        val y = gamepad1.left_stick_y.toDouble()
        val r = gamepad1.left_stick_x.toDouble()
        drive?.setSpeedXYR(y, -x, r)
        drive?.update()
        lift?.setPower(0.3)
        if (up_pressed && !gamepad2.dpad_up) {
            lift?.liftUp()
        } else if (down_pressed && !gamepad2.dpad_down) {
            lift?.liftDown()
        } else if (drop_pressed && !gamepad2.left_bumper) {
            lift?.liftReset()
        }
        if (gamepad2.a) {
            config?.spinIntake?.power = -1.0
        } else if (gamepad2.b) {
            config?.spinIntake?.power = 1.0
        } else {
            config?.spinIntake?.power = 0.0
        }
        if (gamepad2.left_bumper) {
            if (gamepad2.x) {
                config?.carouselLeft?.power = -1.0
            } else if (gamepad2.y) {
                config?.carouselLeft?.power = 1.0
            }
        } else if (gamepad2.right_bumper) {
            if (gamepad2.x) {
                config?.carouselRight?.power = -1.0
            } else if (gamepad2.y) {
                config?.carouselRight?.power = 1.0
            }
        } else {
            config?.carouselLeft?.power = 0.0
            config?.carouselRight?.power = 0.0
        }
        up_pressed = gamepad2.dpad_up
        down_pressed = gamepad2.dpad_down
        drop_pressed = gamepad2.left_bumper
        lift?.update()
    }
}