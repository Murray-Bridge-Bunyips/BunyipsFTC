package org.firstinspires.ftc.teamcode.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.MirroredPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.ActionTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Goal Side", preselectTeleOp = "TeleOp")
public class GoalSide extends AutonomousBunyipsOpMode {
    private final Jonas robot = new Jonas();
    PoseMap currentPoseMap;
    RevBlinkinLedDriver.BlinkinPattern lightsChargeColour, lightsLaunchColour;
    /**
     * W task?
     */
    SequentialTaskGroup launch;
    Vector2d launchPos;
    double launchRot;
    ActionTask midLaunchMovement;
    ActionTask postLaunchReturn;

    @Override
    protected void onInitialise() {
        robot.init();
        setOpModes(
                StartingConfiguration.redLeft().tile((23.5/24)+0.5).forward(Inches.of(72+42.5)).rotate(Degrees.of(126+90)),
                StartingConfiguration.blueRight().tile((23.5/24)+0.5).forward(Inches.of(72+42.5)).rotate(Degrees.of(90-126))
        ).assignButton(0, 0, Controls.B).assignButton(0, 1, Controls.X);

        robot.preventer.close();
        robot.preventer.update();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.get();
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

        launchPos = new Vector2d(-24*1.5, 24*0.5);
        launchRot = 126+180;
        midLaunchMovement = robot.drive.makeTrajectory(new Pose2d(launchPos, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeTo(new Vector2d(launchPos.x-1.765, launchPos.y+6), Inches)
                .build();
        postLaunchReturn = robot.drive.makeTrajectory(new Pose2d(launchPos.x-6, launchPos.y+6, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeTo(launchPos, Inches)
                .build();
        launch = new SequentialTaskGroup(
                new ParallelTaskGroup(
                        robot.output.tasks.runFor(Seconds.of(3.4), 0.9),
                        robot.intake.tasks.runFor(Seconds.of(1), 1).after(robot.preventer.tasks.open().after(2.4, Seconds)),
                        midLaunchMovement.after(2.4, Seconds)
                ).during(robot.lights.tasks.setPatternFor(Seconds.of(2.4), lightsChargeColour).then(robot.lights.tasks.setPattern(lightsLaunchColour))),
                new ParallelTaskGroup(
                        postLaunchReturn,
                        robot.preventer.tasks.close()
                )
        );

        robot.drive.setPose(startingPosition.toFieldPose());

        add(robot.drive.makeTrajectory(currentPoseMap)
                .strafeToLinearHeading(launchPos, Inches, launchRot, Degrees)
                .build()
                .during(robot.intake.tasks.run(1)));

        add(launch);

        robot.drive.makeTrajectory(new Pose2d(launchPos, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeToLinearHeading(new Vector2d(72-(36+23.75*2), 24*0.5), Inches, 90, Degrees)
                .addTask();

        add(robot.drive.makeTrajectory(new Pose2d(72-(36+23.75*2), 24*0.5, Radians.convertFrom(90, Degrees)), currentPoseMap)
                .strafeTo(new Vector2d(72-(36+23.75*2), 48-(12.75/2)), Inches)
                .strafeToLinearHeading(launchPos, Inches, launchRot, Degrees)
                .build()
                .during(robot.intake.tasks.run(1))
        );

        add(launch);

        robot.drive.makeTrajectory(new Pose2d(launchPos, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeToLinearHeading(new Vector2d(72-(36+23.75), 24*0.5), Inches, 90, Degrees)
                .addTask();

        add(robot.drive.makeTrajectory(new Pose2d(72-(36+23.75), 24*0.5, Radians.convertFrom(90, Degrees)), currentPoseMap)
                .strafeTo(new Vector2d(72-(36+23.75), 48-(12.75/2)), Inches)
                .strafeToLinearHeading(launchPos, Inches, launchRot, Degrees)
                .build()
                .during(robot.intake.tasks.run(1))
        );

        add(launch);

        robot.drive.makeTrajectory(new Pose2d(launchPos, Radians.convertFrom(launchRot, Degrees)), currentPoseMap)
                .strafeToLinearHeading(new Vector2d(72-(36), 24*0.5), Inches, 90, Degrees)
                .addTask();

        add(robot.drive.makeTrajectory(new Pose2d(72-(36), 24*0.5, Radians.convertFrom(90, Degrees)), currentPoseMap)
                .strafeTo(new Vector2d(72-(36), 48-(12.75/2)), Inches)
                .strafeToLinearHeading(launchPos, Inches, launchRot, Degrees)
                .build()
                .during(robot.intake.tasks.run(1))
        );

        add(launch);
    }
}
