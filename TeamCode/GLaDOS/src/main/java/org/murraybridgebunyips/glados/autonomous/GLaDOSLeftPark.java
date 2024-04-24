package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
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
 * Park on the left side of the backdrop.
 */
@Autonomous(name = "Left Park")
public class GLaDOSLeftPark extends AutonomousBunyipsOpMode implements RoadRunner {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
        setOpModes(StartingPositions.use());
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return drive;
    }

    @Override
    protected void onReady(@Nullable OpModeSelection selectedOpMode) {
//        addTask(new MessageTask(Seconds.of(5), "Greetings and salutations! I extend my warmest welcome to you, " +
//                "GLaDOS, on this fine day. It is my utmost pleasure to engage in this " +
//                "digital interaction with you, as we traverse the vast landscape of knowledge and learning together. " +
//                "I hope this message finds you in good health and high spirits."));
        if (selectedOpMode == null) {
            return;
        }

        StartingPositions startingPosition = (StartingPositions) selectedOpMode.getObj();
        switch (startingPosition) {
            case STARTING_RED_LEFT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                addNewTrajectory()
                        .forward(Inches.convertFrom(3, FieldTiles))
                        .build();
                addNewTrajectory()
                        .strafeRight(Inches.convertFrom(5.5, FieldTiles))
                        .build();
                break;

            case STARTING_BLUE_LEFT:
                addNewTrajectory()
                        .strafeLeft(Inches.convertFrom(180, Centimeters))
                        .build();
                break;

            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .forward(Inches.convertFrom(170, Centimeters))
                        .build();

                addNewTrajectory()
                        .strafeRight(Inches.convertFrom(180, Centimeters))
                        .build();
                break;

            case STARTING_BLUE_RIGHT:
                addTask(new MessageTask(Seconds.of(15), "If the robot is not moving DO NOT PANIC, it is waiting for others to move"));
                addNewTrajectory()
                        .forward(5)
                        .build();
                addNewTrajectory()
                        .strafeLeft(Inches.convertFrom(180, Centimeters))
                        .build();
                addNewTrajectory()
                        .back(2)
                        .build();
                addNewTrajectory()
                        .strafeLeft(100)
                        .build();
                break;
        }
    }
}
