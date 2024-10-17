package org.murraybridgebunyips.joker.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Millimeters;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.EncoderTicks;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.localizers.MecanumLocalizer;
import org.murraybridgebunyips.bunyipslib.vision.AprilTagPoseEstimator;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.joker.Joker;

import java.util.Arrays;

import kotlin.Pair;

@TeleOp(name = "DriveTest")
public class DriveTest extends BunyipsOpMode {
    private final Joker robot = new Joker();
    private CartesianMecanumDrive drive;
    private MecanumLocalizer localizer;
    private Vision webcam;

    @Override
    protected void onInit() {
        robot.init();
        drive = new CartesianMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight);
        localizer = new MecanumLocalizer(17, (ticks) -> EncoderTicks.toInches(ticks, Inches.convertFrom(37.5, Millimeters), 0.05, 28),
                () -> Arrays.asList(robot.frontLeft.getCurrentPosition(), robot.backLeft.getCurrentPosition(), robot.backRight.getCurrentPosition(), robot.frontRight.getCurrentPosition()),
                new Pair<>(() -> robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS), null), null, 100.0 / 97.5);
        drive.setLocalizer(localizer);
        webcam = new Vision(robot.camera);
        AprilTag at = new AprilTag();
        webcam.init(at);
        webcam.start(at);
        webcam.startPreview();
        AprilTagPoseEstimator.enable(at, localizer)
                .setCameraOffset(new Pose2d(8.5, 0, 0))
                .setHeadingEstimate(false)
                .setKalmanGains(4, 0.1);
    }

    @Override
    protected void activeLoop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;
        drive.setSpeedUsingController(leftStickX, leftStickY, rightStickX);
        drive.update();

        telemetry.addData("pose", localizer.getPoseEstimate());
    }
}