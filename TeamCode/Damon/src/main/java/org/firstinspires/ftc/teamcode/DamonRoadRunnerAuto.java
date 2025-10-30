package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeter;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Auto")
public class DamonRoadRunnerAuto extends AutonomousBunyipsOpMode {
    private final Damon damon = new Damon();

    @Override
    protected void onInitialise() {
        damon.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        damon.drive.setPose(new Vector2d(-54, -45), Inches, 53, Degrees);
        damon.drive.makeTrajectory()
                //MAKE THIS GO BACK AWAY FROM GOAL FURTHER
                .strafeTo(new Vector2d(-20, -8.5)) //MAKE THIS GO BACK AWAY FROM GOAL FURTHER
                //MAKE THIS GO BACK AWAY FROM GOAL FURTHER
                //MAKE THIS GO BACK AWAY FROM GOAL FURTHER
                //MAKE THIS GO BACK AWAY FROM GOAL FURTHER
                //MAKE THIS GO BACK AWAY FROM GOAL FURTHER
                //Launch Artifacts
                .stopAndAdd(new ParallelTaskGroup(
                        damon.shooter.tasks.runFor(Seconds.of(5), 0.85),
                        new ParallelTaskGroup(
                                damon.intake.tasks.runFor(Seconds.of(5), 1),
                                new SequentialTaskGroup(
                                        damon.transferWheel.tasks.runFor(Seconds.of(3), 0.85),
                                        damon.transferWheel.tasks.runFor(Seconds.of(2), 1)
                                )
                        ).after(Seconds.of(1))
                ))
                //Move to new Artifacts
                .strafeToLinearHeading(new Vector2d(-4, -8.5), Inches, -90, Degrees)
                .strafeTo(new Vector2d(-4, -53))
                //Lower speed and intake Artifacts
                .strafeToLinearHeading(new Vector2d(-20, -8.5), Inches, 53, Degrees)
                //Launch Artifacts
                //.strafeToLinearHeading(new Vector2d(17, -8.5), Inches, -90, Degrees)
                //.strafeTo(new Vector2d(17, -53))
                //.strafeToLinearHeading(new Vector2d(-20, -8.5), Inches, 53, Degrees)
                .addTask();
    }
}