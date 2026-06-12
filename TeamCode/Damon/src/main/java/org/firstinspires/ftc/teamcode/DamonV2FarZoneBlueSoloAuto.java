package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.ConditionalTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.RaceTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Blue Far Zone Auto V2")
public class DamonV2FarZoneBlueSoloAuto extends AutonomousBunyipsOpMode {
    private final DamonV2 damon = new DamonV2();

    @Override
    protected void onInitialise() {
        damon.init();
        //Try to move camera init code into the confi file?

    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = redRight()
                .tile(1)
                .rotate(Degrees.of(-90))
                .forward(FieldTile.of(1.9))
                .build()
                .save();
        damon.drive.setPose(start.toFieldPose());
        damon.drive.makeTrajectory()
                .afterTime(0, damon.shooter.tasks.runFor(Seconds.of(3), 1))
                .strafeToLinearHeading(new Vector2d(53, -15), Inches, 35, Degrees) //Shooting

                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(3), 1),
                                damon.intake.tasks.runFor(Seconds.of(3), 0.4),
                                damon.transferLeft.tasks.runFor(Seconds.of(3), 0.8),
                                damon.transferRight.tasks.runFor(Seconds.of(3), 0.8),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1)))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())


                .strafeToLinearHeading(new Vector2d(35.5, -25), Inches, -90, Degrees)
                .afterTime(0, new RaceTaskGroup(
                        damon.intake.tasks.runFor(Seconds.of(3), 0.9),
                        Task.waitFor(() -> !damon.touchSensor.isPressed()),
                        damon.transferRight.tasks.runFor(Seconds.of(3), 0.6),
                        damon.transferLeft.tasks.runFor(Seconds.of(3), 0.6)

                ))
                .strafeToLinearHeading(new Vector2d(35.5, -55), Inches, -90, Degrees) //Intaking (Stop Intake once three balls)

                .afterTime(0, damon.shooter.tasks.runFor(Seconds.of(3), 1))
                .strafeToLinearHeading(new Vector2d(53, -15), Inches, 35, Degrees) //Shooting
                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(3), 1),
                                damon.intake.tasks.runFor(Seconds.of(3), 0.4),
                                damon.transferLeft.tasks.runFor(Seconds.of(3), 0.8),
                                damon.transferRight.tasks.runFor(Seconds.of(3), 0.8),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1)))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())

                .strafeToLinearHeading(new Vector2d(11.7, -25), Inches, -90, Degrees)

                .strafeToLinearHeading(new Vector2d(35.5, -25), Inches, -90, Degrees)
                .afterTime(0, new RaceTaskGroup(
                        damon.intake.tasks.runFor(Seconds.of(3), 0.9),
                        Task.waitFor(() -> !damon.touchSensor.isPressed()),
                        damon.transferRight.tasks.runFor(Seconds.of(3), 0.6),
                        damon.transferLeft.tasks.runFor(Seconds.of(3), 0.6)

                ))
                .strafeToLinearHeading(new Vector2d(11.7, -50), Inches, -90, Degrees) //Intaking (Stop Intake once three balls)
                .strafeToLinearHeading(new Vector2d(53, -15), Inches, 35, Degrees) //Shooting
                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(3), 1),
                                damon.intake.tasks.runFor(Seconds.of(3), 0.4),
                                damon.transferLeft.tasks.runFor(Seconds.of(3), 0.8),
                                damon.transferRight.tasks.runFor(Seconds.of(3), 0.8),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1)))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())


                .stopAndAdd(damon.gate.tasks.toggle())
                .strafeToLinearHeading(new Vector2d(-11.8, -25), Inches, -90, Degrees)


                .afterTime(0, new RaceTaskGroup(
                        damon.intake.tasks.runFor(Seconds.of(3), 0.9),
                        Task.waitFor(() -> !damon.touchSensor.isPressed()),
                        damon.transferRight.tasks.runFor(Seconds.of(3), 0.6),
                        damon.transferLeft.tasks.runFor(Seconds.of(3), 0.6)

                ))
                .strafeToLinearHeading(new Vector2d(-11.8, -50), Inches, -90, Degrees) //Intaking (Stop Intake once three balls)

                .strafeToLinearHeading(new Vector2d(-15, -14.5), Inches, 43.5, Degrees) //Shooting
                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(3), 1),
                                damon.intake.tasks.runFor(Seconds.of(3), 0.4),
                                damon.transferLeft.tasks.runFor(Seconds.of(3), 0.8),
                                damon.transferRight.tasks.runFor(Seconds.of(3), 0.8),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1))),
                                        damon.kicker.tasks.toggleBoth().then(wait(Seconds.of(1)))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())


                .addTask();


    }
}