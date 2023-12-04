package org.murraybridgebunyips.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.Inches;
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

@Autonomous(name = "Far Park Auto")
public class WheatleyFarParkAuto extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
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
            case RED_LEFT:
                addNewTrajectory()
                        .setTimeout(-1)
                        .forward(Inches.fromCM(175))
                        .turn(Math.toRadians(-90))
                        .forward(Inches.fromCM(340))
                        .build();
                break;

            case BLUE_LEFT:
                addNewTrajectory(new Pose2d(-36.94, 70.42, Math.toRadians(270.00)))
                        .setTimeout(-1)
                        .splineTo(new Vector2d(-25.45, 35.16), Math.toRadians(6.83))

                        // Since we could possibly interfere with our teammate's Autonomous,
                        // we wait a few seconds to give them time to move.
                        .waitSeconds(10)
                        .splineTo(new Vector2d(25.45, 39.52), Math.toRadians(11.92))
                        .splineTo(new Vector2d(61.50, 59.92), Math.toRadians(0.00))
                        .build();
                break;

            case RED_RIGHT:
                addNewTrajectory(new Pose2d(-36.49, -72.20, Math.toRadians(90.00)))
                        .setTimeout(-1)
                        .forward(Inches.fromCM(25))
                        .strafeRight(Inches.fromCM(260))
                        .build();
                break;

            case BLUE_RIGHT:
                // RED_LEFT and BLUE_RIGHT should pretty much be the same, just taking different turns
                addNewTrajectory(new Pose2d(-35.31, 72.00, Math.toRadians(90.00)))
                        .setTimeout(-1)
                        .forward(Inches.fromCM(175))
                        .turn(Math.toRadians(90))
                        .forward(Inches.fromCM(340))
                        .build();
                break;
        }
    }
}
