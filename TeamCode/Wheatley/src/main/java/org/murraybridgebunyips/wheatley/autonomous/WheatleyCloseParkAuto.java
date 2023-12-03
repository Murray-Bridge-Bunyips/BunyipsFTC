package org.murraybridgebunyips.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

import java.util.List;

/**
 * Parking autonomous for Wheatley
 * This Auto is for when you are CLOSEST to the backdrop
 * Make sure to coordinate with your alliance before selecting an Autonomous
 * <p></p>
 * A for Short Red<p></p>
 * B for Short Blue<p></p>
 * X for Long Red<p></p>
 * Y for Long Blue<p></p>
 *
 * @author Lachlan Paul, 2023
 */

@Autonomous(name = "Close Park Auto")
public class WheatleyCloseParkAuto extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInitialisation() {
        config.init(this);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
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
                addNewTrajectory(new Pose2d(11.38, -72.59, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(10.99, -37.08), Math.toRadians(91.27))
                        .splineTo(new Vector2d(62.19, -11.57), Math.toRadians(0.00))
                        .build();
                break;

            case BLUE_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromCM(150))
                        .strafeLeft(Inches.fromCM(140))
                        .build();
                break;

            case RED_RIGHT:
                addNewTrajectory()
                        .strafeRight(Inches.fromCM(140))
                        .build();
                break;

            case BLUE_RIGHT:
                addNewTrajectory(new Pose2d(10.79, 71.61, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(11.57, 32.76), Math.toRadians(270.00))
                        .splineTo(new Vector2d(61.41, 12.36), Math.toRadians(0.00))
                        .build();
                break;
        }
    }
}
