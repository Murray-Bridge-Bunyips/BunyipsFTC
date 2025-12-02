package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTiles;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Ref;
import dev.frozenmilk.util.cell.RefCell;

/**
 * Base OpMode to shoot preloaded artifacts.
 *
 * @author Lucas Bubner, 2025
 */
@Autonomous(name = "Goal Side, Move Forward and Shoot Two/Three Preloads")
public class ForwardAndShootPreload extends AutonomousBunyipsOpMode {
    private final Lilbro5000 robot = new Lilbro5000();

    @Override
    protected void onInitialise() {
        setOpModes(2, 3).captionLayer(0, "BALLS PRELOADED? (DEFAULT 3)");
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
        robot.drive.setPose(start.toFieldPose());
        robot.drive.makeTrajectory()
                .strafeTo(new Vector2d(-37.2, -27.5))
                .addTask(last);
        if (selectedOpMode != null && (int) selectedOpMode.get() == 2)
            add(robot.intake.tasks.runFor(Seconds.of(2), 1));
        add(new ParallelTaskGroup(
                        robot.outtake.tasks.run(1),
                        robot.middletake.tasks.run(1),
                        robot.transfer.tasks.run(1)
                                .with(robot.intake.tasks.run(1))
                                .after(Seconds.of(1))
                ).timeout(Seconds.of(10)));
        robot.drive.makeTrajectory(last.get())
                .strafeTo(new Vector2d(-56, -27))
                .addTask();
    }
}
