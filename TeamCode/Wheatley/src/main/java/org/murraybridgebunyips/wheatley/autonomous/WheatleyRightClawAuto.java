package org.murraybridgebunyips.wheatley.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.StartingPositions;

/**
 * Places pixels on the backboard and parks on the right side of the backboard.
 * Extends off of LeftClaw to optimise.
 *
 * @author Lachlan Paul, 2024
 */
@Autonomous(name = "Right Claw Auto")
public class WheatleyRightClawAuto extends WheatleyLeftClawAuto {
    private void parkRight() {
        makeTrajectory()
                .strafeRight(70, Centimeters)
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
                        .turn(90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_FAR_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkRight();

                break;

            case STARTING_BLUE_LEFT:
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(-90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_CLOSE_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkRight();

                break;

            case STARTING_RED_RIGHT:
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_CLOSE_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkRight();

                break;

            case STARTING_BLUE_RIGHT:
                addTask(waitMessage);
                makeTrajectory()
                        .forward(FORWARD_DISTANCE, Centimeters)
                        .turn(-90, Degrees)
                        .addTask();
                makeTrajectory()
                        .forward(TO_BOARD_FAR_DISTANCE, Centimeters)
                        .addTask();

                placePixel();
                parkRight();

                break;
        }
    }
}
