package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

import android.widget.ToggleButton;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.DualServos;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToAprilTagTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.data.AprilTagData;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.AprilTag;
import org.opencv.core.Point;

import java.util.Vector;

@Config
@TeleOp(name = "TeleOp")
public class SpecterTeleOp extends BunyipsOpMode {
    private final Specter robot = new Specter();

    private Vision webcam;

    public AprilTag aprilTag;

    public static PIDFCoefficients ALIGN_TO_POINT_PIDF_COEFFICIENTS = new PIDFCoefficients(2, 0, 0, 0);

    public Vector2d goal;

    public Pose2d resetPose;


    @Override
    protected void onInit() {
        robot.init();


        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> false);
        new HolonomicDriveTask(gamepad1, robot.drive).setAsDefaultTask();

        StartingConfiguration.Position startConfig = Storage.memory().lastKnownStartingConfiguration;
        Vector2d goal1, goal2;
        if(startConfig == null){
            goal1 = new Vector2d(21.5, -12); //Red goal
            goal2 = new Vector2d(-21.5, -12);
            resetPose = new Pose2d(-60, -11, 180);
        }
        else {
            if(startConfig.isBlue()){
                goal1 = new Vector2d(-21.5, 12);
                goal2 = new Vector2d(21.5, 12);
                resetPose = new Pose2d(60, 11, 0);
            }
            else{ //if red
                goal1 = new Vector2d(21.5, -12);
                goal2 = new Vector2d(-21.5, -12);
                resetPose = new Pose2d(-60, -11, 180);
            }
        }



        gamepad1.button(RIGHT_BUMPER)
                .whileTrue(new AlignToPointDriveTask(() -> goal1, gamepad1, robot.drive).withAlignmentOffset(Degrees.of(180)));

        gamepad1.button(LEFT_BUMPER)
                .whileTrue(new AlignToPointDriveTask(() -> goal2, gamepad1, robot.drive).withAlignmentOffset(Degrees.of(180)));

        gamepad1.axisGreaterThan(LEFT_TRIGGER, 0.90)
                .onTrue(() -> robot.drive.setPose(resetPose));






        /*
        webcam = new Vision(hardwareMap.get(WebcamName.class, "webcam")).withName("Webcam");

        aprilTag = new AprilTag(builder -> {
            // extra builder parameters can optionally go in this lambda, including configuring the camera location for relocalization
            builder.setSuppressCalibrationWarnings(true);
            // other builder config of the AprilTagProcessor can be done too
            // utility builder for robot camera pose
            builder = AprilTag.setCameraPose(builder)
                    .backward(Centimeters.of(21)) // define where the camera is
                    .right(Centimeters.of(16))
                    .up(Centimeters.of(26))
                    .yaw(Degrees.of(180))
                    .apply();
            return builder;
        });

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.startCameraStream(aprilTag, 0);

        webcam.init(aprilTag);
        webcam.start(aprilTag);
         */



        gamepad1.button(Y)
                .whileTrue(robot.transfer.tasks.run(1));

        gamepad1.button(X)
                .whileTrue(robot.intake.tasks.run(0.8));

        gamepad1.button(B)
                .whileTrue(robot.leftShooter.tasks.run(0.75))
                .whileTrue(robot.rightShooter.tasks.run(0.75));

        gamepad1.button(DPAD_DOWN)
                .onTrue(robot.gate.tasks.toggle());


        gamepad1.button(DPAD_UP)
                .onTrue(robot.brake.tasks.toggle());

        gamepad1.button(DPAD_RIGHT)
                .onTrue(robot.sweeper.tasks.toggle());

        //Shooting speed up
        gamepad2.button(B)
                .whileTrue(robot.leftShooter.tasks.run(0.75))
                .whileTrue(robot.rightShooter.tasks.run(0.75));

        gamepad2.button(A)
                .onTrue(robot.gate.tasks.toggle())
                .onFalse(robot.gate.tasks.toggle())
                .whileTrue(robot.intake.tasks.run(1))
                .whileTrue(robot.transfer.tasks.run(1))
                .whileTrue(robot.leftShooter.tasks.run(0.75))
                .whileTrue(robot.rightShooter.tasks.run(0.75));

        gamepad2.button(X)
                .whileTrue(robot.intake.tasks.run(1));

        gamepad2.button(Y)
                .whileTrue(robot.intake.tasks.run(1));

        gamepad2.button(LEFT_BUMPER)
                .onTrue(robot.gate.tasks.toggle());

        gamepad2.button(RIGHT_BUMPER)
                .onTrue(robot.brake.tasks.toggle());


        gamepad2.button(DPAD_DOWN)
                .whileTrue(robot.intake.tasks.run(-1));








    }


    @Override
    protected void activeLoop() {


        Scheduler.update();
    }

}

