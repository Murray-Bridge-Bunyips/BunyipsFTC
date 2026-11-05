package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.Nullable;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import org.firstinspires.ftc.teamcode.ApartmentBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import dev.frozenmilk.util.cell.RefCell;

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

