package org.firstinspires.ftc.teamcode.autonomous;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.DinkyBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Move Forward 0.2 Sec", preselectTeleOp = "TeleOp")
public class AutoOmittedMouse extends AutonomousBunyipsOpMode {
    private final DinkyBot robot = new DinkyBot();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        add(
            new ParallelTaskGroup(
                Task.task()
                    .periodic(() -> robot.drive.setPower(Geometry.vel(1, 0, 0)))
                    .timeout(Seconds.of(0.2)),
                robot.pusher.tasks.open()
            )
        );
    }
}
