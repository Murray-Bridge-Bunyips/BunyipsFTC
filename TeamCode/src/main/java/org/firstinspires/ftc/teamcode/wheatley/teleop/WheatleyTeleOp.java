package org.firstinspires.ftc.teamcode.wheatley.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyMecanumDrive;

/**
 * Primary TeleOp for all of Wheatley's functions.
 * Uses gamepad1 for drive control (presumably) and gamepad2 for... something
 * > gamepad1 left stick for driving
 * > gamepad1 right stick for turning
 *
 * @author Lachlan Paul, 2023
 */

@TeleOp(name = "WHEATLEY: TeleOp", group = "WHEATLEY")
public class WheatleyTeleOp extends BunyipsOpMode {

    private WheatleyConfig config = new WheatleyConfig();
    private WheatleyMecanumDrive drive;

    @Override
    protected void onInit() {

        config = (WheatleyConfig) RobotConfig.newConfig(this, config, hardwareMap);

        drive = new WheatleyMecanumDrive(this, config.frontLeft, config.backLeft, config.frontRight, config.backRight);;

    }

    @Override
    protected void activeLoop() {

        drive.setSpeedXYR(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Adds a message on the driver hub stating the status of different controller inputs
        addTelemetry("Left Stick Y: " + gamepad1.left_stick_y);
        addTelemetry("Left Stick X: " + gamepad1.left_stick_x);

        // Gives a different message based on whether or not the camera is connected
        if (config.affirm(config.webCam)) {
            addTelemetry("Camera is connected");
        } else {
            addTelemetry("Camera is NOT connected");
        }

        /*
         * TODO: Pick out some good Wheatley voice lines for telemetry
         * Different lines will be displayed depending on different values
         * They should overwrite each other and NOT stack
         */
    }
}
