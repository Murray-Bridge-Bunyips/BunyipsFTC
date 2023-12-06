package org.murraybridgebunyips.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.RobotConfig;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

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

@Autonomous(name = "Right Park Auto")
public class WheatleyRightParkAuto extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInitialisation() {
        config.init(this);
        RobotConfig.setLastKnownPosition(null);
        drive = new MecanumDrive(
                this, config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br
        );
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
            case STARTING_RED_LEFT:
                addNewTrajectory(new Pose2d(-36.20, -70.21, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-37.79, -20.29), Math.toRadians(92.04))
                        .splineTo(new Vector2d(31.62, -13.33), Math.toRadians(-29.59))
                        // TODO: Put wait here
                        .splineTo(new Vector2d(61.66, -59.87), Math.toRadians(0.00))
                        .build();

                break;

            case STARTING_BLUE_LEFT:
//                addNewTrajectory(new Pose2d(-36.99, 71.05, Math.toRadians(270.00)))
//                        .splineTo(new Vector2d(-30.00, 37.55), Math.toRadians(-7.71))
//
//                        // Since we could possibly interfere with our teammate's Autonomous,
//                        // we wait a few seconds to give them time to move.
//                        // TODO: Time code and change wait to make sure we can still make it
//                        // .waitSeconds(10)
//                        .splineTo(new Vector2d(32.07, 37.37), Math.toRadians(373.92))
//                        .splineTo(new Vector2d(62.92, 60.65), Math.toRadians(360.00))
//                        .build();

                addNewTrajectory(new Pose2d(11.54, 63.05, Math.toRadians(90.00)))
                        .setReversed(true)
                        .splineTo(new Vector2d(11.73, 32.82), Math.toRadians(269.70))
                        .splineTo(new Vector2d(61.26, 11.93), Math.toRadians(0.00))
                        .setReversed(false)
                        .build();


                break;

            case STARTING_RED_RIGHT:
//                addNewTrajectory(new Pose2d(-36.99, -71.05, Math.toRadians(90.00)))
//                        .splineTo(new Vector2d(-29.03, -37.55), Math.toRadians(-7.71))
//                        .splineTo(new Vector2d(32.07, -37.37), Math.toRadians(-13.92))
//                        .splineTo(new Vector2d(62.92, -60.65), Math.toRadians(0.00))
//                        .build();

                addNewTrajectory(new Pose2d(10.54, -70.61, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(60.86, -61.46), Math.toRadians(0.00))
                        .build();

                break;

            case STARTING_BLUE_RIGHT:
//                addNewTrajectory(new Pose2d(-36.05, 71.43, Math.toRadians(270.00)))
//                        .splineTo(new Vector2d(-36.99, 33.21), Math.toRadians(268.58))
//                        .splineTo(new Vector2d(61.40, 12.58), Math.toRadians(0.00))
//                        .build();

                addNewTrajectory(new Pose2d(-37.39, 66.43, Math.toRadians(90.00)))
                        .setReversed(true)
                        .splineTo(new Vector2d(-36.40, 33.41), Math.toRadians(-88.28))
                        .splineTo(new Vector2d(61.86, 11.73), Math.toRadians(-12.44))
                        .setReversed(false)
                        .build();

                break;
        }
    }
}
