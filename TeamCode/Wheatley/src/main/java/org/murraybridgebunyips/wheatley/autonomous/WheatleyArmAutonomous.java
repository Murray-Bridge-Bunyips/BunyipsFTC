package org.murraybridgebunyips.wheatley.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.GetTeamPropTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.RedTeamProp;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

import java.util.List;

/**
 * Use Wheatley's arm to place a Purple Pixel (loaded on left) to the Spike Mark detected in Init Phase.
 * @author Lucas Bubner, 2024
 */
@Autonomous(name = "Arm Autonomous")
@Disabled
public class WheatleyArmAutonomous extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final WheatleyConfig config = new WheatleyConfig();
    private Vision vision;
    private ColourThreshold teamProp;
    private GetTeamPropTask getTeamProp;

    @Override
    protected void onInitialise() {
        config.init();
        vision = new Vision(config.webcam);
    }

    @Override
    protected MecanumDrive setDrive() {
        return new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br
        );
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return StartingPositions.use();
    }

    @Override
    protected RobotTask setInitTask() {
        return getTeamProp;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.getObj()) {
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
        getTeamProp = new GetTeamPropTask(teamProp);
    }

    @Override
    protected void onStart() {
        if (getTeamProp.getPosition() != null)
            addRetainedTelemetry("Spike mark locked: %", getTeamProp.getPosition().toString());

        switch (getTeamProp.getPosition()) {
            case LEFT:
                addNewTrajectory(new Pose2d(-36.43, -71.81, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-47.21, -45.13), Math.toRadians(90.00))
                        .build();
                break;
            case RIGHT:
                addNewTrajectory(new Pose2d(-36.57, -71.24, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-32.78, -39.79), Math.toRadians(82.34))
                        .build();
                break;
            case FORWARD:
                addNewTrajectory(new Pose2d(-36.58, -74.71, Math.toRadians(90.00)))
                        .splineTo(new Vector2d(-36.00, -37.35), Math.toRadians(90.29))
                        .build();
                break;
        }
    }
}