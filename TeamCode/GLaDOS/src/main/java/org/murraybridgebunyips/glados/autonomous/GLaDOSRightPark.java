package org.murraybridgebunyips.glados.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

import java.util.List;

@Autonomous(name = "Right Park")
public class GLaDOSRightPark extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInitialisation() {
        config.init(this);
    }

    @Override
    protected MecanumDrive setDrive() {
        return new DualDeadwheelMecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelEncoder, config.perpendicularEncoder);
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
                        .strafeRight(Inches.fromCM(80))
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
