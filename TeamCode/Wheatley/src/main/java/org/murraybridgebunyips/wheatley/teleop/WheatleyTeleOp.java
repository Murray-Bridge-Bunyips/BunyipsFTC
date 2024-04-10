package org.murraybridgebunyips.wheatley.teleop;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.subsystems.Cannon;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.external.pid.PIDController;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.AlignToContourTask;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.bunyipslib.tasks.RunTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.Pixels;
import org.murraybridgebunyips.bunyipslib.subsystems.Rotator;
import org.murraybridgebunyips.common.centerstage.tasks.PickUpPixelTask;
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
    private HoldableActuator linearActuator;
    private Rotator rotator;
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
        linearActuator = new HoldableActuator(config.linearActuator);
        rotator = new Rotator(config.clawRotator, 288, 0, 90, -0.2, 0.33);
        claws = new DualServos(config.leftPixel, config.rightPixel, 0.0, 1.0, 1.0, 0.0);

        vision = new Vision(config.webcam);
        pixels = new MultiColourThreshold(Pixels.createProcessors());
        vision.init(pixels);
        vision.start(pixels);
//        vision.startPreview();

        addSubsystems(drive, cannon, linearActuator, rotator, claws, vision);
    }

    @Override
    protected void assignCommands() {
        operator().when(Controls.Analog.RIGHT_TRIGGER, (v) -> v == 1.0)
                .run(cannon.fireTask());
        operator().whenPressed(Controls.BACK)
                .run(cannon.resetTask());

        operator().whenPressed(Controls.X)
                .run(claws.toggleTask(DualServos.ServoSide.LEFT));
        operator().whenPressed(Controls.B)
                .run(claws.toggleTask(DualServos.ServoSide.RIGHT));

        operator().whenPressed(Controls.DPAD_UP)
                .run(rotator.gotoTimeTask(Degrees.of(60), Seconds.of(2)));
        operator().whenPressed(Controls.DPAD_DOWN)
                .run(rotator.runForTask(Seconds.of(1), -0.33, true));

        driver().whenPressed(Controls.RIGHT_BUMPER)
                .run(new AlignToContourTask<>(gamepad1, drive, pixels, new PIDController(0.67, 0.25, 0.0)))
                .finishingWhen(() -> !gamepad1.right_bumper);

        operator().whenPressed(Controls.A)
                .run(new RunTask());

        operator().whenPressed(Controls.RIGHT_STICK_BUTTON)
                .run(new PickUpPixelTask(linearActuator, claws));

        linearActuator.setDefaultTask(linearActuator.controlTask(() -> -gamepad2.lsy));
        rotator.setDefaultTask(rotator.controlTask(() -> -gamepad2.rsy));
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false));
    }

    @Override
    protected void periodic() {
        // Some drivers have noted that they sometimes cannot tell whether a claw is open or closed.
        // Hopefully this helps. Update: It did :)
        addTelemetry("\n---------");

        // The actual string is set to the opposite of what you might expect, by driver request.
        addTelemetry("Left Claw: " + (claws.isOpen(DualServos.ServoSide.LEFT) ? "Closed" : "Open"));
        addTelemetry("Right Claw: " + (claws.isOpen(DualServos.ServoSide.RIGHT) ? "Closed" : "Open"));
        addTelemetry("---------\n");
    }
}