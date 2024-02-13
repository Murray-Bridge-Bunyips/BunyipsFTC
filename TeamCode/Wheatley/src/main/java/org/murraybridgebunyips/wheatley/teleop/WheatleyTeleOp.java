package org.murraybridgebunyips.wheatley.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.DualClaws;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.NullSafety;
import org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.PersonalityCoreArm;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * Primary TeleOp for all of Wheatley's functions.
 * gamepad1:
 *      left_stick_x: strafe
 *      left_stick_y: forward/backward
 *      right_stick_x: turn
 *      right_trigger: fire cannon
 *      options: reset cannon
 * gamepad2:
 *      x: toggle left claw
 *      b: toggle right claw
 *      y: align claw to board
 *      a: align claw to ground
 *      left_stick_y: actuate the management rail
 *      right_stick_y: move claw mover
 *      dpad_up: extend hook one position
 *      dpad_down: retract hook one position
 *
 * @author Lachlan Paul, 2023
 * @author Lucas Bubner, 2023
 */

@TeleOp(name = "TeleOp")
public class WheatleyTeleOp extends BunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;
    private Cannon cannon;
    private PersonalityCoreArm arm;

    private boolean xPressed;
    private boolean bPressed;

    @Override
    protected void onInit() {
        config.init();
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br
        );
        if (NullSafety.assertComponentArgs(Cannon.class, config.launcher))
            cannon = new Cannon(config.launcher);
        arm = new PersonalityCoreArm(config.pixelMotion, config.pixelAlignment,
                config.suspenderHook, config.suspenderActuator, config.leftPixel, config.rightPixel
        );
    }

    @Override
    protected void activeLoop() {
        drive.setSpeedUsingController(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // Launches the paper plane
        // The triggers are pressure sensitive, apparently.
        // Set to 1 to avoid any slight touches launching a nuke.
        if (gamepad1.right_trigger == 1.0) {
            // "Now then, let's see what we got here. Ah! 'Reactor Core Emergency Heat Venting Protocols.'
            // that's the problem right there, isn't it? 'Emergency'. you don't want to see 'emergency'
            // flashing at you. Never good that, is it? Right. DELETE."
            cannon.fire();
        }

        // Reset cannon for debugging purposes
        if (gamepad1.back) {
            // "Undelete, undelete! Where's the undelete button?"
            cannon.reset();
        }

        // Claw controls
        if (gamepad2.x && !xPressed) {
            arm.toggleClaw(DualClaws.ServoSide.LEFT);
        } else if (gamepad2.b && !bPressed) {
            arm.toggleClaw(DualClaws.ServoSide.RIGHT);
        }

        // Claw alignment
        if (gamepad2.y) {
            arm.faceClawToBoard();
        } else if (gamepad2.a) {
            arm.faceClawToGround();
        }
        arm.actuateClawRotatorUsingController(gamepad2.right_stick_y);

        // Management rail controls
        arm.actuateManagementRailUsingController(gamepad2.left_stick_y);

        // Claw mover controls
        arm.actuateClawMoverUsingDpad(gamepad2.dpad_up, gamepad2.dpad_down);

        // Hook controls
        if (gamepad1.dpad_up) {
            arm.extendHook();
        } else if (gamepad1.dpad_down) {
            arm.retractHook();
        }

        // Register actions only once per press
        xPressed = gamepad2.x;
        bPressed = gamepad2.b;

        // Send stateful updates to the hardware
        drive.update();
        arm.update();
        cannon.update();
    }
}