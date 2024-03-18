package org.murraybridgebunyips.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.DualServos;
import org.murraybridgebunyips.bunyipslib.InputMultiplier;
import org.murraybridgebunyips.bunyipslib.NullSafety;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreClawRotator;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreForwardServo;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreHook;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreLinearActuator;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * TeleOp for GLaDOS robot FTC 15215, command-based
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
 * @author Lucas Bubner, 2024
 */
@TeleOp(name = "TeleOp (Command Based)")
public class GLaDOSTeleOpCommand extends CommandBasedBunyipsOpMode {
    private final InputMultiplier speed = new InputMultiplier(0.25, 0.5, 1.0).withDefaultIndex(1);
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private PersonalityCoreClawRotator clawRotator;
    private PersonalityCoreForwardServo pixelMotion;
    private PersonalityCoreHook hook;
    private PersonalityCoreLinearActuator linearActuator;
    private DualServos claws;
    private Cannon cannon;

    @Override
    protected void onInitialisation() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelEncoder, config.perpendicularEncoder
        );
        if (NullSafety.assertComponentArgs(Cannon.class, config.launcher))
            cannon = new Cannon(config.launcher);
        if (NullSafety.assertComponentArgs(PersonalityCoreClawRotator.class, config.pixelAlignment))
            clawRotator = new PersonalityCoreClawRotator(config.pixelAlignment);
        if (NullSafety.assertComponentArgs(PersonalityCoreForwardServo.class, config.pixelMotion))
            pixelMotion = new PersonalityCoreForwardServo(config.pixelMotion);
        if (NullSafety.assertComponentArgs(PersonalityCoreHook.class, config.suspenderHook))
            hook = new PersonalityCoreHook(config.suspenderHook);
        if (NullSafety.assertComponentArgs(PersonalityCoreLinearActuator.class, config.suspenderActuator))
            linearActuator = new PersonalityCoreLinearActuator(config.suspenderActuator);
        if (NullSafety.assertComponentArgs(DualServos.class, config.leftPixel, config.rightPixel))
            claws = new DualServos(config.leftPixel, config.rightPixel, 0.0, 1.0, 1.0, 0.0);
    }

    @Override
    protected BunyipsSubsystem[] setSubsystems() {
        return new BunyipsSubsystem[]{drive, clawRotator, pixelMotion, hook, linearActuator, claws, cannon};
    }

    @Override
    protected void assignCommands() {
        scheduler().whenPressed(Controller.User.TWO, Controller.X)
                .run(claws.toggleServoTask(DualServos.ServoSide.LEFT));
        scheduler().whenPressed(Controller.User.TWO, Controller.B)
                .run(claws.toggleServoTask(DualServos.ServoSide.RIGHT));

        scheduler().whenPressed(Controller.User.TWO, Controller.Y)
                .run(clawRotator.faceBoardTask());
        scheduler().whenPressed(Controller.User.TWO, Controller.A)
                .run(clawRotator.faceGroundTask());

        pixelMotion.setDefaultTask(pixelMotion.dpadControlTask(() -> gamepad2.dpad_up, () -> gamepad2.dpad_down));
        linearActuator.setDefaultTask(linearActuator.joystickControlTask(() -> gamepad2.left_stick_y));
        clawRotator.setDefaultTask(clawRotator.joystickControlTask(() -> gamepad2.right_stick_y));

        scheduler().whenPressed(Controller.User.ONE, Controller.RIGHT_BUMPER)
                .run(speed.incrementTask());
        scheduler().whenPressed(Controller.User.ONE, Controller.LEFT_BUMPER)
                .run(speed.decrementTask());

        scheduler().when(() -> gamepad2.right_trigger == 1.0)
                .run(cannon.fireTask());
        scheduler().whenPressed(Controller.User.ONE, Controller.BACK)
                .run(cannon.resetTask());

        scheduler().whenPressed(Controller.User.ONE, Controller.DPAD_UP)
                .run(hook.extendTask());
        scheduler().whenPressed(Controller.User.ONE, Controller.DPAD_DOWN)
                .run(hook.retractTask());

        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false));
    }
}
