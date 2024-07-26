package org.murraybridgebunyips.glados.autonomous.l5;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Second;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.ANGLED_INITIAL_FORWARD_DIST_FT;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_FORWARD_DIST_CM;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_FORWARD_INITIAL_FORWARD_DIST_FT;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_LEFT_TURN_DEG;
import static org.murraybridgebunyips.glados.autonomous.l4.GLaDOSSpikeMarkPlacerAutonomous.M_RIGHT_TURN_DEG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
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
import org.murraybridgebunyips.bunyipslib.tasks.DriveToPoseTask;
import org.murraybridgebunyips.bunyipslib.tasks.DynamicTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetTriPositionContourTask;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.bunyipslib.tasks.groups.ParallelTaskGroup;
import org.murraybridgebunyips.bunyipslib.vision.AprilTagPoseEstimator;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.SpikeMarkBackdropId;
import org.murraybridgebunyips.common.centerstage.vision.BlueTeamProp;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Non-composite refined Ultimate Preload OpMode. This is to fix the shortcomings of the composite OpModes which was
 * developed rapidly in order to fit within limited testing time. Compared to the composite OpModes, this OpMode will
 * be more aware of where the Spike Mark is, adding extra routes to avoid displacing a placed Purple Pixel.
 * <p>
 * This Autonomous will place the Purple Pixel on the left claw on the appropriate vision-detected spike mark, navigate
 * to the backboard, place the Yellow Pixel on the appropriate side, then park to the left. A right park variant exists
 * which extends this class to provide a right park. This OpMode is accomplished with the help of RoadRunner and the AprilTag
 * Pose Estimator.
 * <p>
 * This class operates an effective total of 12 OpModes per parking position, to a total of 24 OpModes in one implementation.
 *
 * @author Lucas Bubner, 2024
 */
@Autonomous(name = "Ultimate Preload (Purple on Left, Yellow on Right, Left Park)", group = "L5")
public class GLaDOSUltimatePreloadLeftPark extends AutonomousBunyipsOpMode implements RoadRunner {
    /**
     * Multiplicative scale for all RoadRunner distances.
     */
    public static double FIELD_TILE_SCALE = 1.5;
    /**
     * X offset to DriveToPose AprilTag in inches
     */
    public static float APRILTAG_FORWARD_OFFSET = -9.0f;
    /**
     * Y offset to DriveToPose AprilTag in inches
     */
    public static float APRILTAG_SIDE_OFFSET = 0;
    /**
     * Position delta (in ticks) of the arm extension at backboard
     */
    public static int ARM_DELTA_BACKDROP = 1600;
    /**
     * Whether a heading estimate is also used from AprilTag data.
     */
    public static boolean USING_HEADING_ESTIMATE = true;
    /**
     * Arm to ground from stow in ticks.
     * */
    public static int ARM_DELTA_GROUND = 2000;
    /**
     * Strafe left distance for left park.
     */
    public static double PARK_LEFT_DISTANCE_FIELD_TILES = 0.85;
    /**
     * Strafe right distance for right park. Although this constant is unused in this particular OpMode, the override
     * will set it to use this constant instead. These constants are different as they seem to be scaled strangely.
     */
    public static double PARK_RIGHT_DISTANCE_FIELD_TILES = 0.9;

    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private HoldableActuator arm;
    private DualServos claws;
    private Direction spikeMark;
    private VectorF backdropPose;
    private StartingPositions startingPosition;
    private DualDeadwheelMecanumDrive drive;
    private Vision vision;
    private AprilTag aprilTag;
    private ColourThreshold teamProp;
    private GetTriPositionContourTask getTeamProp;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
        arm = new HoldableActuator(config.arm).withMovingPower(0.5);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);
        vision = new Vision(config.webcam);

        aprilTag = new AprilTag();
        AprilTagPoseEstimator atpe = new AprilTagPoseEstimator(aprilTag, drive)
                .setHeadingEstimate(USING_HEADING_ESTIMATE)
                .setCameraOffset(config.robotCameraOffset);
        onActiveLoop(atpe);

        setOpModes(StartingPositions.use());

        getTeamProp = new GetTriPositionContourTask();
        setInitTask(getTeamProp);
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }

    // Set which direction the robot will strafe at the backdrop. Overridden in the right park variant.
    protected Direction getParkingDirection() {
        return Direction.LEFT;
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null)
            return;

        startingPosition = (StartingPositions) selectedOpMode.require();
        // Facing FORWARD from the starting position as selected
        setPose(startingPosition.getPose());

        // Approach Spike Marks
        addTask(new DynamicTask(
                () -> new ParallelTaskGroup(
                    arm.gotoTask(ARM_DELTA_GROUND).withName("Extend Arm"),
                    makeTrajectory()
                            .forward(spikeMark == Direction.FORWARD ? M_FORWARD_INITIAL_FORWARD_DIST_FT : ANGLED_INITIAL_FORWARD_DIST_FT, FieldTile)
                            .withName("Move Forward to Spike Marks")
                            .buildTask()
                )
        ).withName("Move to Spike Marks"));

        // Align arm to Spike Mark, need to defer this task until runtime as this data is not available until then
        addTask(new DynamicTask(() -> {
            switch (spikeMark) {
                case FORWARD:
                    return makeTrajectory()
                            .forward(M_FORWARD_DIST_CM, Centimeters)
                            .withName("Push to Spike Mark")
                            .buildTask();
                case LEFT:
                case RIGHT:
                    return makeTrajectory()
                            .turn(spikeMark == Direction.LEFT ? M_LEFT_TURN_DEG : M_RIGHT_TURN_DEG, Degrees)
                            .withName("Rotate to Spike Mark")
                            .buildTask();
                default:
                    throw new IllegalStateException("impossible state");
            }
        }));

        // Place Purple Pixel (loaded left)
        addTask(claws.openTask(DualServos.ServoSide.LEFT).withName("Open Left Claw"));
        addTask(arm.deltaTask(-ARM_DELTA_GROUND).withName("Retract Arm"));

        // Go to backdrop from current position
        // We do not know where the robot is in terms of a relative space, so these pathways need to be
        // in a global inferred context, accomplished by deferring construction to runtime

