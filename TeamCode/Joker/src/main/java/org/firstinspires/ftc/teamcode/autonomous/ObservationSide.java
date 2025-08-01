package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.teleop.TeleOpCommandBASED.startingPos;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Joker;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.SymmetricPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Observation Side", preselectTeleOp = "TeleOp")
public class ObservationSide extends AutonomousBunyipsOpMode {
    private final Joker robot = new Joker();
    PoseMap currentPoseMap;

    @Override
    protected void onInitialise() {
        robot.init();
        setOpModes(
                StartingConfiguration.redRight().tile(2.5).backward(Inches.of(4)),
                StartingConfiguration.blueRight().tile(2.5).backward(Inches.of(4))
        ).assignButton(0, 0, Controls.B).assignButton(0, 1, Controls.X);
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.get();
        startingPos = startingPosition;
        currentPoseMap = startingPosition.isRed() ? new SymmetricPoseMap() : new IdentityPoseMap();

        robot.drive.setPose(startingPosition.toFieldPose());

        add(robot.drive.makeTrajectory(currentPoseMap)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*1.8, 24*1.5), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*1.8, 8), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*2.4, 8), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*2.4, 24*2.2+1), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*2.4, 8), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*3.1, 8), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*3.1, 24*2.2+1), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*3.1, 8), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*3.7, 8), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*3.7, 24*2.2+1), Inches)
                // VROOOOOOM
                .strafeTo(new Vector2d(-24*3.7+6, 24*2.2-6), Inches)
                // VROOOOOOM
                .strafeToLinearHeading(new Vector2d(-24*2.5, 56), Inches, 90, Degrees)
                // VROOOOOOM
                //   COPPER LEFT FOR TOO LONG AND HAS BEEN MEGA VANDALISED !!!!!!!!!!!!!!!!!!!!!1
                .build()
                // pulls intake out then lifts lift up above intake claw height when closed then tucks intake then opens outtake
                // Big Beautiful Task// a // that is a // that makes giulio is a // /* AAAAAAAAAAAAAA */ // giulio toolbag !!!!!!!!!!!!!!!!
                // pepsi min is a government operation sponsored by Big Cola to support their patriarchal structure in the bisector known as Australia.
                .with(robot.lift.tasks.goTo(270).timeout(Seconds.of(3)).after(robot.outtakeGrip.tasks.open().after(robot.tuck.get().after((robot.lift.tasks.goTo(500).timeout(Seconds.of(2)).after(robot.intake.tasks.goTo(150).timeout(Seconds.of(1))))))))
        );

        /*
        add(robot.drive.makeTrajectory(currentPoseMap)
                .strafeTo(new Vector2d(-24*1.8, 24*1.5), Inches)
                .strafeTo(new Vector2d(-24*1.8, 8), Inches)
                .strafeTo(new Vector2d(-24*2.4, 8), Inches)
                .strafeTo(new Vector2d(-24*2.4, 24*2.2+1), Inches)
                .strafeTo(new Vector2d(-24*2.4, 8), Inches)
                .strafeTo(new Vector2d(-24*3.1, 8), Inches)
                .strafeTo(new Vector2d(-24*3.1, 24*2.2+1), Inches)
                .strafeTo(new Vector2d(-24*3.1, 8), Inches)
                .strafeTo(new Vector2d(-24*3.7, 8), Inches)
                .strafeTo(new Vector2d(-24*3.7, 24*2.2+1), Inches)
                .strafeTo(new Vector2d(-24*3.7+6, 24*2.2-6), Inches)
                .strafeToLinearHeading(new Vector2d(-24*2.5, 56), Inches, 90, Degrees)
                .build()
                .with((robot.lift.tasks.goTo(270).timeout(Seconds.of(3)).after(robot.outtakeGrip.tasks.open())))
        );
        */

    }
}