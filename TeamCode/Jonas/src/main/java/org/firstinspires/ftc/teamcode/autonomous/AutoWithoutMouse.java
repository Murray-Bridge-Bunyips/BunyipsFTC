package org.firstinspires.ftc.teamcode.autonomous;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Move Forward 0.4 Sec", preselectTeleOp = "TeleOp")
public class AutoWithoutMouse extends AutonomousBunyipsOpMode {
    private final Jonas robot = new Jonas();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        add(
                Task.task()
                        .periodic(() -> robot.drive.setPower(Geometry.vel(1, 0, 0)))
                        .timeout(Seconds.of(0.4))
        );
    }
}
