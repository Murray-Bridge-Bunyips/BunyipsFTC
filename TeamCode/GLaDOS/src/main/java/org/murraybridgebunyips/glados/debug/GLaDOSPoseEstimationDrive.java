package org.murraybridgebunyips.glados.debug;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.AprilTagPoseEstimator;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Pose estimation drive for GLaDOS robot FTC 15215, debug
 */
@TeleOp
public class GLaDOSPoseEstimationDrive extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    private Vision vision;
    private AprilTag aprilTag;

    private DualDeadwheelMecanumDrive drive;

    private AprilTagPoseEstimator poseEstimator;

    @Override
    protected void onInit() {
        config.init();
        vision = new Vision(config.webcam);
        drive = new DualDeadwheelMecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelDeadwheel, config.perpendicularDeadwheel
        );

        aprilTag = new AprilTag();
        vision.init(aprilTag);
        vision.start(aprilTag);
        drive.setPoseEstimate(new Pose2d(StartingPositions.STARTING_RED_RIGHT.getVector(), Math.PI / 2));

        poseEstimator = new AprilTagPoseEstimator(aprilTag, drive);
    }

    @Override
    protected void activeLoop() {
        drive.setSpeedUsingController(gamepad1.lsx, gamepad1.lsy, gamepad1.rsx);
        drive.update();

        vision.update();
        poseEstimator.update();
    }
}
