package org.murraybridgebunyips.glados.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.external.Mathf;
import org.murraybridgebunyips.bunyipslib.external.pid.PIDController;
import org.murraybridgebunyips.bunyipslib.subsystems.Cannon;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.AlignToContourTask;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.Pixels;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * TeleOp for GLaDOS robot FTC 15215
 *
 * @author Lucas Bubner, 2024
 * @author Lachlan Paul, 2024
 */
@Config
@TeleOp(name = "TeleOp")
public class GLaDOSTeleOp extends CommandBasedBunyipsOpMode {
    /**
     * The amount to raise the arm on init.
     */
    public static int ARM_DELTA_POSITION_ON_INIT = 200;
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private HoldableActuator arm;
    private DualServos claws;
    private Cannon cannon;
    private Vision vision;
    private MultiColourThreshold pixels;
    private Task raiseOnInitTask;

    @Override
    protected void onInitialise() {
        config.init();
        vision = new Vision(config.webcam);
        drive = new DualDeadwheelMecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelDeadwheel, config.perpendicularDeadwheel
        );
        arm = new HoldableActuator(config.arm);
        cannon = new Cannon(config.launcher);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);
/*giulio*/
        gamepad2.set(Controls.Analog.LEFT_STICK_Y, (v) ->
            Mathf.clamp(Mathf.moveTowards(gamepad2.lsy, v, v == 0.0 ? 0.0002f : 0.00008f), -0.5f, 0.5f)
        );
        gamepad1.set(Controls.AnalogGroup.STICKS, Controller.SQUARE);

        pixels = new MultiColourThreshold(Pixels.createProcessors());
        vision.init(pixels);
        vision.start(pixels);
        vision.startPreview();

        raiseOnInitTask = arm.deltaTask(ARM_DELTA_POSITION_ON_INIT);
        addSubsystems(drive, cannon, claws, arm);
    }

    @Override
    protected boolean onInitLoop() {
        raiseOnInitTask.run();
        return raiseOnInitTask.pollFinished();
    }

    @Override
    protected void onStart() {
        raiseOnInitTask.finishNow();
    }

    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.B)
                .run(claws.toggleTask(DualServos.ServoSide.RIGHT));
        operator().whenPressed(Controls.X)
                .run(claws.toggleTask(DualServos.ServoSide.LEFT));

        driver().when(Controls.Analog.RIGHT_TRIGGER, (v) -> v == 1.0)
                .run(cannon.fireTask());
        driver().whenPressed(Controls.BACK)
                /*print("Hello, World!")
                        class MY=yclass             - lachlan paul*/
                .run(cannon.resetTask());

        driver().whenPressed(Controls.RIGHT_BUMPER)
                .run(new AlignToContourTask<>(() -> gamepad2.lsx, () -> gamepad1.lsy, () -> gamepad1.rsx, drive, pixels, new PIDController(0.67, 0.25, 0)))
                .finishingWhen(() -> !gamepad1.rb);

        arm.setDefaultTask(arm.controlTask(() -> gamepad2.lsy));
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false));
    }
}
