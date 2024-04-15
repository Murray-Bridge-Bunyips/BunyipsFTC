package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
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
                addNewTrajectory(new Pose2d(-38.58, -62.79, Math.toRadians(90.00)))
                        .lineToLinearHeading(new Pose2d(-38.73, -6.09, Math.toRadians(360.00)))
                        .splineTo(new Vector2d(18.43, -6.56), Math.toRadians(0.00))
                        .splineTo(new Vector2d(49.67, -52.32), Math.toRadians(298.64))
                        .splineTo(new Vector2d(64.66, -65.91), Math.toRadians(299.20))
                        .build();
                break;
            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .strafeRight(Inches.convertFrom(80, Centimeters))
                        .build();
                break;
            case STARTING_BLUE_LEFT:
                // worlds worst autonomous on blue right
//                addNewTrajectory(new Pose2d(14.68, 65.13, Math.toRadians(270.00)))
//                        .splineToLinearHeading(new Pose2d(37.64, 34.36, Math.toRadians(-90.00)), Math.toRadians(-90.00))
//                        .splineTo(new Vector2d(72.78, 11.25), Math.toRadians(0.00))
//                        .build();
                break;
            case STARTING_BLUE_RIGHT:
                addNewTrajectory(new Pose2d(-38.89, 63.10, Math.toRadians(270.00)))
                        .lineToLinearHeading(new Pose2d(-38.73, 9.21, Math.toRadians(360.00)))
                        .lineTo(new Vector2d(72.62, 11.09))
                        .build();
                break;
        }
    }
}
