package org.firstinspires.ftc.teamcode.common.roadrunner.drive.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumRoadRunnerDrive;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.localizers.TwoWheelTrackingLocalizer;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(name = "SplineTest", group = "tuning")
@Disabled
public class SplineTest extends LinearOpMode {
    // Temporarily match this config to your robot's config
    private static final GLaDOSConfigCore ROBOT_CONFIG = new GLaDOSConfigCore();

    @Override
    public void runOpMode() throws InterruptedException {
        ROBOT_CONFIG.init(this);
        MecanumRoadRunnerDrive drive = new MecanumRoadRunnerDrive(ROBOT_CONFIG.driveConstants, ROBOT_CONFIG.mecanumCoefficients, hardwareMap.voltageSensor, ROBOT_CONFIG.imu, ROBOT_CONFIG.frontLeft, ROBOT_CONFIG.frontRight, ROBOT_CONFIG.backLeft, ROBOT_CONFIG.backRight);
        drive.setLocalizer(new TwoWheelTrackingLocalizer(ROBOT_CONFIG.localizerCoefficients, ROBOT_CONFIG.parallelEncoder, ROBOT_CONFIG.perpendicularEncoder, drive));


        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(30, 30), 0)
                .build();

        drive.followTrajectory(traj);

        sleep(2000);

        drive.followTrajectory(
                drive.trajectoryBuilder(traj.end(), true)
                        .splineTo(new Vector2d(0, 0), Math.toRadians(180))
                        .build()
        );
    }
}