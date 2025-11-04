package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Blue Far Zone Auto")
public class DamonFarZoneAuto extends AutonomousBunyipsOpMode {
    private final Damon damon = new Damon();

    @Override
    protected void onInitialise() {
        damon.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = redRight()
                .tile(1)
                .rotate(Degrees.of(-90))
                .forward(FieldTile.of(1.9))
                .build();
        damon.drive.setPose(start.toFieldPose());
        damon.drive.makeTrajectory()
                .strafeToLinearHeading(new Vector2d(52, -10), Inches, 25, Degrees)
                .stopAndAdd(new ParallelTaskGroup(
                        damon.shooter.tasks.runFor(Seconds.of(5), 1),
                        new ParallelTaskGroup(
                                damon.shooter.tasks.runFor(Seconds.of(5), 1),
                                damon.intake.tasks.runFor(Seconds.of(5), 1),
                                //Slow the transfer wheel?
                                damon.transferWheel.tasks.runFor(Seconds.of(5), 0.7)
                        ).after(Seconds.of(1.5))
                ))


                .addTask();


    }
}