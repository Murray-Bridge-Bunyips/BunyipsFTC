package org.murraybridgebunyips.wheatley.autonomous.arm;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.tasks.RoadRunnerTask;

/**
 * Places pixels on the backboard and parks on the right side of the backboard.
 * Extends off of LeftClaw to optimise.
 *
 * @author Lachlan Paul, 2024
 */
@Autonomous(name = "Right Claw Auto")
public class WheatleyRightClawAuto extends WheatleyLeftClawAuto {
    protected int STRAFE_TO_BACKBOARD = -50;

    @Override
    public RoadRunnerTask park(RoadRunnerTrajectoryTaskBuilder builder) {
        return builder
                .strafeRight(70, Centimeters)
                .forward(60, Centimeters)
                .buildTask();
    }
}
