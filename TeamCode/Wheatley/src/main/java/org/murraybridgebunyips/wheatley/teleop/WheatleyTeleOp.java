package org.murraybridgebunyips.wheatley.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.Cannon;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.DualServos;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.pid.PIDController;
import org.murraybridgebunyips.bunyipslib.tasks.AlignToContourTask;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.Pixels;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreLinearActuator;
import org.murraybridgebunyips.wheatley.components.WheatleyClawRotator;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * Primary TeleOp for all of Wheatley's functions.
 * <p></p>
 * gamepad1:<br>
 * left_stick_x: strafe<br>
 * left_stick_y: forward/backward<br>
 * right_stick_x: turn<br>
 * right_trigger: fire cannon<br>
 * options: reset cannon<br>
 * <p></p>
 * gamepad2:<br>
 * x: toggle left claw<br>
 * b: toggle right claw<br>
 * left_stick_y: actuate the management rail<br>
 * right_stick_y: move claw mover<br>
 *
 * 2dew: update
 * dpad_up: extend hook one position<br>
 * dpad_down: retract hook one position<br>
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
    private Vision vision;
    private MultiColourThreshold pixels;

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

        vision = new Vision(config.webcam);
        pixels = new MultiColourThreshold(Pixels.createProcessors());
        vision.init(pixels);
        vision.start(pixels);
//        vision.startPreview();

        addSubsystems(drive, cannon, linearActuator, clawRotator, claws, vision);
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
                .run(clawRotator.setDegreesTask(60));
        scheduler().whenPressed(Controller.User.TWO, Controller.DPAD_DOWN)
                .run(clawRotator.homeTask());

        scheduler().whenPressed(Controller.User.ONE, Controller.RIGHT_BUMPER)
                .run(new AlignToContourTask<>(gamepad1, drive, pixels, new PIDController(0.67, 0.25, 0.0)))
                .finishingWhen(() -> !gamepad1.right_bumper);

        scheduler().whenPressed(Controller.User.TWO, Controller.A)
                .run(linearActuator.homeTask());

        linearActuator.setDefaultTask(linearActuator.joystickControlTask(() -> gamepad2.left_stick_y));
        clawRotator.setDefaultTask(clawRotator.setPowerUsingControllerTask(() -> gamepad2.right_stick_y));
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false).withSquaredInputs());
    }

    @Override
    protected void periodic() {
        // Some drivers have noted that they sometimes cannot tell whether a claw is open or closed.
        // Hopefully this helps.
        addTelemetry("\n---------");

        // The actual string is set to the opposite of what you might expect, by driver request.
        addTelemetry("Left Claw: " + (claws.isOpen(DualServos.ServoSide.LEFT) ? "Closed" : "Open"));
        addTelemetry("Right Claw: " + (claws.isOpen(DualServos.ServoSide.RIGHT) ? "Closed" : "Open"));
        addTelemetry("---------\n");
    }
}