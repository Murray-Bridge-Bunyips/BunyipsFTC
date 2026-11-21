package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.Nullable;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ApartmentBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.SymmetricPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.WaitTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Ref;
import dev.frozenmilk.util.cell.RefCell;

/**
 * Waits, shoots preload pollen into the side of the hive that will be facing up if it gets tipped,
 * then parks.
 */
@Autonomous(name = "Shoot into Tipped Hive", preselectTeleOp = "TeleOp")
public class ShootIntoTippedHive extends AutonomousBunyipsOpMode {
    private final ApartmentBot robot = new ApartmentBot();
    PoseMap currentPoseMap;

    private final double width = robot.width;
    private final double length = robot.length;
    private final double diagonal = robot.diagonal;

    @Override
    protected void onInitialise() {
        robot.init();
        setOpModes(
                StartingConfiguration.redLeft().tile(1-(((24-length)/2)/24)).forward(Inches.of(24*2)).rotate(Degrees.of(90)),
                StartingConfiguration.blueLeft().tile(1-(((24-length)/2)/24)).forward(Inches.of(24*2)).rotate(Degrees.of(90))
        ).assignButton(0, 0, Controls.B).assignButton(0, 1, Controls.X);
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position startingPosition = (StartingConfiguration.Position) selectedOpMode.get();
        currentPoseMap = startingPosition.isRed() ? new IdentityPoseMap() : new SymmetricPoseMap();

        robot.drive.setPose(startingPosition.toFieldPose());

        RefCell<Pose2d> pose = Ref.of(robot.drive.getPose());

        add(new WaitTask(Seconds.of(10)));

        add(robot.launch);

        robot.drive.makeTrajectory(pose.get(), currentPoseMap)
                .strafeTo(new Vector2d((-24)*2,-12), Inches)
                .strafeTo(new Vector2d((-24)*2,-10-(24*2)), Inches)
                .addTask(pose);
    }
}

