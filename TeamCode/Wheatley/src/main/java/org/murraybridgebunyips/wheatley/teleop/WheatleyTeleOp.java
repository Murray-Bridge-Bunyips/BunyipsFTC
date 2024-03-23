package org.murraybridgebunyips.wheatley.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.DualServos;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreLinearActuator;
import org.murraybridgebunyips.wheatley.components.WheatleyClawRotator;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * Primary TeleOp for all of Wheatley's functions.
 *
 * @author Lachlan Paul, 2024
 * @author Lucas Bubner, 2024
 */

@TeleOp(name = "TeleOp")
public class WheatleyTeleOp extends CommandBasedBunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;
    private Cannon cannon;
    private PersonalityCoreLinearActuator linearActuator;
    private WheatleyClawRotator clawRotator;
    private DualServos claws;

    @Override
    protected void onInitialisation() {
        config.init();
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br
        );
        cannon = new Cannon(config.launcher);
        linearActuator = new PersonalityCoreLinearActuator(config.linearActuator);
        clawRotator = new WheatleyClawRotator(config.clawRotator);
        claws = new DualServos(config.leftPixel, config.rightPixel, 0.0, 1.0, 1.0, 0.0);
        addSubsystems(drive, cannon, linearActuator, clawRotator, claws);
    }

    @Override
    protected void assignCommands() {
        scheduler().when(() -> gamepad2.right_trigger == 1.0)
                .run(cannon.fireTask());
        scheduler().whenPressed(Controller.User.TWO, Controller.BACK)
                .run(cannon.resetTask());

        scheduler().whenPressed(Controller.User.TWO, Controller.X)
                .run(claws.toggleServoTask(DualServos.ServoSide.LEFT));
        scheduler().whenPressed(Controller.User.TWO, Controller.B)
                .run(claws.toggleServoTask(DualServos.ServoSide.RIGHT));

        scheduler().whenPressed(Controller.User.TWO, Controller.DPAD_UP)
                .run(clawRotator.setDegreesTask(30));
        scheduler().whenPressed(Controller.User.TWO, Controller.DPAD_DOWN)
                .run(clawRotator.homeTask());

        scheduler().whenPressed(Controller.User.TWO, Controller.A)
                .run(linearActuator.homeTask());

        linearActuator.setDefaultTask(linearActuator.joystickControlTask(() -> gamepad2.left_stick_y));
        clawRotator.setDefaultTask(clawRotator.setPowerUsingControllerTask(() -> gamepad2.right_stick_y));
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false).withSquaredInputs());
    }
}