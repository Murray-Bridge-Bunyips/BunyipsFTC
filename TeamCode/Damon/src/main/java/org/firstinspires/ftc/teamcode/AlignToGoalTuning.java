package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.RIGHT_BUMPER;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
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

    public static int Target_Tag_ID = 20;


    public void onInit(){

        robot.init();
        // 1. Instantiating creates the BunyipsLib camera binding wrapper
        webcam = new Vision(hardwareMap.get(WebcamName.class, "webcam")).withName("Webcam");

        AprilTag defaultAprilTag = new AprilTag();
        AprilTag aprilTag = new AprilTag(builder -> {
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

        //AlignToAprilTagTask task = new AlignToAprilTagTask(robot.drive, aprilTag, 20); // Use in tasks

        AlignToAprilTagTask task = new AlignToAprilTagTask(robot.drive, aprilTag, 20);




    }






    public void activeLoop() {




        BunyipsSubsystem.updateAll();

        //Scheduler.update(); //I'm not using the correct thing to refresh it
    }
}
