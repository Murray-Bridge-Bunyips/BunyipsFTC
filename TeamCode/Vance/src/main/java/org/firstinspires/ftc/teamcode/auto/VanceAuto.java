package org.firstinspires.ftc.teamcode.auto;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Milliseconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.TurnTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous
public class VanceAuto extends AutonomousBunyipsOpMode {
    private final Vance vance = Vance.instance;
    private final Pose2d basketPos = Geometry.poseFrom(new Vector2d(58.93, 55.54), Inches, -50, Degrees);
    private final int shPlaceHeight = 500;
    private final int elPlaceHeight = 33;
    private final int pickUpPos = 156;

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
//        vance.drive.setPose(new Vector2d(35.51, 66.60), Inches, -90.00, Degrees);
//        vance.drive.makeTrajectory()
//                .strafeTo(new Vector2d(58.93, 55.54), Inches)
//                .turn(-40, Degrees)
//                .addTask();
        vance.drive.setPose(new Vector2d(45.91, 59.88), Inches, 230.00, Degrees);
        vance.drive.makeTrajectory()
                .strafeTo(new Vector2d(56.45, 56.19), Inches)
                .addTask();

        add(vance.wholeArmUp);
        add(vance.intake.tasks.runFor(Milliseconds.of(500), vance.EJECT));

        pickAndPlace(20);
        // TODO: TOOD: will need a unique one for third sample since it's against the wall
    }


    private void pickAndPlace(int degreesToTurn) {
        TurnTask turnTask = new TurnTask(vance.drive, Degrees.of(degreesToTurn));
        TurnTask reverseTurnTask = new TurnTask(vance.drive, Degrees.of(-degreesToTurn));

        add(turnTask);
        add(vance.shoulder.tasks.home());

        new ParallelTaskGroup(
            add(vance.elbow.tasks.goToProfiled(pickUpPos)),
            add(vance.intake.tasks.runFor(Milliseconds.of(2000), 1))
        );

        add(reverseTurnTask);
        add(vance.wholeArmUp);
        add(vance.intake.tasks.runFor(Milliseconds.of(500), 1));
    }
}
