package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTile;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration.blueRight;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import dev.frozenmilk.util.cell.RefCell;

@Autonomous(name = "Blue Solo Auto")
public class BlueSoloAuto extends AutonomousBunyipsOpMode {
    private final Specter robot = new Specter();

    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        StartingConfiguration.Position start = blueRight()
                .tile(0.9)
                .rotate(Degrees.of(270))
                .forward(FieldTile.of(2))
                .build()
                .save();
        robot.drive.setPose(start.toFieldPose());
        robot.drive.makeTrajectory()
                //Shoot
                .strafeToLinearHeading(new Vector2d(48, 11), Inches, 0, Degrees)
                .strafeToLinearHeading(new Vector2d(57, 21), Inches, 20, Degrees)
                //Intake from the flower
                .strafeToLinearHeading(new Vector2d(48, 11), Inches, 0, Degrees)
                .strafeToLinearHeading(new Vector2d(62, 11), Inches, 0, Degrees)
                //Shoot
                .strafeToLinearHeading(new Vector2d(54, 11), Inches, 0, Degrees)
                .strafeToLinearHeading(new Vector2d(47, 60), Inches, 0, Degrees)
                .addTask();

    }
}