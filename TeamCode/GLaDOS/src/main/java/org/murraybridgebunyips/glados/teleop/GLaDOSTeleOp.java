package org.murraybridgebunyips.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.DualClaws;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.NullSafety;
import org.murraybridgebunyips.bunyipslib.TriSpeed;
import org.murraybridgebunyips.bunyipslib.bunyipsftc.personalitycore.PersonalityCoreArm;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * TeleOp for GLaDOS robot FTC 15215
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
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "TeleOp")
public class GLaDOSTeleOp extends BunyipsOpMode {
    private final TriSpeed speed = new TriSpeed(TriSpeed.Speed.NORMAL);
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private PersonalityCoreArm arm;
    private Cannon cannon;
    private boolean x_pressed;
    private boolean b_pressed;
    private boolean inc_pressed;
    private boolean dec_pressed;

    @Override
    protected void onInit() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelEncoder, config.perpendicularEncoder
        );
        if (NullSafety.assertComponentArgs(Cannon.class, config.launcher))
            cannon = new Cannon(config.launcher);
        arm = new PersonalityCoreArm(config.pixelMotion, config.pixelAlignment, config.suspenderHook, config.suspenderActuator, config.leftPixel, config.rightPixel);
    }

    @Override
    protected void activeLoop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;

        // Aperture Science Triple Velocity Augmentation Apparatus
        if (gamepad1.right_bumper && !inc_pressed) {
            speed.increment();
        } else if (gamepad1.left_bumper && !dec_pressed) {
            speed.decrement();
        }
        addTelemetry("TriSpeed: Running at % speed", speed.getSpeed());

        // 12 Volt Aperture Science Heavy Duty Super-Colliding Alliance-Destroying Mecanum Drive
        drive.setSpeedUsingController(x * speed.getMultiplier(), y * speed.getMultiplier(), r * speed.getMultiplier());

        // Aperture Science Pixel Grabbing Pixel Placing Pixel Claw
        if (gamepad2.x && !x_pressed) {
            arm.toggleClaw(DualClaws.ServoSide.LEFT);
        } else if (gamepad2.b && !b_pressed) {
            arm.toggleClaw(DualClaws.ServoSide.RIGHT);
        }

        // Aperture Science Dynamic Aperture Science Pixel Grabbing Pixel Placing Pixel Claw Angle Aligner
        if (gamepad2.y) {
            arm.faceClawToBoard();
        } else if (gamepad2.a) {
            arm.faceClawToGround();
        }
        arm.actuateClawRotatorUsingController(gamepad2.right_stick_y);

        // Aperture Science High Energy Pellet Launcher
        if (gamepad1.right_trigger == 1.0) {
            cannon.fire();
        }
        if (gamepad1.back) {
            cannon.reset();
        }

        // Aperture Science Multitasking Management Rail
        arm.actuateManagementRailUsingController(gamepad2.left_stick_y);

        // Aperture Science Gamepad-Enhanced Precision Pixel Grabbing Pixel Placing Pixel Claw Mover
        arm.actuateClawMoverUsingDpad(gamepad2.dpad_up, gamepad2.dpad_down);

        // Aperture Science Self-Suspending Suspender Suspension System
        if (gamepad1.dpad_up) {
            arm.extendHook();
        } else if (gamepad1.dpad_down) {
            arm.retractHook();
        }

        // Ensure that the buttons are only registered once per press
        x_pressed = gamepad2.x;
        b_pressed = gamepad2.b;
        inc_pressed = gamepad1.right_bumper;
        dec_pressed = gamepad1.left_bumper;

        // Dispatch stateful changes
        drive.update();
        arm.update();
        cannon.update();
    }
}
