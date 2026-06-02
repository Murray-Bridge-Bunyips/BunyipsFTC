package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToAprilTagTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.data.VisionData;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.AprilTag;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.decode.GreenArtifact;


@Config
@TeleOp(name = "AlignToGoalTuning")
public class AlignToGoalTuning extends BunyipsOpMode {

    //DO NOT COMMIT THE XML FILE
    //DO NOT COMMIT THE XML FILE
    //DO NOT COMMIT THE XML FILE
    DamonV2 robot = new DamonV2();
    private Vision webcam;


    protected void onInit(){

        robot.init();
        // 1. Instantiating creates the BunyipsLib camera binding wrapper
        webcam = new Vision(hardwareMap.get(WebcamName.class, "webcam")).withName("Webcam");

        AprilTag defaultAprilTag = new AprilTag(); // no parameters, using all defaults
        AprilTag aprilTag = new AprilTag(builder -> {
            // extra builder parameters can optionally go in this lambda, including configuring the camera location for relocalization
            builder.setSuppressCalibrationWarnings(true);

            // other builder config of the AprilTagProcessor can be done too
            // utility builder for robot camera pose
            builder = AprilTag.setCameraPose(builder)
                    .backward(Centimeters.of(20)) // define where the camera is
                    .left(Centimeters.of(20))
                    .up(Centimeters.of(7))
                    .yaw(Degrees.of(90))
                    .apply();
            return builder;
        });

        webcam.init(defaultAprilTag);
        webcam.start(defaultAprilTag);

        AlignToAprilTagTask task = new AlignToAprilTagTask(robot.drive, defaultAprilTag, 20); // Use in tasks


    }






    protected void activeLoop() {


        //Do a tasks.run in here?

        BunyipsSubsystem.updateAll();

        //Scheduler.update(); //I'm not using the correct thing to refresh it
    }
}
