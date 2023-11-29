package org.firstinspires.ftc.teamcode.common.roadrunner.drive.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumCoefficients;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumRoadRunnerDrive;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.localizers.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.common.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

/*
 * Op mode for preliminary tuning of the follower PID coefficients (located in the drive base
 * classes). The robot drives in a DISTANCE-by-DISTANCE square indefinitely. Utilization of the
 * dashboard is recommended for this tuning routine. To access the dashboard, connect your computer
 * to the RC's WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're
 * using the RC phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once
 * you've successfully connected, start the program, and your robot will begin driving in a square.
 * You should observe the target position (green) and your pose estimate (blue) and adjust your
 * follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleMecanumDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * If you are using SampleTankDrive, you should be tuning AXIAL_PID, CROSS_TRACK_PID, and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 */
@Config
@Autonomous(name = "FollowerPIDTuner", group = "tuning")
@Disabled
public class FollowerPIDTuner extends LinearOpMode {
    // Temporarily match this config to your robot's config
    private static final GLaDOSConfigCore ROBOT_CONFIG = new GLaDOSConfigCore();
    public static double DISTANCE = 48; // in

    public static double TRANSLATION_kP = 0.0;
    public static double TRANSLATION_kI = 0.0;
    public static double TRANSLATION_kD = 0.0;
    public static double HEADING_kP = 0.0;
    public static double HEADING_kI = 0.0;
    public static double HEADING_kD = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        ROBOT_CONFIG.init(this);
        MecanumRoadRunnerDrive drive = new MecanumRoadRunnerDrive(ROBOT_CONFIG.driveConstants, ROBOT_CONFIG.mecanumCoefficients, hardwareMap.voltageSensor, ROBOT_CONFIG.imu, ROBOT_CONFIG.frontLeft, ROBOT_CONFIG.frontRight, ROBOT_CONFIG.backLeft, ROBOT_CONFIG.backRight);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(ROBOT_CONFIG.localizerCoefficients, ROBOT_CONFIG.parallelEncoder, ROBOT_CONFIG.perpendicularEncoder, drive));


        Pose2d startPose = new Pose2d(-DISTANCE / 2, -DISTANCE / 2, 0);

        drive.setPoseEstimate(startPose);

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
            drive.setCoefficients(
                    new MecanumCoefficients.Builder()
                            .setTranslationalPID(new PIDCoefficients(TRANSLATION_kP, TRANSLATION_kI, TRANSLATION_kD))
                            .setHeadingPID(new PIDCoefficients(HEADING_kP, HEADING_kI, HEADING_kD))
                            .build()
            );
            TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)
                    .forward(DISTANCE)
                    .turn(Math.toRadians(90))
                    .forward(DISTANCE)
                    .turn(Math.toRadians(90))
                    .forward(DISTANCE)
                    .turn(Math.toRadians(90))
                    .forward(DISTANCE)
                    .turn(Math.toRadians(90))
                    .build();
            drive.followTrajectorySequence(trajSeq);
        }
    }
}