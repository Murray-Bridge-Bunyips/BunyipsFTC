package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeter;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Auto")
public class DamonRoadRunnerAuto extends AutonomousBunyipsOpMode {
    private final Damon damon = new Damon();

    @Override
    protected void onInitialise() {
        damon.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        damon.drive.makeTrajectory()
                .strafeTo(new Vector2d(3, 0), FieldTile)
                .strafeTo(new Vector2d(3,-2), FieldTile)
                .addTask();
    }
}