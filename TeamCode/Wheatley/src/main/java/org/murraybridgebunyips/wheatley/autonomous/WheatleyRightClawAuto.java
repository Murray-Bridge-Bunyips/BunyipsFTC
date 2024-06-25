package org.murraybridgebunyips.wheatley.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Centimeters;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Places pixels on the backboard and parks on the right side of the backboard.
 * Extends off of LeftClaw to optimise.
 *
 * @author Lachlan Paul, 2024
 */
@Autonomous(name = "Right Claw Auto")
public class WheatleyRightClawAuto extends WheatleyLeftClawAuto {
    @Override
    public void park() {
        makeTrajectory()
                .strafeRight(70, Centimeters)
                .forward(30, Centimeters)
                .addTask();
    }
}
