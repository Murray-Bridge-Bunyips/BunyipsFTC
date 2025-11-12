package org.firstinspires.ftc.teamcode.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.PoseMap;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.SymmetricPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Autonomous", preselectTeleOp = "TeleOp")
public class Auto extends AutonomousBunyipsOpMode {
    private final Jonas robot = new Jonas();
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
//        startingPos = startingPosition;
        currentPoseMap = startingPosition.isRed() ? new SymmetricPoseMap() : new IdentityPoseMap();

        robot.drive.setPose(startingPosition.toFieldPose());

        /*
        .setDimensions(13, 12.75)

        drive.makeTrajectory(new Pose2d(-47.5, 55, Radians.convertFrom(126, Degrees)))
                .strafeTo(new Vector2d(-24*1.5, 24*0.5), Inches)
                .strafeToLinearHeading(new Vector2d(72-(35+24*2), 24*0.5), Inches, 90, Degrees)
                .strafeTo(new Vector2d(72-(35+24*2), 48-(12.75/2)), Inches)
                .strafeToLinearHeading(new Vector2d(-24*1.5, 24*0.5), Inches, 126, Degrees)
                .addTask();
         */
    }
}
