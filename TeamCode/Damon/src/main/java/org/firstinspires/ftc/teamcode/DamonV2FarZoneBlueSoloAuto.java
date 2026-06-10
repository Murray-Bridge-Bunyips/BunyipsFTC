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
@Disabled
@Autonomous(name = "Blue Far Zone Auto")
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
                .tile(2.3)
                .rotate(Degrees.of(0))
                .forward(FieldTile.of(0.7))
                .build()
                .save();
        damon.drive.setPose(start.toFieldPose());
        damon.drive.makeTrajectory()
                .afterTime(0, damon.shooter.tasks.runFor(Seconds.of(3), 0.9))
                .strafeToLinearHeading(new Vector2d(53, -15), Inches, 35, Degrees) //Shooting

                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(damon.kicker.tasks.toggleBoth())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 1),
                                damon.intake.tasks.runFor(Seconds.of(5), 0.6),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())


                .strafeToLinearHeading(new Vector2d(35.5, -25), Inches, -90, Degrees)
                .afterTime(0, new RaceTaskGroup(
                        damon.intake.tasks.runFor(Seconds.of(3), 0.9),
                        Task.waitFor(() -> !damon.touchSensor.isPressed())

                ))
                .strafeToLinearHeading(new Vector2d(35.5, -55), Inches, -90, Degrees) //Intaking (Stop Intake once three balls)

                .afterTime(0, damon.shooter.tasks.runFor(Seconds.of(3), 1))
                .strafeToLinearHeading(new Vector2d(53, -15), Inches, 35, Degrees) //Shooting
                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(damon.kicker.tasks.toggleBoth())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 1),
                                damon.intake.tasks.runFor(Seconds.of(5), 0.6),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())

                .strafeToLinearHeading(new Vector2d(11.7, -25), Inches, -90, Degrees)

                .strafeToLinearHeading(new Vector2d(35.5, -25), Inches, -90, Degrees)
                .afterTime(0, new RaceTaskGroup(
                        damon.intake.tasks.runFor(Seconds.of(3), 0.9),
                        Task.waitFor(() -> !damon.touchSensor.isPressed())

                ))
                .strafeToLinearHeading(new Vector2d(11.7, -50), Inches, -90, Degrees) //Intaking (Stop Intake once three balls)
                .strafeToLinearHeading(new Vector2d(53, -15), Inches, 35, Degrees) //Shooting
                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(damon.kicker.tasks.toggleBoth())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 1),
                                damon.intake.tasks.runFor(Seconds.of(5), 0.6),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())
                .strafeToLinearHeading(new Vector2d(-11.8, -25), Inches, -90, Degrees)


                .afterTime(0, new RaceTaskGroup(
                        damon.intake.tasks.runFor(Seconds.of(3), 0.9),
                        Task.waitFor(() -> !damon.touchSensor.isPressed())

                ))
                .strafeToLinearHeading(new Vector2d(-11.8, -50), Inches, -90, Degrees) //Intaking (Stop Intake once three balls)

                .strafeToLinearHeading(new Vector2d(-19, -18.5), Inches, 50, Degrees) //Shooting
                .stopAndAdd(damon.gate.tasks.toggle())
                .stopAndAdd(damon.kicker.tasks.toggleBoth())
                .stopAndAdd(new ParallelTaskGroup(
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 0.7),
                                damon.intake.tasks.runFor(Seconds.of(5), 0.6),
                                new SequentialTaskGroup(
                                        damon.kicker.tasks.toggleBoth().after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                                .then(damon.kicker.tasks.toggleBoth()).after(Seconds.of(0.3))
                                )
                        ).after(Seconds.of(1))
                ))

                .stopAndAdd(damon.gate.tasks.toggle())


                .addTask();


    }
}