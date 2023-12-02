package org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.murraybridgebunyips.ftc.bunyipslib.RobotConfig;
import org.murraybridgebunyips.ftc.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
public abstract class StraightTest extends LinearOpMode {
    protected MecanumRoadRunnerDrive drive;

    public static double DISTANCE = 39.3701; // in

    @Override
    public void runOpMode() throws InterruptedException {
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        if (drive == null) throw new NullPointerException("drive is null!");


        Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectory(trajectory);

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (!isStopRequested() && opModeIsActive()) ;
    }
}