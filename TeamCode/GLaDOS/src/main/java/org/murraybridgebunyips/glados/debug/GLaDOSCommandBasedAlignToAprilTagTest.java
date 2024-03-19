package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicDriveTask;
import org.murraybridgebunyips.bunyipslib.tasks.MoveToAprilTagTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Align to AprilTag.
 *
 * @author Lucas Bubner, 2024
 */
@TeleOp(name = "Align To AprilTag (Command Based)")
//@Disabled
public class GLaDOSCommandBasedAlignToAprilTagTest extends CommandBasedBunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private Vision vision;
    private AprilTag aprilTag;

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
        aprilTag = new AprilTag();
        vision.init(aprilTag);
        vision.start(aprilTag);
        vision.startPreview();
        addSubsystems(drive, vision);
    }

    @Override
    protected void assignCommands() {
        drive.setDefaultTask(new HolonomicDriveTask<>(gamepad1, drive, () -> false));
        scheduler().whenPressed(Controller.User.ONE, Controller.LEFT_BUMPER)
                .run(new MoveToAprilTagTask<>(gamepad1, drive, aprilTag, -1))
                .finishingWhen(() -> !gamepad1.left_bumper);
    }
}

