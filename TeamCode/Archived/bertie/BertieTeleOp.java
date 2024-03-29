package org.firstinspires.ftc.teamcode.bertie;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.bertie.components.BertieArm;
import org.firstinspires.ftc.teamcode.bertie.components.BertieConfig;
import org.firstinspires.ftc.teamcode.bertie.components.BertieDrive;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@TeleOp(name = "<BERTIE> TeleOp")
@Disabled
public class BertieTeleOp extends BunyipsOpMode {
    private BertieConfig config;
    private BertieDrive drive = null;
    private BertieArm lift = null;
    private boolean up_pressed = false;
    private boolean down_pressed = false;
    private boolean drop_pressed = false;

    @Override
    protected void onInit() {
        config = BertieConfig.newConfig(hardwareMap, telemetry);

        try {
            drive = new BertieDrive(this,
            config.frontLeft, config.frontRight,
            config.backLeft, config.backRight,
            false);
            lift = new BertieArm(this, config.armMotor);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise motors.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        double x = gamepad1.right_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.left_stick_x;
        drive.setSpeedXYR(y, -x, r);
        drive.update();

        lift.setPower(0.3);
        if (up_pressed && !gamepad2.dpad_up) {
            lift.liftUp();
        } else if (down_pressed && !gamepad2.dpad_down) {
            lift.liftDown();
        } else if (drop_pressed && !gamepad2.left_bumper) {
            lift.liftReset();
        }

        if (gamepad2.a) {
            config.spinIntake.setPower(-1);
        } else if (gamepad2.b) {
            config.spinIntake.setPower(1);
        } else {
            config.spinIntake.setPower(0);
        }

        if (gamepad2.left_bumper) {
            if (gamepad2.x) {
                config.carouselLeft.setPower(-1);
            } else if (gamepad2.y) {
                config.carouselLeft.setPower(1);
            }
        } else if (gamepad2.right_bumper) {
            if (gamepad2.x) {
                config.carouselRight.setPower(-1);
            } else if (gamepad2.y) {
                config.carouselRight.setPower(1);
            }
        } else {
            config.carouselLeft.setPower(0);
            config.carouselRight.setPower(0);
        }

        up_pressed = gamepad2.dpad_up;
        down_pressed = gamepad2.dpad_down;
        drop_pressed = gamepad2.left_bumper;

        lift.update();
    }
}