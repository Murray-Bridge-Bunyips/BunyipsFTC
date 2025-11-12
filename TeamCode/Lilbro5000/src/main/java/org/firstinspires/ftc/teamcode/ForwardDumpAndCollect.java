package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTiles;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.constraints.Vel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Ref;
import dev.frozenmilk.util.cell.RefCell;

/**
 * Extension of {@link ForwardAndShootPreload} to attempt to pick up the artifacts closest.
 *
 * @author Lucas Bubner, 2025
 */
@Autonomous(name = "Goal Side, Move Forward and Shoot Three Preloads, and Collect to Re-Shoot")
public class ForwardDumpAndCollect extends AutonomousBunyipsOpMode {
    private final Lilbro5000 robot = new Lilbro5000();

    /**
     * W Interval?
     * <pre><code>
     * robot.hand.grab(sandwich);
     * robot.arm.moveTo(robot.mouth.eatPos);
     * robot.mouth.eat();
     * </code></pre>
     */
    private Task luncheonInterval;

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = blueRight()
                .tile(1.3)
                .forward(FieldTiles.of(5).minus(Inches.of(12)))
                .rotate(Degrees.of(143))
                .build();
        RefCell<Pose2d> last = Ref.empty();
        luncheonInterval = new ParallelTaskGroup(
                robot.outtake.tasks.run(1),
                robot.transfer.tasks.run(1)
                        .with(robot.intake.tasks.run(1))
                        .after(Seconds.of(1))
        ).timeout(Seconds.of(5));
        robot.drive.setPose(start.toFieldPose());
        robot.drive.makeTrajectory()
                .strafeTo(new Vector2d(-37.2, -27.5))
                .addTask(last);
        add(luncheonInterval);
        robot.drive.makeTrajectory(last.get())
                .splineTo(new Vector2d(-7.4, -27.0), -Math.PI / 2)
                .setVelConstraints(Vel.ofMax(0.3, FieldTilesPerSecond))
                .afterTime(0, robot.intake.tasks.runFor(Seconds.of(5), 1))
                .splineTo(new Vector2d(-7.4, -58.2), -Math.PI / 2)
                .resetVelConstraints()
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-37.2, -27.5, Math.PI / 4), Math.toRadians(135))
                .addTask();
        add(luncheonInterval);
        robot.drive.makeTrajectory(last.get())
                .strafeTo(new Vector2d(-56, -27))
                .addTask();
    }
}
