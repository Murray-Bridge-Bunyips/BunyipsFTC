package org.murraybridgebunyips.glados.autonomous.l4;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

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
import org.murraybridgebunyips.bunyipslib.external.pid.PIDController;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.AlignToContourTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetTriPositionContourTask;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.common.centerstage.vision.BlueTeamProp;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Place a purple pixel loaded on the right side of the arm onto the scanned Spike Mark and remain in place.
 */
@Autonomous(name = "Spike Mark Placer (Purple on Right, No Park)")
public class GLaDOSSpikeMarkPlacerAutonomous extends AutonomousBunyipsOpMode implements RoadRunner {
    /**
     * Arm delta position from stow to ground.
     */
    public static int ARM_DELTA = 1800;
    /**
     * Number of field tiles to move forward as the first step.
     */
    public static double FORWARD_FIELD_TILES = 1;
    /**
     * Rotation factor in degrees to align with the Spike Mark.
     */
    public static double TURN_ANGLE_DEG = 45;

    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;
    private HoldableActuator arm;
    private DualServos claws;
    private Vision vision;
    private ColourThreshold teamProp;
    private GetTriPositionContourTask getTeamProp;
    private StartingPositions startingPosition;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
        arm = new HoldableActuator(config.arm).withMovingPower(0.5);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);
        vision = new Vision(config.webcam);

        getTeamProp = new GetTriPositionContourTask();
        setOpModes(StartingPositions.use());
        setInitTask(getTeamProp);
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) return;
        startingPosition = (StartingPositions) selectedOpMode.require();
        teamProp = startingPosition.isRed() ? new RedTeamProp() : new BlueTeamProp();

        vision.init(teamProp);
        vision.start(teamProp);
        // TODO: remove debugging statements
        vision.startPreview();
    }

    @Override
    protected void onStart() {
        Direction spikeMark = getTeamProp.getPosition();

        makeTrajectory()
                .forward(FORWARD_FIELD_TILES, FieldTile)
                .withName("Move Forward to Spike Marks")
                .addTask();

        double mul = spikeMark == Direction.FORWARD ? 0 : spikeMark == Direction.LEFT ? 1 : -1;
        makeTrajectory()
                .turn(TURN_ANGLE_DEG * mul, Degrees)
                .withName("Rotate to Spike Mark")
                .addTask();

        addTask(new AlignToContourTask(Seconds.of(3), drive, teamProp, new PIDController(0.25, 0, 0)));

        addTask(arm.deltaTask(ARM_DELTA));
        addTask(claws.openTask(DualServos.ServoSide.RIGHT));
        addTask(arm.deltaTask(-ARM_DELTA));
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }
}
