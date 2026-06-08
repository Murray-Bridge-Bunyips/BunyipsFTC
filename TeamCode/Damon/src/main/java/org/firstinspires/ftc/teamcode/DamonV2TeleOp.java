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

@Config
@TeleOp(name = "TeleOp")
public class DamonV2TeleOp extends BunyipsOpMode {
    public static PIDFCoefficients ALIGN_TO_APRILTAG_PIDF_COEFFICIENTS = new PIDFCoefficients(0.03, 0, 0, 0);

    public static PIDFCoefficients ALIGN_TO_POINT_PIDF_COEFFICIENTS = new PIDFCoefficients(1, 0, 0, 0);
    private final DamonV2 robot = new DamonV2();
    private PIDFController shooterPid;

    private Vision webcam;
    public static int Target_Tag_ID = 20;

    public AprilTag aprilTag;

    public Rotation2d fcOffset;

    public boolean sensorValue;

    public boolean maxBalls = false;








    @Override
    protected void onInit() {




        robot.init();
        //shooterPid = robot.hw.shooter.getRunUsingEncoderController().pidf().get();
        //Check if startConfig in null
        //Check if its red or blue
        //Asign the goal






        StartingConfiguration.Position startConfig = Storage.memory().lastKnownStartingConfiguration;
        Vector2d goal;
        sensorValue = robot.touchSensor.isPressed();
        int e = 1;



        if(startConfig == null){
            Target_Tag_ID = 20;
            goal = new Vector2d(-67, 62);//Blue Goal
        }
        else {
            if(startConfig.isRed()){ //if blue because the starting configs in the autos
                Target_Tag_ID = 20;
                goal = new Vector2d(-67, -62);
            }
            else{ //if red
                Target_Tag_ID = 24;
                goal = new Vector2d(-67, 62);
            }
        }

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> false);
        new HolonomicDriveTask(gamepad1, robot.drive).setAsDefaultTask();

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

        AlignToAprilTagTask.R_TOLERANCE = -20;
        AlignToAprilTagTask.DEFAULT_CONTROLLER.setPIDF(ALIGN_TO_APRILTAG_PIDF_COEFFICIENTS);

        gamepad1.button(LEFT_BUMPER)
                .whileTrue(new AlignToAprilTagTask(gamepad1, robot.drive, aprilTag, Target_Tag_ID).withBearingTarget(Degrees.of(10)));

        gamepad1.axisGreaterThan(LEFT_TRIGGER, 0.9)
                .whileTrue(new AlignToPointDriveTask(() -> goal, gamepad1, robot.drive).withAlignmentOffset(Degrees.of(180))); //To be changed

        gamepad1.button(X).or(gamepad2.button(X)).and(() -> !maxBalls)
                .whileTrue(robot.intake.tasks.run(1))
                .whileTrue(robot.transferLeft.tasks.run(1))
                .whileTrue(robot.transferRight.tasks.run(1));

        gamepad1.button(Y).or(gamepad2.axisGreaterThan(LEFT_TRIGGER, 0.9))
                .whileTrue(robot.intake.tasks.run(1));


        gamepad1.button(A).or(gamepad2.button(DPAD_UP))
                .onTrue(robot.gate.tasks.open())
                .onTrue(robot.gate.tasks.close().after(Seconds.of(5)))
                .onTrue(robot.kicker.tasks.toggleBoth());

        gamepad1.button(RIGHT_BUMPER).or(gamepad2.button(RIGHT_BUMPER))
                .whileTrue(robot.shooter.tasks.run(0.9))
                .onTrue(robot.hoodAdjustment.tasks.close());

        gamepad2.button(LEFT_BUMPER)
                .whileTrue(robot.shooter.tasks.run(0.65))
                .onTrue(robot.hoodAdjustment.tasks.open());//Add adjustment to hood
    }


    @Override
    protected void activeLoop() {
        //telemetry.addData("currentVelocity", shooterPid.getCurrentProcess());
        //telemetry.addData("targetVelocity", shooterPid.getSetpoint());
        //AlignToPointDriveTask.DEFAULT_CONTROLLER.setPIDF(ALIGN_TO_POINT_PIDF_COEFFICIENTS);
        //AlignToAprilTagTask.DEFAULT_CONTROLLER.setPIDF(ALIGN_TO_APRILTAG_PIDF_COEFFICIENTS);
        //AlignToAprilTagTask task = new AlignToAprilTagTask(robot.drive, aprilTag, 20);
        AlignToPointDriveTask.DEFAULT_CONTROLLER.setPIDF(ALIGN_TO_POINT_PIDF_COEFFICIENTS);
        telemetry.addData("isTouched", robot.touchSensor.getValue());
        //telemetry.addData("maxBalls", maxBalls);

        if (!robot.touchSensor.isPressed()) {
            robot.intake.setPower(-0.2);
            robot.transferRight.setPower(-0.25);
            robot.transferLeft.setPower(-0.25);
            maxBalls = true;
            telemetry.addData("maxBalls", maxBalls);
            telemetry.addData("test", "hi");
        }
        else {
            robot.intake.setPower(0);
            robot.transferRight.setPower(0);
            robot.transferLeft.setPower(0);
        }

        if(robot.kicker.isOpen(DualServos.ServoSide.RIGHT)){
            maxBalls = false;

        }

        Scheduler.update();
    }

}