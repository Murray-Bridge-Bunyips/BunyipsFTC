package org.murraybridgebunyips.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.DualServos;
import org.murraybridgebunyips.bunyipslib.InputMultiplier;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreClawRotator;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreForwardServo;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreHook;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreLinearActuator;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * TeleOp for GLaDOS robot FTC 15215
 * gamepad1:
 * left_stick_x: strafe
 * left_stick_y: forward/backward
 * right_stick_x: turn
 * right_trigger: fire cannon
 * options: reset cannon
 * gamepad2:
 * x: toggle left claw
 * b: toggle right claw
 * y: align claw to board
 * a: align claw to ground
 * left_stick_y: actuate the management rail
 * right_stick_y: move claw mover
 * dpad_up: extend hook one position
 * dpad_down: retract hook one position
 *
 * @author Lucas Bubner, 2024
 */
@TeleOp(name = "OLD TeleOp")
public class GLaDOSTeleOpOld extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private InputMultiplier speed;
    private MecanumDrive drive;
    private PersonalityCoreClawRotator clawRotator;
    private PersonalityCoreForwardServo pixelMotion;
    private PersonalityCoreHook hook;
    private PersonalityCoreLinearActuator linearActuator;
    private DualServos claws;
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
                config.parallelDeadwheel, config.perpendicularDeadwheel
        );
        cannon = new Cannon(config.launcher);
        clawRotator = new PersonalityCoreClawRotator(config.pixelAlignment);
        pixelMotion = new PersonalityCoreForwardServo(config.pixelMotion);
        hook = new PersonalityCoreHook(config.suspenderHook);
        linearActuator = new PersonalityCoreLinearActuator(config.suspenderActuator);
        speed = new InputMultiplier(0.25, 0.5, 1.0).withDefaultIndex(1);
        claws = new DualServos(config.leftPixel, config.rightPixel, 0.0, 1.0, 1.0, 0.0);
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

        // 12 Volt Aperture Science Heavy Duty Super-Colliding Alliance-Destroying Mecanum Drive
        drive.setSpeedUsingController(x * speed.getMultiplier(), y * speed.getMultiplier(), r * speed.getMultiplier());

        // Aperture Science Pixel Grabbing Pixel Placing Pixel Claw
        if (gamepad2.x && !x_pressed) {
            claws.toggleServo(DualServos.ServoSide.LEFT);
        } else if (gamepad2.b && !b_pressed) {
            claws.toggleServo(DualServos.ServoSide.RIGHT);
        }

        // Aperture Science Dynamic Aperture Science Pixel Grabbing Pixel Placing Pixel Claw Angle Aligner
        if (gamepad2.y) {
            clawRotator.faceBoard();
        } else if (gamepad2.a) {
            clawRotator.faceGround();
        }
        clawRotator.actuateUsingController(gamepad2.right_stick_y);

        // Aperture Science High Energy Pellet Launcher
        if (gamepad1.right_trigger == 1.0) {
            cannon.fire();
        }
        if (gamepad1.back) {
            cannon.reset();
        }

        // Aperture Science Multitasking Management Rail
        linearActuator.setPower(-gamepad2.left_stick_y);

        // Aperture Science Gamepad-Enhanced Precision Pixel Grabbing Pixel Placing Pixel Claw Mover
        pixelMotion.actuateUsingDpad(gamepad2.dpad_up, gamepad2.dpad_down);

        // Aperture Science Self-Suspending Suspender Suspension System
        if (gamepad1.dpad_up) {
            hook.extend();
        } else if (gamepad1.dpad_down) {
            hook.retract();
        }

        // Ensure that the buttons are only registered once per press
        x_pressed = gamepad2.x;
        b_pressed = gamepad2.b;
        inc_pressed = gamepad1.right_bumper;
        dec_pressed = gamepad1.left_bumper;

        // Dispatch stateful changes
        drive.update();
        speed.update();
        clawRotator.update();
        pixelMotion.update();
        hook.update();
        linearActuator.update();
        claws.update();
        cannon.update();
    }
}
