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
 * This Auto is for when you are parking on the RIGHT of your alliance backdrop
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
                        // TODO: Test this time
                        .waitSeconds(5)
                        .splineTo(new Vector2d(61.66, -59.87), Math.toRadians(0.00))
                        .build();
                break;

            case STARTING_BLUE_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromCM(116.84))
                        .build();

                addNewTrajectory()
                        .strafeLeft(Inches.fromCM(116.84))
                        .build();

                addNewTrajectory()
                        .strafeRight(Inches.fromCM(5))
                        .build();
                break;

            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .strafeRight(Inches.fromCM(116.84))
                        .build();

                addNewTrajectory()
                        .strafeLeft(Inches.fromCM(10))
                        .build();
                break;

            case STARTING_BLUE_RIGHT:
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
