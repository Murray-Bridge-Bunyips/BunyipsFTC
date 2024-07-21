package org.murraybridgebunyips.wheatley.autonomous.arm;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Milliseconds;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.MessageTask;
import org.murraybridgebunyips.bunyipslib.tasks.RoadRunnerTask;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.bunyipslib.tasks.groups.ParallelTaskGroup;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * Places pixels on the backboard and parks on the left side of the backboard.
 *
 * @author Lachlan Paul, 2024
 */
@Autonomous(name = "Left Claw Auto")
public class WheatleyLeftClawAuto extends AutonomousBunyipsOpMode implements RoadRunner {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;
    private HoldableActuator rotator;
    private DualServos claws;
    protected MessageTask waitMessage;

    // All of these values are read in CM
    protected final int FORWARD_DISTANCE = 170;  // This is used when taking the long path, to get under the gate
    protected final int TO_BOARD_FAR_DISTANCE = 300;  // The distance to the board when on the other side of the truss
    protected final int TO_BOARD_CLOSE_DISTANCE = 50;  // The distance to the board when on the close side of the truss
    protected final int ARM_PLACING_POSITION = 2700;  // The position the arm goes to from 0 when placing pixels
    protected int STRAFE_TO_BACKBOARD = 30;  // The distance to strafe when getting back to the backboard
    protected int TURN_ANGLE = 90;  // Angle to turn, is turned to negative when needed

    @Override
    protected void onInitialise() {
        config.init();
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu,
                config.fl, config.fr, config.bl, config.br
        );
        rotator = new HoldableActuator(config.clawRotator).withMovingPower(1);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);

        waitMessage = new MessageTask(Seconds.of(10), "<style=\"color:red;\">If the robot is not moving DO NOT PANIC, it is waiting for others to move</>");

        setOpModes(StartingPositions.use());
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return drive;
    }

    /**
     * Parks to the left of the backboard
     * @param builder builds the path
     * @return the path
     */
    public RoadRunnerTask park(RoadRunnerTrajectoryTaskBuilder builder) {
        return builder
                .strafeLeft(70, Centimeters)
                .forward(60, Centimeters)
                .buildTask();
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        int distanceToGoGoGo = TO_BOARD_FAR_DISTANCE;  // The distance to move when we go(go go) towards the backboard
        boolean takeLongPath = false;

        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.require()) {
            // Since all positions have an almost identical path, we can just swap variables around or run wait tasks
            // based on the chosen direction, instead of four nearly identical auto paths.
            case STARTING_RED_LEFT:
                addTask(waitMessage);
                takeLongPath = true;
                TURN_ANGLE = -TURN_ANGLE;
                break;
            case STARTING_BLUE_LEFT:
                distanceToGoGoGo = TO_BOARD_CLOSE_DISTANCE;
                break;
            case STARTING_RED_RIGHT:
                TURN_ANGLE = -TURN_ANGLE;
                distanceToGoGoGo = TO_BOARD_CLOSE_DISTANCE;
                break;
            case STARTING_BLUE_RIGHT:
                addTask(waitMessage);
                takeLongPath = true;
                break;
        }

        if (takeLongPath) {
            // Used when starting in the position furthest from the backboard, (red left, blue right)
            makeTrajectory()
                    .forward(FORWARD_DISTANCE, Centimeters)
                    .turn(TURN_ANGLE, Degrees)
                    .forward(distanceToGoGoGo, Centimeters)
                    .strafeRight(-STRAFE_TO_BACKBOARD)
                    .addTask();
        } else {
            // Used everywhere else
            makeTrajectory()
                    .forward(1)  // This is so we are out of the way of the truss' legs.
                    .turn(TURN_ANGLE, Degrees)
                    .forward(distanceToGoGoGo)
                    // This will always be strafe right, but depending on the auto, will be negative so we can strafe right.
                    .strafeRight(STRAFE_TO_BACKBOARD)
                    .addTask();
        }

        // Places the pixels and then parks
        addTask(rotator.deltaTask(ARM_PLACING_POSITION));
        addTask(claws.openTask(DualServos.ServoSide.BOTH));
        addTask(new WaitTask(Milliseconds.of(500)));
        addTask(new ParallelTaskGroup(
                park(makeTrajectory()),
                rotator.deltaTask(-ARM_PLACING_POSITION),
                claws.closeTask(DualServos.ServoSide.BOTH)
        ));
    }
}
