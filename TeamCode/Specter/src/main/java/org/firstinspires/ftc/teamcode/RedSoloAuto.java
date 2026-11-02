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

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Red Solo Auto")
public class RedSoloAuto extends AutonomousBunyipsOpMode {
    private final Specter robot = new Specter();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = redRight()
                .tile(0.9)
                .rotate(Degrees.of(-90))
                .forward(FieldTile.of(2))
                .build()
                .save();
        robot.drive.setPose(start.toFieldPose());
        robot.drive.makeTrajectory()

                .stopAndAdd(new ParallelTaskGroup(
                        robot.leftShooter.tasks.runFor(Seconds.of(2.5), 0.7),
                        robot.rightShooter.tasks.runFor(Seconds.of(2.5), 0.7),
                        new ParallelTaskGroup(
                                robot.leftShooter.tasks.runFor(Seconds.of(3), 0.7),
                                robot.rightShooter.tasks.runFor(Seconds.of(3), 0.7),
                                robot.gate.tasks.toggle(),
                                robot.intake.tasks.runFor(Seconds.of(3), 1),
                                robot.transfer.tasks.runFor(Seconds.of(3), 1)
                        ).after(Seconds.of(2.5))
                ))
                .afterTime(0, robot.gate.tasks.toggle())

                .strafeToLinearHeading(new Vector2d(-50, -11), Inches, 180, Degrees)

                .strafeToLinearHeading(new Vector2d(-51, -24), Inches, 180, Degrees)
                .afterTime(0, robot.sweeper.tasks.toggleBoth())
                .waitFor(0.3)

                .afterTime(0, robot.intake.tasks.runFor(Seconds.of(2), 1))

                .strafeToLinearHeading(new Vector2d(-55, -24), Inches, 180, Degrees)


                .waitFor(2)

                .strafeToLinearHeading(new Vector2d(-51, -24), Inches, 180, Degrees)



                .strafeToLinearHeading(new Vector2d(-50, -11), Inches, 180, Degrees)
                .afterTime(0, robot.leftShooter.tasks.runFor(Seconds.of(3), 0.7))
                .afterTime(0, robot.rightShooter.tasks.runFor(Seconds.of(3), 0.7))
                .strafeToLinearHeading(new Vector2d(-62, -11), Inches, 180, Degrees)

                .waitFor(0.5)

                .stopAndAdd(new ParallelTaskGroup(
                        robot.leftShooter.tasks.runFor(Seconds.of(3), 0.7),
                        robot.rightShooter.tasks.runFor(Seconds.of(3), 0.7),
                        robot.gate.tasks.toggle(),
                        robot.intake.tasks.runFor(Seconds.of(3), 1),
                        robot.transfer.tasks.runFor(Seconds.of(3), 1)
                ))

                .afterTime(0, robot.gate.tasks.toggle())

                .strafeToLinearHeading(new Vector2d(-48, -11), Inches, 180, Degrees)


                .strafeToLinearHeading(new Vector2d(-47, -60), Inches, 180, Degrees)

                .addTask();

    }
}