//        Reference<TrajectorySequence> blueRight = Reference.empty();
//        TrajectorySequence redLeft = makeTrajectory()
//                .setScale(FIELD_TILE_SCALE)
//                .forward(1.8, FieldTiles)
//                .strafeRight(2.8, FieldTiles)
//                .turn(-Math.PI / 2)
//                .strafeRight(1, FieldTile)
//                .mirrorToRef(blueRight)
//                .build();
//        TrajectorySequence redRight = makeTrajectory()
//                .lineToLinearHeading(startingPosition.getPose()
//                        .plus(unitPose(new Pose2d(1, 1, -90), FieldTiles, Degrees, FIELD_TILE_SCALE)))
//                .build();
//        TrajectorySequence blueLeft = makeTrajectory()
//                .lineToLinearHeading(startingPosition.getPose()
//                        .plus(unitPose(new Pose2d(1, -1, 90), FieldTiles, Degrees, FIELD_TILE_SCALE)))
//                .build();
        addTask(new DynamicTask(() -> {
            // TODO: backdrop global pose trajectory to also avoid spike mark placed
            return null;
        }).withName("Navigate to Backdrop"));

        // Backdrop pose will be provided after onStart() and has been deferred until runtime
        addTask(new DynamicTask(
                () -> new DriveToPoseTask(Seconds.of(5), drive,
                        new Pose2d(backdropPose.get(0), backdropPose.get(1), 0),
                        new PIDController(0.1, 0, 0),
                        new PIDController(0.1, 0, 0),
                        new PIDController(4, 0, 0))
        ));

        // Place pixels and park to the left of the backdrop
        addTask(arm.deltaTask(ARM_DELTA_BACKDROP).withName("Deploy Arm"));
        addTask(claws.openTask(DualServos.ServoSide.BOTH).withName("Drop Pixels"));
        addTask(new WaitTask(Seconds.of(1)).withName("Wait for Pixels"));

        // Park
        double parkDistance = getParkingDirection() == Direction.LEFT
                ? PARK_LEFT_DISTANCE_FIELD_TILES * FIELD_TILE_SCALE
                : -PARK_RIGHT_DISTANCE_FIELD_TILES * FIELD_TILE_SCALE;
        addTask(new ParallelTaskGroup(
                makeTrajectory().strafeLeft(parkDistance).buildTask(),
                arm.deltaTask(-ARM_DELTA_BACKDROP)
        ).withName("Stow and Move to Park"));

        makeTrajectory()
                .forward(0.98 * FIELD_TILE_SCALE, FieldTiles)
                .setVelConstraint(atVelocity(0.1, FieldTiles.per(Second)))
                .withName("Finish Park")
                .addTask();

        // Tasks created, run vision initialisation
        vision.init(aprilTag, teamProp);
        vision.start(teamProp);
        teamProp = startingPosition.isRed() ? new RedTeamProp() : new BlueTeamProp();
        getTeamProp.setProcessor(teamProp);
    }

    @Override
    protected void onStart() {
        spikeMark = getTeamProp.getPosition();
        int id = SpikeMarkBackdropId.get(spikeMark, startingPosition);
        AprilTagMetadata aprilTagDetection = AprilTagGameDatabase.getCenterStageTagLibrary().lookupTag(id);
        if (aprilTagDetection == null) {
            telemetry.log("apriltag not found, seeing tag: %", id);
            return;
        }
        // Supply dynamic constructed information for AprilTag alignment tasks
        backdropPose = aprilTagDetection.fieldPosition;
        // Offset from the tag to the backdrop to not drive directly into the board
        backdropPose.add(new VectorF(APRILTAG_FORWARD_OFFSET, APRILTAG_SIDE_OFFSET, 0));

        vision.stop(teamProp);
        vision.start(aprilTag);
    }
}
