package org.firstinspires.ftc.teamcode.auto;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous
public class VanceAuto extends AutonomousBunyipsOpMode {
    private final Vance vance = Vance.instance;

    @Override
    protected void onInitialise() {
        setOpModes("Red", "Blue");
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        String res = (String) selectedOpMode.get();

        switch (res) {
            case "Red":

            case "Blue":
                vance.drive.setPose(new Vector2d(36.03, 62.31), Inches, -90.00, Degrees);
                vance.drive.makeTrajectory()
                        .splineTo(new Vector2d(49.84, 40.63), Inches, -90.00, Degrees)
                        .splineTo(new Vector2d(57.71, 54.68), Inches, 50.00, Degrees)
                        .splineTo(new Vector2d(59.04, 37.97), Inches, -89.71, Degrees)
                        .splineTo(new Vector2d(58.31, 54.80), Inches, 70.00, Degrees)
                        .splineTo(new Vector2d(60.98, 38.21), Inches, -70.00, Degrees)
                        .addTask();

        }
    }
}
