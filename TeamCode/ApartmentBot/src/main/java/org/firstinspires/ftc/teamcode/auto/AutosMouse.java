package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.Nullable;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ApartmentBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Forward 0.2 Seconds", preselectTeleOp = "TeleOp")
public class AutosMouse extends AutonomousBunyipsOpMode {
    private final ApartmentBot robot = new ApartmentBot();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        // Drives forward for 0.2 seconds, they're calling it the most basic auto of all time
        add(
            Task.task()
                .periodic(() -> robot.drive.setPower(Geometry.vel(1, 0, 0)))
                .timeout(Seconds.of(0.2))
        );
    }
}

