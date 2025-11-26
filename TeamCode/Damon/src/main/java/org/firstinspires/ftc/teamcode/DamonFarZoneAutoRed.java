package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueLeft;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Red Far Zone Auto")
public class DamonFarZoneAutoRed extends AutonomousBunyipsOpMode {
    private final Damon damon = new Damon();

    @Override
    protected void onInitialise() {
        damon.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = blueLeft()
                .tile(1)
                .rotate(Degrees.of(90))
                .forward(FieldTile.of(1.9))
                .build()
                .save();
        damon.drive.setPose(start.toFieldPose());
        damon.drive.makeTrajectory()
                .strafeToLinearHeading(new Vector2d(52, 10), Inches, -24, Degrees)
                .stopAndAdd(new ParallelTaskGroup(
                        damon.shooter.tasks.runFor(Seconds.of(5), 1),
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 1),
                                damon.intake.tasks.runFor(Seconds.of(5), 1),
                                //Slow the transfer wheel?
                                new SequentialTaskGroup(
                                        damon.transferWheel.tasks.runFor(Seconds.of(3), 0.7),
                                        damon.transferWheel.tasks.runFor(Seconds.of(2), 0.8)
                                )
                        ).after(Seconds.of(1.5))
                ))

                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(2), -1))
                .strafeToLinearHeading(new Vector2d(35, 24), Inches, 90, Degrees)
                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(5), 1))
                .strafeTo(new Vector2d(35, 60))
                .strafeToLinearHeading(new Vector2d(52, 10), Inches, -24, Degrees)
                .stopAndAdd(new ParallelTaskGroup(


                        damon.shooter.tasks.runFor(Seconds.of(5), 1),
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 1),
                                damon.intake.tasks.runFor(Seconds.of(5), 1),
                                //Slow the transfer wheel?
                                new SequentialTaskGroup(
                                        damon.transferWheel.tasks.runFor(Seconds.of(3), 0.7),
                                        damon.transferWheel.tasks.runFor(Seconds.of(2), 0.8)
                                )
                        ).after(Seconds.of(1.5))
                ))

                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(2), -1))
                .strafeToLinearHeading(new Vector2d(11, 24), Inches, 90, Degrees)
                .afterTime(0, damon.intake.tasks.runFor(Seconds.of(5), 1))
                .strafeTo(new Vector2d(11, 60))


                .addTask();


    }
}