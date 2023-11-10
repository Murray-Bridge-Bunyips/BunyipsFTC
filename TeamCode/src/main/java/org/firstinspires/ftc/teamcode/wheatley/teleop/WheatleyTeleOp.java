package org.firstinspires.ftc.teamcode.wheatley.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Cannon;
import org.firstinspires.ftc.teamcode.common.NullSafety;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyLift;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyManagementRail;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyMecanumDrive;

/**
 * Primary TeleOp for all of Wheatley's functions.
 * Uses gamepad1 for drive control (presumably) and gamepad2 for both arms/claws
 * > gamepad1 left stick for driving
 * > gamepad1 right stick for turning
 * > gamepad2 left stick for Pixel Claw arm
 * > gamepad2 right stick for Suspender arm (only works when arm is released)
 * > gamepad2 dpad up releases the suspender
 * > gamepad1 right trigger fires paper plane
 * > gamepad2 x operates the left pixel claw
 * > gamepad2 y operates the right pixel claw
 *
 * @author Lachlan Paul, 2023
 */

@TeleOp(name = "WHEATLEY: TeleOp", group = "WHEATLEY")
public class WheatleyTeleOp extends BunyipsOpMode {

    private WheatleyConfig config = new WheatleyConfig();
    private WheatleyMecanumDrive drive;
    private WheatleyLift lift;
    private WheatleyManagementRail suspender; // no way it's the wheatley management rail:tm:
    private Cannon cannon;

    @Override
    protected void onInit() {
        config = (WheatleyConfig) RobotConfig.newConfig(this, config, hardwareMap);
        drive = new WheatleyMecanumDrive(this, config.fl, config.bl, config.fr, config.br);
        lift = new WheatleyLift(this, config.ra, config.ls, config.rs);
        suspender = new WheatleyManagementRail(this, config.se, config.pl);
        cannon = new Cannon(this, config.pl);
    }

    @Override
    protected void activeLoop() {
        drive.setSpeedUsingController(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        drive.update();

        lift.armLiftController(gamepad2.left_stick_y);
        lift.update();

        suspender.hookArm(gamepad2.right_stick_y);

        // Launches the paper plane
        // The triggers are pressure sensitive, apparently.
        // Set to 1 to avoid any slight touches launching a nuke.
        if (gamepad1.right_trigger == 1) {
            cannon.fire();
        }

        if (gamepad2.dpad_up) {
            suspender.release();
        }

        // Claw controls
        if (gamepad2.x) {
            lift.leftClaw();
        } else if (gamepad2.y) {
            lift.rightClaw();
        }

        // Adds a message on the driver hub stating the status of different controller inputs
        addTelemetry("Left Stick Y: " + gamepad1.left_stick_y);
        addTelemetry("Left Stick X: " + gamepad1.left_stick_x);

        // Gives a different message based on whether or not the camera is connected
        if (NullSafety.assertNotNull(config.webcam)) {
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
