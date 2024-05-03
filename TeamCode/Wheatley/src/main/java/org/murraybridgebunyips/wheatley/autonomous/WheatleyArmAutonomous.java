package org.murraybridgebunyips.wheatley.autonomous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.GetTriPositionContourTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * Use Wheatley's arm to place a Purple Pixel (loaded on left) to the Spike Mark detected in Init Phase.
 * @author Lucas Bubner, 2024
 */
@Autonomous(name = "Arm Autonomous")
@Disabled
public class WheatleyArmAutonomous extends AutonomousBunyipsOpMode implements RoadRunner {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;
    private Vision vision;
    private ColourThreshold teamProp;
    private GetTriPositionContourTask getTeamProp;

    @Override
    protected void onInitialise() {
        config.init();
        vision = new Vision(config.webcam);
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu,
                config.fl, config.fr, config.bl, config.br
        );
        setOpModes(StartingPositions.use());
        setInitTask(getTeamProp);
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return drive;
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.require()) {
            case STARTING_RED_LEFT:
            case STARTING_RED_RIGHT:
                teamProp = new RedTeamProp();
                break;
            case STARTING_BLUE_LEFT:
            case STARTING_BLUE_RIGHT:
                break;
        }

        vision.init(teamProp);
        vision.start(teamProp);
        getTeamProp = new GetTriPositionContourTask(teamProp);
    }

    @Override
    protected void onStart() {
        if (getTeamProp.getPosition() != null)
            addRetainedTelemetry("Spike mark locked: %", getTeamProp.getPosition().toString());

        switch (getTeamProp.getPosition()) {
            case LEFT:
                makeTrajectory(new Pose2d(-36.43, -71.81, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-47.21, -45.13), Math.toRadians(90.00))
                        .addTask();
                break;
            case RIGHT:
                makeTrajectory(new Pose2d(-36.57, -71.24, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-32.78, -39.79), Math.toRadians(82.34))
                        .addTask();
                break;
            case FORWARD:
                makeTrajectory(new Pose2d(-36.58, -74.71, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-36.00, -37.35), Math.toRadians(90.29))
                        .addTask();
                break;
        }
    }
}