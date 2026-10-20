package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;
@Disabled
@Autonomous(name = "Red Close Zone Auto")
public class DamonRedCloseAuto extends AutonomousBunyipsOpMode {
    private final Damon damon = new Damon();

    @Override
    protected void onInitialise() {
        damon.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = blueRight()
                .tile(1.1)
                .rotate(Degrees.of(38))
                .forward(FieldTile.of(0.7))
                .build()
                .save();
        damon.drive.setPose(start.toFieldPose());
        damon.drive.makeTrajectory()
                .afterTime(0, damon.shooter.tasks.runFor(Seconds.of(3), 0.85))
                .strafeToLinearHeading(new Vector2d(-20, 8.5), Inches, -49, Degrees)
                //Launching
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 0.85),
                                damon.intake.tasks.runFor(Seconds.of(5), 1),
                                new SequentialTaskGroup(
                                        damon.transferWheel.tasks.runFor(Seconds.of(3), 0.85),
                                        damon.transferWheel.tasks.runFor(Seconds.of(2), 0.7)
                                )
                        ).after(Seconds.of(1))
                ))
                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(2), -1))
                .strafeToLinearHeading(new Vector2d(-8, 8.5), Inches, 90, Degrees)


                //Intake
                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(5), 1))
                .strafeTo(new Vector2d(-8, 53))


                .afterTime(0, damon.shooter.tasks.runFor(Seconds.of(3), 0.85))
                //Launching
                .strafeToLinearHeading(new Vector2d(-20, 8.5), Inches, -49, Degrees)
                .stopAndAdd(new ParallelTaskGroup(
                       damon.shooter.tasks.runFor(Seconds.of(5), 0.85),
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 0.85),
                                damon.intake.tasks.runFor(Seconds.of(5), 1),
                                new SequentialTaskGroup(
                                        damon.transferWheel.tasks.runFor(Seconds.of(3), 0.85),
                                        damon.transferWheel.tasks.runFor(Seconds.of(2), 0.7)
                                )
                        ).after(Seconds.of(1))
                ))

                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(2), -1))
                .strafeToLinearHeading(new Vector2d(17, 8.5), Inches, 90, Degrees)
                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(5), 1))
                .strafeTo(new Vector2d(17, 53))
                .addTask();
    }
}