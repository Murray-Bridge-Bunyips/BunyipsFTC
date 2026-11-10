package org.firstinspires.ftc.teamcode.auto;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.BioBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Forward 0.2 Seconds", preselectTeleOp = "TeleOp")
public class BioBotAutonomous extends AutonomousBunyipsOpMode {
    private final BioBot robot = new BioBot();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        add(
                Task.task()
                        .periodic(() -> robot.drive.setPower(Geometry.vel(1, 0, 0)))
                        .timeout(Seconds.of(0.2))
        );
    }
}