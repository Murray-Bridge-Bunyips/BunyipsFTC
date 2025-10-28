package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeter;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;

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
        damon.drive.setPose(new Vector2d(-54, -45), Inches, 53, Degrees);
        damon.drive.makeTrajectory()
                .strafeTo(new Vector2d(-20, -8.5))
                //Launch Artifacts
                .strafeToLinearHeading(new Vector2d(-8, -8.5), Inches, -90, Degrees)
                .strafeTo(new Vector2d(-8, -53))
                //Lower speed and intake Artifacts
                .strafeToLinearHeading(new Vector2d(-20, -8.5), Inches, 53, Degrees)
                //Launch Artifacts
                .addTask();
    }
}