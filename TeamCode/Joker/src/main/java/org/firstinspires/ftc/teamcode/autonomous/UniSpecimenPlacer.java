package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.teleop.TeleOpCommandBASED.startingPos;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.SymmetricPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import org.firstinspires.ftc.teamcode.Joker;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Uni Specimen Placer", preselectTeleOp = "TeleOp")
public class UniSpecimenPlacer extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
    PoseMap currentPoseMap;

    @Override
    protected void onInitialise() {
        robot.init();
        setOpModes(
                StartingConfiguration.redRight().tile(2.5).backward(Inches.of(4)),
                StartingConfiguration.blueRight().tile(2.5).backward(Inches.of(4))
        ).assignButton(0, 0, Controls.B).assignButton(0, 1, Controls.X);
        telemetry.addData("lift current position", robot.hw.liftMotor.getCurrentPosition());
        telemetry.addData("lift target position", robot.hw.liftMotor.getTargetPosition());
        telemetry.addData("lift power", robot.hw.liftMotor.getPower());
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.get();
        startingPos = startingPosition;
        if (startingPosition.isBlue()) {robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);} else {robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);}
        currentPoseMap = startingPosition.isRed() ? new SymmetricPoseMap() : new IdentityPoseMap();

        robot.drive.setPose(startingPosition.toFieldPose());
        add(robot.outtakeGrip.tasks.open());

        double grabX;
        double grabY;
        double hangY;
        double alignX;
        if (startingPosition.isRed()) {
            grabX = -24*2.6;
            grabY = 24*2.5;
            hangY = 24+9.5;
            alignX = -24*2.4;
        }
        else {
            grabX = -24*2.4;
            grabY = 24*2.525;
            hangY = 24+9.3;
            alignX = -24*2.3;
        }

        robot.drive.makeTrajectory(currentPoseMap)
                .strafeTo(new Vector2d(-24*1.8, 24*1.4), Inches)
                .strafeTo(new Vector2d(-24*1.8, 8), Inches)
                .strafeToLinearHeading(new Vector2d(alignX, 8), Inches, 90, Degrees)
                .strafeTo(new Vector2d(alignX, 24*2.2+1), Inches)
                .strafeTo(new Vector2d(alignX, 24*2), Inches)
                .waitFor(3, Seconds)
                .addTask();
// a man that is here his name was giulio
        add(robot.drive.makeTrajectory(new Pose2d(-24*2.4, 24*2, Math.toRadians(90)), currentPoseMap)
                .strafeTo(new Vector2d(grabX, grabY), Inches)
                .build()
                // moving lift up to correct height to grab specimen
                .with(robot.lift.tasks.goTo(270).timeout(Seconds.of(0.3)))
        );

        add(robot.outtakeGrip.tasks.close());
        wait(0.1, Seconds);

        // moving lift up above so specimen is off the wall
        add(robot.lift.tasks.goTo(700).timeout(Seconds.of(0.35)));

        add(robot.drive.makeTrajectory(new Pose2d(-24*2.6, 24*2.47, Math.toRadians(90)), currentPoseMap)
                .strafeTo(new Vector2d(-24*2, 24*1.835), Inches)
                .strafeToLinearHeading(new Vector2d(0, hangY), Inches, 270, Degrees)
                .build()
                // moving lift up ready to hang specimen
                .with(robot.lift.tasks.goTo(2400).timeout(Seconds.of(1.2))));

        // moving lift down to hang specimen
        add(robot.lift.tasks.goTo(1730).timeout(Seconds.of(0.6)));
        wait(0.05, Seconds);

        add(robot.outtakeGrip.tasks.open());

        add(robot.drive.makeTrajectory(new Pose2d(0, hangY, Math.toRadians(270)), currentPoseMap)
                .strafeTo(new Vector2d(-24*2.75, 24*2.25), Inches) // mods ban this guy
                .build()
                .with(robot.lift.tasks.home()));
    }
}