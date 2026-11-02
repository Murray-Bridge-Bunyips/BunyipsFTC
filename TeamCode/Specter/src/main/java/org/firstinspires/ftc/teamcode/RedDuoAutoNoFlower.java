package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueLeft;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redLeft;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Red Duo Auto No Flower")
public class RedDuoAutoNoFlower extends AutonomousBunyipsOpMode {
    private final Specter robot = new Specter();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = redLeft()
                .tile(0.9)
                .rotate(Degrees.of(90))
                .forward(FieldTile.of(2))
                .build()
                .save();
        robot.drive.setPose(start.toFieldPose());
        robot.drive.makeTrajectory()


                .waitFor(7)

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

                .strafeToLinearHeading(new Vector2d(-48, -11), Inches, 180, Degrees)

                .strafeToLinearHeading(new Vector2d(-47, -60), Inches, 180, Degrees)


                .addTask();

    }
}