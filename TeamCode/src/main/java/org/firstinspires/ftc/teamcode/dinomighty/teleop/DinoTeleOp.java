package org.firstinspires.ftc.teamcode.dinomighty.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.dinomighty.components.DinoConfig;
import org.firstinspires.ftc.teamcode.dinomighty.components.DinoLift;
import org.firstinspires.ftc.teamcode.dinomighty.components.DinoMecanumDrive;

/**
 * Primary TeleOp for all of DinoMighty's functions.
 * Uses gamepad1 for drive control and gamepad2 for lift control.
 * > gamepad1 left stick for driving
 * > gamepad1 right stick for turning
 * > gamepad2 left stick for lift movement
 * > gamepad2 A to open claw
 * > gamepad2 B to close claw
 *
 * @author Lachlan Paul, 2023
 */

@TeleOp(name="DinoMighty TeleOp")
public class DinoTeleOp extends BunyipsOpMode {

    private DinoConfig config;
    private DinoMecanumDrive drive;
    private DinoLift lift;

    @Override
    protected void onInit() {

        // This line is required to initialise config and allow you to access all your instance
        // variables declared in the config class. This is required for all OpMode classes.
        config = (DinoConfig) RobotConfig.newConfig(this, config, hardwareMap);

        // Initialise all components
        drive = new DinoMecanumDrive(this, config.frontLeft, config.backLeft, config.frontRight, config.backRight);
        lift = new DinoLift(this, config.arm);
    }

    // activeLoop runs every hardware cycle
    @Override
    protected void activeLoop() {

        lift.update();
        drive.update();

        lift.armLift(gamepad2.left_stick_y);

        // Adds a message on the driver hub stating the status of the left stick's Y axis
        addTelemetry("Left Stick Y: " + gamepad1.left_stick_y, false);
    }
}
