package org.firstinspires.ftc.teamcode.wheatley.autonomous;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.RoadRunnerAutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.StartingPositions;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;

import java.util.List;

/**
 * Parking autonomous for Wheatley
 * This Auto is for when you are CLOSEST to the backdrop
 * Make sure to coordinate with your alliance before selecting an Autonomous
 * <p></p>
 * A for Short Red
 * B for Short Blue
 * X for Long Red
 * Y for Long Blue
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyCloseParkAuto extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    @Override
    protected void onInitialisation() {

    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return StartingPositions.use();
    }

    @Override
    protected AutoTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case RED_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromCM(150))
                        .strafeRight(Inches.fromCM(140))
                        .build();

            case BLUE_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromCM(150))
                        .strafeLeft(Inches.fromCM(140))
                        .build();

            case RED_RIGHT:
                addNewTrajectory()
                        .strafeRight(Inches.fromCM(140))
                        .build();

            case BLUE_RIGHT:
                addNewTrajectory()
                        .strafeLeft(Inches.fromCM(140))
                        .build();

        }
    }
}
