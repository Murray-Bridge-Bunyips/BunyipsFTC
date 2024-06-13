package org.murraybridgebunyips.wheatley.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
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

    // TODO: Test this
    protected final int FORWARD_DISTANCE = 100;
    protected final int TO_BOARD_FAR_DISTANCE = 300;
    protected final int TO_BOARD_CLOSE_DISTANCE = 120;
    protected final int PLACING_POSITION = 2500;
    protected MessageTask waitMessage;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new MecanumDrive(
                config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu,
                config.fl, config.fr, config.bl, config.br
        );
        rotator = new HoldableActuator(config.clawRotator);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);

        addSubsystems(drive, rotator, claws);

        waitMessage = new MessageTask(Seconds.of(15), "<style=\"color:red;\">If the robot is not moving DO NOT PANIC, it is waiting for others to move</>");

        setOpModes(StartingPositions.use());
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return drive;
    }

    /**
     * Place pixel and return to home position
     */
    public void placePixel() {
        addTask(rotator.gotoTask(PLACING_POSITION));
        addTask(claws.openTask(DualServos.ServoSide.BOTH));
        addTask(rotator.homeTask());
    }

    /**
     * Parks to the left of the backboard
     */
    public void parkLeft() {
        makeTrajectory()
                .strafeLeft(70, Centimeters)
                .forward(30, Centimeters)
                .addTask();
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) {
            return;
        }

        switch ((StartingPositions) selectedOpMode.require()) {
            // TODO: THIS HAS NOT BEEN TESTED DO NOT USE
            case STARTING_RED_LEFT:
                addTask(waitMessage);
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(-90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_FAR_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkLeft();

                break;

            case STARTING_BLUE_LEFT:
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_CLOSE_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkLeft();

                break;

            case STARTING_RED_RIGHT:
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(-90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_CLOSE_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkLeft();

                break;

            case STARTING_BLUE_RIGHT:
                addTask(waitMessage);
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_FAR_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkLeft();

                break;
        }
    }
}
