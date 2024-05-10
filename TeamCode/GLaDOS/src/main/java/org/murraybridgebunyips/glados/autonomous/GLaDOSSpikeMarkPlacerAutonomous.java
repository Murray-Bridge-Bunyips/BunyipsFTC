package org.murraybridgebunyips.glados.autonomous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Direction;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.GetTriPositionContourTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.common.centerstage.vision.BlueTeamProp;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Place a purple pixel loaded on the right side onto the scanned Spike Mark.
 */
@Autonomous(name = "Spike Mark Placer")
public class GLaDOSSpikeMarkPlacerAutonomous extends AutonomousBunyipsOpMode implements RoadRunner {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;
    private HoldableActuator arm;
    private DualServos claws;
    private Vision vision;
    private ColourThreshold teamProp;
    private GetTriPositionContourTask getTeamProp;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
        arm = new HoldableActuator(config.arm).withMovingPower(0.5);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);
        vision = new Vision(config.webcam);

        setOpModes(StartingPositions.use());
        addSubsystems(drive, arm, claws, vision);
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) return;
        StartingPositions startingPosition = (StartingPositions) selectedOpMode.require();
        switch (startingPosition) {
            case STARTING_RED_LEFT:
            case STARTING_RED_RIGHT:
                teamProp = new RedTeamProp();
                break;
            case STARTING_BLUE_LEFT:
            case STARTING_BLUE_RIGHT:
                teamProp = new BlueTeamProp();
                break;
        }

        vision.init(teamProp);
        vision.start(teamProp);
        vision.startPreview();

        getTeamProp = new GetTriPositionContourTask(teamProp);
        setInitTask(getTeamProp);
    }

    @Override
    protected void onStart() {
        Direction spikeMark = getTeamProp.getPosition();
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }
}
