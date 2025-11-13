package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTiles;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.redLeft;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.IdentityPoseMap;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.MirroredPoseMap;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.constraints.Vel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
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
        setOpModes(
                blueRight().tile(1.3).forward(FieldTiles.of(5).minus(Inches.of(12))).rotate(Degrees.of(143)),
                redLeft().tile(1.3).forward(FieldTiles.of(5).minus(Inches.of(12))).rotate(Degrees.of(-143))
        ).assignButton(0, 0, Controls.X).assignButton(0, 1, Controls.B);
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingConfiguration.Position start = (StartingConfiguration.Position) selectedOpMode.get();
        PoseMap poseMap = start.isRed() ? new MirroredPoseMap() : new IdentityPoseMap();
        RefCell<Pose2d> last = Ref.empty();
        // giulio was here
        luncheonInterval = new ParallelTaskGroup(
                robot.outtake.tasks.run(1),
                robot.transfer.tasks.run(1)
                        .with(robot.intake.tasks.run(1))
                        .after(Seconds.of(1))
        ).timeout(Seconds.of(5));
        robot.drive.setPose(start.toFieldPose());
        robot.drive.makeTrajectory(poseMap)
                .strafeTo(new Vector2d(-37.2, -27.5))
                .addTask(last);
        add(luncheonInterval);
        robot.drive.makeTrajectory(last.get(), poseMap)
                .splineTo(new Vector2d(-7.4, -27.0), -Math.PI / 2)
                .setVelConstraints(Vel.ofMax(0.3, FieldTilesPerSecond))
                .afterTime(0, robot.intake.tasks.runFor(Seconds.of(16.5), 1).during(robot.transfer.tasks.run(-1)))
                .splineTo(new Vector2d(-7.4, -34.9), -Math.PI / 2) // first ball
                .waitSeconds(3)
                .splineTo(new Vector2d(-7.4, -40.7), -Math.PI / 2) // second ball
                .waitSeconds(3)
                .splineTo(new Vector2d(-7.4, -58.2), -Math.PI / 2) // third ball
                .waitSeconds(5)
                .resetVelConstraints()
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-37.2, -27.5, Math.PI / 4), Math.toRadians(135))
                .addTask();
        add(luncheonInterval);
        robot.drive.makeTrajectory(last.get(), poseMap)
                .strafeTo(new Vector2d(-56, -27))
                .addTask();
    }
}
