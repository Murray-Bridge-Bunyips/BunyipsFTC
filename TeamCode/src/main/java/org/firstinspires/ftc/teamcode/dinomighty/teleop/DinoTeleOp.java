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

@TeleOp(name="DINOMIGHTY TeleOp", group="DINOMIGHTY")
public class DinoTeleOp extends BunyipsOpMode {

    private DinoConfig config = new DinoConfig();
    private DinoMecanumDrive drive;
    private DinoLift lift;

    @Override
    protected void onInit() {

        // This line is required to initialise config and allow you to access all your instance
        // variables declared in the config class. This is required for all OpMode classes.
        config = (DinoConfig) RobotConfig.newConfig(this, config, hardwareMap);

        // Initialise all components
        drive = new DinoMecanumDrive(this, config.frontLeft, config.backLeft, config.frontRight, config.backRight);
        lift = new DinoLift(this, config.arm, config.frontServo, config.backServo);
    }

    // activeLoop runs every hardware cycle
    @Override
    protected void activeLoop() {

        // Sets controller bindings for the robot controllers
        drive.setSpeedXYR(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        lift.armLift(gamepad2.left_stick_y);

        // If this wasn't set in if statements, the claw would open/close rapidly
        // In other words, any button inputs must be set to if statements
        // Stick inputs can be set as they are above
        if(gamepad2.x){
            lift.clawOpen();
        } else if(gamepad2.y){
            lift.clawClose();
        }

        // Adds a message on the driver hub stating the status of different controlelr inputs
        addTelemetry("Left Stick Y: " + gamepad1.left_stick_y, false);
        addTelemetry("Left Stick X: " + gamepad1.left_stick_x, false);
        addTelemetry("Right Stick X: " + gamepad1.right_stick_x, false);
    
        // Updates the speeds of the motors
        // While it doesn't matter where this is because activeLoop runs so fast
        // It's still good practice to put update methods at the bottom
        lift.update();
        drive.update();
    }
}
