package org.firstinspires.ftc.teamcode.components;


import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.Jonas;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.MirroredPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.TaskBuilder;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.ActionTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;

public class AutoComponents {
    Jonas robot;
    PoseMap currentPoseMap;
    StartingConfiguration.Position startingPosition;

    public SequentialTaskGroup launch;
    public Vector2d launchPos;
    public double launchRot;
    public ActionTask midLaunchMovement;
    public ActionTask postLaunchReturn;
    public RevBlinkinLedDriver.BlinkinPattern lightsChargeColour, lightsLaunchColour;
    public TaskBuilder toLaunchPos;

    public AutoComponents(Jonas robot, PoseMap currentPoseMap, StartingConfiguration.Position startingPosition) {
        this.robot = robot;
        this.currentPoseMap = currentPoseMap;
        this.startingPosition = startingPosition;

        if (startingPosition.isRed()) {
            robot.lights.setDefaultPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED);
            lightsChargeColour = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
            lightsLaunchColour = RevBlinkinLedDriver.BlinkinPattern.RED;
            currentPoseMap = new IdentityPoseMap();
        }
        else {
            robot.lights.setDefaultPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_BLUE);
            lightsChargeColour = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE;
            lightsLaunchColour = RevBlinkinLedDriver.BlinkinPattern.BLUE;
            currentPoseMap = new MirroredPoseMap();
        }

        launchPos = new Vector2d(-24, 12);
        launchRot = 126+180;
        //TODO: look at below and make sure it works
        toLaunchPos = robot.drive.makeTrajectory(startingPosition.toFieldPose())
                .strafeToLinearHeading(launchPos, Inches, launchRot, Degrees);
        midLaunchMovement = robot.drive.makeTrajectory(new Pose2d(launchPos, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeTo(new Vector2d(launchPos.x+(6*-0.8), launchPos.y+6), Inches)
                .build();
        postLaunchReturn = robot.drive.makeTrajectory(new Pose2d(launchPos.x-6, launchPos.y+6, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeTo(launchPos, Inches)
                .build();
        launch = new SequentialTaskGroup(
                new ParallelTaskGroup(
                        robot.intake.tasks.runFor(Seconds.of(2), 1),
                        robot.output.tasks.runFor(Seconds.of(3.4), 0.375),
                        robot.intake.tasks.runFor(Seconds.of(1), 1).after(robot.preventer.tasks.open().after(2.4, Seconds)),
                        midLaunchMovement.after(2.6, Seconds)
                ).during(robot.lights.tasks.setPatternFor(Seconds.of(2.4), lightsChargeColour).then(robot.lights.tasks.setPattern(lightsLaunchColour))),
                new ParallelTaskGroup(
                        postLaunchReturn,
                        robot.preventer.tasks.close()
                ));
    }
}
