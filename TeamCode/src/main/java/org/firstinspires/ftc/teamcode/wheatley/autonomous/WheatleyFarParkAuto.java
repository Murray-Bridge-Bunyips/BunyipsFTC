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
 * This Auto is for when you are FURTHEST from the backdrop
 * Make sure to coordinate with your alliance before selecting an Autonomous
 * <p></p>
 * A for Short Red<p></p>
 * B for Short Blue<p></p>
 * X for Long Red<p></p>
 * Y for Long Blue<p></p>
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyFarParkAuto extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
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

        switch ((StartingPositions)selectedOpMode.getObj()) {
            case RED_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromCM(150))
                        .strafeRight(Inches.fromCM(240))
                        .build();

            case BLUE_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromCM(150))
                        .strafeLeft(Inches.fromCM(240))
                        .build();

            case RED_RIGHT:
                addNewTrajectory()
                        .forward(Inches.fromCM(25))
                        .strafeRight(Inches.fromCM(260))
                        .build();

            case BLUE_RIGHT:
                addNewTrajectory()
                        .forward(Inches.fromCM(25))
                        .strafeLeft(Inches.fromCM(260))
                        .build();
        }
    }
}
