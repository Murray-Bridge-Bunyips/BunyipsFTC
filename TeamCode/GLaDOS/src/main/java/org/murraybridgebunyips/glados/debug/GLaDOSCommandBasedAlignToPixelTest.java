package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.pid.PIDController;
import org.murraybridgebunyips.bunyipslib.tasks.AlignToPixelTask;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.bunyipslib.tasks.InstantTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiYCbCrThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.GreenPixel;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.Pixels;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.WhitePixel;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * POV drivetrain only for GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "Align To Pixel (Command Based)")
//@Disabled
public class GLaDOSCommandBasedAlignToPixelTest extends CommandBasedBunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private Vision vision;
    private MultiYCbCrThreshold pixels;

    @Override
    protected void onInitialisation() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelEncoder, config.perpendicularEncoder
        );
        vision = new Vision(config.webcam);
        pixels = new MultiYCbCrThreshold(Pixels.createProcessors());
        vision.init(pixels, vision.raw);
        vision.start(pixels, vision.raw);
        vision.startDashboardSender();
    }

    @Override
    protected BunyipsSubsystem[] setSubsystems() {
        return new BunyipsSubsystem[] {
                drive,
        };
    }

    @Override
    protected void assignCommands() {
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false));
        scheduler().whenHeld(Controller.User.ONE, Controller.Y)
                .run(new InstantTask(() -> drive.resetYaw()))
                .immediately();
        scheduler().whenPressed(Controller.User.ONE, Controller.RIGHT_BUMPER)
                .run(new AlignToPixelTask<>(gamepad1, drive, pixels, new PIDController(1, 0.25, 0.0)))
                .finishingWhen(() -> !gamepad1.right_bumper);
    }
}

