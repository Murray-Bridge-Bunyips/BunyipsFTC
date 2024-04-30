package org.murraybridgebunyips.glados.autonomous;

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
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.MessageTask;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Park on the right side of the backdrop.
 */
@Autonomous(name = "Right Park")
public class GLaDOSRightPark extends AutonomousBunyipsOpMode implements RoadRunner {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInitialise() {
        config.init();
        setOpModes(StartingPositions.use());
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
    }

    @Override
    protected void onReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        StartingPositions startingPosition = (StartingPositions) selectedOpMode.getObj();
        switch (startingPosition) {
            case STARTING_RED_LEFT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                makeTrajectory()
                        .forward(5)
                        .addTask();
                makeTrajectory()
                        .strafeRight(180, Centimeters)
                        .addTask();
                makeTrajectory()
                        .back(2)
                        .addTask();
                makeTrajectory()
                        .strafeRight(100)
                        .addTask();
                break;

            case STARTING_BLUE_LEFT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                makeTrajectory()
                        .forward(3, FieldTiles)
                        .addTask();
                makeTrajectory()
                        .strafeLeft(3, FieldTiles)
                        .addTask();
                break;

            case STARTING_RED_RIGHT:
                makeTrajectory()
                        .strafeRight(180, Centimeters)
                        .addTask();
                break;

            case STARTING_BLUE_RIGHT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                makeTrajectory()
                        .forward(3, FieldTiles)
                        .addTask();
                makeTrajectory()
                        .strafeLeft(5.5, FieldTiles)
                        .addTask();
                break;
        }
    }
}
