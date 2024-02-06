package org.murraybridgebunyips.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.tasks.MessageTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

import java.util.List;

/**
 * Parking autonomous for Wheatley
 * This Auto is for when you are parking on the LEFT of your alliance backdrop
 * Make sure to coordinate with your alliance before selecting an Autonomous
 * <p></p>
 * A for Short Red<p></p>
 * B for Short Blue<p></p>
 * X for Long Red<p></p>
 * Y for Long Blue<p></p>
 *
 * @author Lachlan Paul, 2023
 */

@Autonomous(name = "Left Park Auto")
public class WheatleyLeftParkAuto extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInitialise() {
        config.init(this);
    }

    @Override
    protected MecanumDrive setDrive() {
        return new MecanumDrive(
                this, config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br
        );
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return StartingPositions.use();
    }

    @Override
    protected RobotTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case STARTING_RED_LEFT:
//                addTask(new MessageTask(this, 15, "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                // TODO: fix
                addNewTrajectory(new Pose2d(-36.80, -71.01))
                        .forward(Inches.fromCM(185))
                        .build();

                addNewTrajectory(new Pose2d(-36.80, -71.01))
                        .strafeRight(Inches.fromCM(280))
                        .build();
                break;

            case STARTING_BLUE_LEFT:
                addNewTrajectory()
                        .strafeLeft(Inches.fromCM(116.84))
                        .build();

                addNewTrajectory()
                        .strafeRight(Inches.fromCM(10))
                        .build();
                break;

            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .forward(Inches.fromCM(116.84))
                        .build();

                addNewTrajectory()
                        .strafeRight(Inches.fromCM(116.84))
                        .build();

                addNewTrajectory()
                        .strafeLeft(Inches.fromCM(5))
                        .build();
                break;

            case STARTING_BLUE_RIGHT:
                addNewTrajectory(new Pose2d(-37.19, 70.81, Math.toRadians(90.00)))
//                        .setReversed(true)
                        .splineTo(new Vector2d(-36.00, 28.04), Math.toRadians(-88.41))
                        .splineTo(new Vector2d(9.55, 7.56), Math.toRadians(-24.21))
                        // TODO: Test wait here
                        .waitSeconds(10)
                        .splineTo(new Vector2d(60.27, 60.27), Math.toRadians(0.00))
//                        .setReversed(false)
                        .build();
                break;
        }
    }
}
