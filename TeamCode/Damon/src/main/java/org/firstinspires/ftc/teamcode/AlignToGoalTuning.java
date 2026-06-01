package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.data.VisionData;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.AprilTag;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;

@Config
@TeleOp(name = "AlignToGoalTuning")
public class AlignToGoalTuning extends BunyipsOpMode {

    DamonV2 robot = new DamonV2();
    //Vision vision = new Vision(robot.hw.webcam);

    protected void onInit(){


        //vision.init(vision.raw);

        //vision.start();

        //Try to get it detecting april tags
        //Make Selection of april tags according to what auto was selected

        //Look in the API and see how to do it

        /*

        int AprilTagID = 21; //defult


        AprilTag defaultAprilTag = new AprilTag(); // no parameters, using all defaults
        AprilTag aprilTag = new AprilTag(builder -> {
            // extra builder parameters can optionally go in this lambda, including configuring the camera location for relocalization
            builder.setSuppressCalibrationWarnings(true); // other builder config of the AprilTagProcessor can be done too
            // utility builder for robot camera pose
            builder = AprilTag.setCameraPose(builder)
                    .forward(Centimeters.of(-3)) // define where the camera is
                    .left(Centimeters.of(2))
                    .up(Centimeters.of(7))
                    .yaw(Degrees.of(90))
                    .apply();
            return builder;
        });


        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> false);
        robot.drive.setDefaultTask(driveTask);
    */
    }






    protected void activeLoop() {


        Scheduler.update();
    }
}
