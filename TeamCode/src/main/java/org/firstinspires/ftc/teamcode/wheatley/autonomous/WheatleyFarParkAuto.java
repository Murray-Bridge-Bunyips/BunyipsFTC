package org.firstinspires.ftc.teamcode.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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

@Autonomous(name = "WHEATLEY: Far Park Auto", group = "WHEATLEY")
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

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case RED_LEFT:
                addNewTrajectory(new Pose2d(-36.49, -72.20, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-37.28, -41.00), Math.toRadians(91.44))
                        .splineTo(new Vector2d(60.82, -11.57), Math.toRadians(0.00))
                        .build();

            case BLUE_LEFT:
                addNewTrajectory(new Pose2d(-36.94, 70.42, Math.toRadians(270.00)))
                        .splineTo(new Vector2d(-25.45, 35.16), Math.toRadians(6.83))

                        // Since we could possibly interfere with our teammate's Autonomous,
                        // we wait a few seconds to give them time to move.
                        .waitSeconds(10)
                        .splineTo(new Vector2d(25.45, 39.52), Math.toRadians(11.92))
                        .splineTo(new Vector2d(61.50, 59.92), Math.toRadians(0.00))
                        .build();

            case RED_RIGHT:
                addNewTrajectory()
                        .forward(Inches.fromCM(25))
                        .strafeRight(Inches.fromCM(260))
                        .build();

            case BLUE_RIGHT:
                addNewTrajectory(new Pose2d(-35.31, 72.00, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-37.67, 32.96), Math.toRadians(266.55))
                        .splineTo(new Vector2d(58.86, 12.16), Math.toRadians(0.00))
                        .build();
        }
    }
}
