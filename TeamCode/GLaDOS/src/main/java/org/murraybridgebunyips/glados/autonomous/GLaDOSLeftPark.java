package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.MessageTask;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Park on the left side of the backdrop.
 */
@Autonomous(name = "Left Park")
public class GLaDOSLeftPark extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInitialise() {
        config.init();
        setOpModes(StartingPositions.use());
    }

    @Override
    protected MecanumDrive setDrive() {
        return new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
    }

    @Override
    protected void onReady(@Nullable OpModeSelection selectedOpMode) {
        addTask(new MessageTask(Seconds.of(5), "Greetings and salutations! I extend my warmest welcome to you, " +
                "GLaDOS, on this fine day. It is my utmost pleasure to engage in this " +
                "digital interaction with you, as we traverse the vast landscape of knowledge and learning together. " +
                "I hope this message finds you in good health and high spirits."));
        if (selectedOpMode == null) {
            return;
        }

        StartingPositions startingPosition = (StartingPositions) selectedOpMode.getObj();

        switch (startingPosition) {
            case STARTING_RED_LEFT:
                addNewTrajectory(new Pose2d(-38.89, -63.10, Math.toRadians(90.00)))
                        .lineToLinearHeading(new Pose2d(-38.73, -9.21, Math.toRadians(0.00)))
                        .lineTo(new Vector2d(70.75, -14.21))
                        .build();
                break;
            case STARTING_RED_RIGHT:
                addNewTrajectory(new Pose2d(14.37, -63.41, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(33.11, -42.48), Math.toRadians(59.82))
                        .splineTo(new Vector2d(69.03, -11.56), Math.toRadians(0.00))
                        .build();
                break;
            case STARTING_BLUE_LEFT:
                addNewTrajectory()
                        .strafeLeft(Inches.convertFrom(80, Centimeters))
                        .build();
                break;
            case STARTING_BLUE_RIGHT:
                addNewTrajectory(new Pose2d(-38.58, 62.79, Math.toRadians(270.00)))
                        .lineToLinearHeading(new Pose2d(-38.73, 6.09, Math.toRadians(0.00)))
                        .splineTo(new Vector2d(17.49, 7.34), Math.toRadians(0.00))
                        .splineTo(new Vector2d(49.67, 52.32), Math.toRadians(61.36))
                        .splineTo(new Vector2d(64.66, 65.91), Math.toRadians(60.80))
                        .build();
                break;
        }
    }
}
