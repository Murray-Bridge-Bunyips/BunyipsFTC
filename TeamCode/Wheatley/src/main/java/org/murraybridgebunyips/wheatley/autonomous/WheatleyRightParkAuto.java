package org.murraybridgebunyips.wheatley.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.MessageTask;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

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
 * @author Lachlan Paul, 2024
 */

@Autonomous(name = "Right Park Auto")
public class WheatleyRightParkAuto extends AutonomousBunyipsOpMode implements RoadRunner {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu,
                config.fl, config.fr, config.bl, config.br
        );
        setOpModes(StartingPositions.use());
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return drive;
    }

    @Override
    protected void onReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case STARTING_RED_LEFT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                addNewTrajectory()
                        .forward(5)
                        .build();
                addNewTrajectory()
                        .strafeRight(180, Centimeters)
                        .build();
                addNewTrajectory()
                        .back(2)
                        .build();
                addNewTrajectory()
                        .strafeRight(100)
                        .build();
                break;

            case STARTING_BLUE_LEFT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                addNewTrajectory()
                        .forward(3, FieldTiles)
                        .build();
                addNewTrajectory()
                        .strafeLeft(3, FieldTiles)
                        .build();
                break;

            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .strafeRight(180, Centimeters)
                        .build();
                break;

            case STARTING_BLUE_RIGHT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                addNewTrajectory()
                        .forward(3, FieldTiles)
                        .build();
                addNewTrajectory()
                        .strafeLeft(5.5, FieldTiles)
                        .build();
                break;
        }
    }
}
