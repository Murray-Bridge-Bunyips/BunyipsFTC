package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Second;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.external.pid.PIDController;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.trajectorysequence.TrajectorySequence;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.DriveToPoseTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetTriPositionContourTask;
import org.murraybridgebunyips.bunyipslib.tasks.RoadRunnerTask;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.bunyipslib.tasks.groups.ParallelTaskGroup;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.SpikeMarkBackdropId;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Backdrop Placer Autonomous for Left Parking
 *
 * @author Lucas Bubner, 2024
 */
@Config
@Autonomous(name = "Backdrop Placer (Left Park)")
public class GLaDOSBackdropPlacerLeftPark extends AutonomousBunyipsOpMode implements RoadRunner {
    /**
     * Multiplicative scale for all RoadRunner distances.
     */
    public static double FIELD_TILE_SCALE = 1.5;
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;
    private HoldableActuator arm;
    private Vision vision;
    private GetTriPositionContourTask getTeamProp;
    private StartingPositions startingPosition;
    private DualServos claws;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
        arm = new HoldableActuator(config.arm).withMovingPower(0.5);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);
        vision = new Vision(config.webcam);

        RedTeamProp rtp = new RedTeamProp();
        vision.init(rtp);
        vision.start(rtp);
        getTeamProp = new GetTriPositionContourTask(rtp);

        setOpModes(StartingPositions.use());
        addSubsystems(drive, arm, claws);
        setInitTask(getTeamProp);
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }

    // Set which direction the robot will strafe at the backdrop. Overridden in the right park variant.
    protected RoadRunnerTask afterPixelDropDriveAction(RoadRunnerTrajectoryTaskBuilder builder) {
        return builder
                .strafeLeft(0.95 * FIELD_TILE_SCALE, FieldTile)
                .buildTask();
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null)
            return;

        // Go to backdrop
        Reference<TrajectorySequence> blueLeft = Reference.empty();
        Reference<TrajectorySequence> blueRight = Reference.empty();
        TrajectorySequence redLeft = makeTrajectory()
                .forward(1.8 * FIELD_TILE_SCALE, FieldTiles)
                .strafeRight(2.8 * FIELD_TILE_SCALE, FieldTiles)
                .turn(-Math.PI / 2)
                .strafeRight(1 * FIELD_TILE_SCALE, FieldTile)
                .mirrorToRef(blueRight)
                .build();
        TrajectorySequence redRight = makeTrajectory()
                .lineToLinearHeading(new Pose2d(1 * FIELD_TILE_SCALE, -1 * FIELD_TILE_SCALE, -90.0), FieldTiles, Degrees)
                .mirrorToRef(blueLeft)
                .build();

        startingPosition = (StartingPositions) selectedOpMode.require();
        TrajectorySequence targetSequence = null;
        switch (startingPosition) {
            case STARTING_RED_LEFT:
                targetSequence = redLeft;
                break;
            case STARTING_RED_RIGHT:
                targetSequence = redRight;
                break;
            case STARTING_BLUE_LEFT:
                targetSequence = blueLeft.require();
                break;
            case STARTING_BLUE_RIGHT:
                targetSequence = blueRight.require();
                break;
        }
        assert targetSequence != null;
        makeTrajectory()
                .runSequence(targetSequence)
                .withName("Navigate to Backdrop")
                .addTask();

        makeTrajectory()
                .forward(30, Centimeters)
                .withName("Forward Align Backdrop")
                .addTask();

        // Place pixels and park to the left of the backdrop
        addTask(arm.deltaTask(1500).withName("Deploy Arm"));
        addTask(claws.openTask(DualServos.ServoSide.BOTH).withName("Drop Pixels"));
        addTask(new WaitTask(Seconds.of(1)).withName("Wait for Pixels"));
        addTask(new ParallelTaskGroup(
                afterPixelDropDriveAction(makeTrajectory()),
                arm.deltaTask(-1500)
        ).withName("Stow and Move to Park"));

        makeTrajectory()
                .forward(0.98 * FIELD_TILE_SCALE, FieldTiles)
                .setAccelConstraint(atAcceleration(0.1, FieldTiles.per(Second).per(Second)))
                .withName("Finish Park")
                .addTask();
    }

    @Override
    protected void onStart() {
        int id = SpikeMarkBackdropId.get(getTeamProp.getPosition(), startingPosition);
        AprilTagMetadata aprilTag = AprilTagGameDatabase.getCenterStageTagLibrary().lookupTag(id);
        VectorF targetPos = aprilTag.fieldPosition;
        // TODO: Pose thingo
//        targetPos.add(new VectorF(-5, 0, 0));

        addTaskAtIndex(1, new DriveToPoseTask(Seconds.of(5), drive,
                new Pose2d(targetPos.get(0), targetPos.get(1), 0),
                new PIDController(8, 0, 0),
                new PIDController(8, 0, 0),
                new PIDController(8, 0, 0)
        ));
    }
}
