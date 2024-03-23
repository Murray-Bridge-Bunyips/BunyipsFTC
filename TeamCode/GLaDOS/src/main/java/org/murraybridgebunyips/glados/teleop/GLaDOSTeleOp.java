package org.murraybridgebunyips.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.DualServos;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreClawRotator;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * TeleOp for GLaDOS robot FTC 15215
 * gamepad1:
 * left_stick_x: strafe
 * left_stick_y: forward/backward
 * right_stick_x: turn
 * right_trigger: fire cannon
 * b: reset cannon
 * gamepad2:
 * x: toggle left claw
 * b: toggle right claw
 * y: align claw to board
 * a: align claw to ground
 * right_stick_y: move claw mover
 *
 * @author Lucas Bubner, 2024
 * @author Lachlan Paul, 2024
 */
@TeleOp(name = "TeleOp")
public class GLaDOSTeleOp extends CommandBasedBunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private PersonalityCoreClawRotator clawRotator;
    private MecanumDrive drive;
    private DualServos claws;
    private Cannon cannon;
    @Override
    protected void onInitialisation() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelDeadwheel, config.perpendicularDeadwheel
        );
        cannon = new Cannon(config.launcher);
        claws = new DualServos(config.leftPixel, config.rightPixel, 0.0, 1.0, 1.0, 0.0);
        clawRotator = new PersonalityCoreClawRotator(config.pixelAlignment);
        addSubsystems(drive, cannon, claws);
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

        scheduler().when(() -> gamepad1.right_trigger == 1.0)
                .run(cannon.fireTask());
        scheduler().whenPressed(Controller.User.ONE, Controller.B)
                .run(cannon.resetTask());

        clawRotator.setDefaultTask(clawRotator.joystickControlTask(() -> gamepad2.right_stick_y));
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false));
    }
}
