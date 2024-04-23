package org.murraybridgebunyips.common.centerstage.tasks;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.bunyipslib.tasks.groups.SequentialTaskGroup;

/**
 * A task that automates picking up pixels.
 * It rotates back to it's original position, closes the claw, goes down to a certain position,
 * picks up the pixel, and lifts it up slightly so it's isn't dragging
 *
 * @author Lachlan Paul, 2024
 */
public class PickUpPixelTask extends SequentialTaskGroup {
    /**
     * Sequence of events:<br>
     *   Shuts the claws<br>
     *   Moves the arm downwards to a point it should fit in any pixels below it<br>
     *   Opens the claws<br>
     *   Moves the arm up a few cms or so<br>
     * @param actuator the arm
     * @param servos the claws
     */
    public PickUpPixelTask(HoldableActuator actuator, DualServos servos) {
        super(
                // hello I am lachlan paul and this is not lacgkab oayk
                // dont listen to him he is evil lachlan from evil lachlan world and he writes evil lachlan code in the evil lachlan ide on the evil lachlan laptop in the illegal lachlan chair
                servos.openTask(DualServos.ServoSide.BOTH),
                // TODO: Test the gotoTask values cause I'll be honest I have pulled these numbers from unsavoury places
                actuator.homeTask(),
                servos.closeTask(DualServos.ServoSide.BOTH),
                new WaitTask(Seconds.of(1), false),
                actuator.deltaTask(2000)
        );
        // Dota, Dota, Counter-Strike
        // I didn't know what to do, so onwards I went to manually animate the wrangler
    }
}
