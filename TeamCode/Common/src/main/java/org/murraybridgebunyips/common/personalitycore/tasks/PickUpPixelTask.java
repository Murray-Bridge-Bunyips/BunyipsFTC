package org.murraybridgebunyips.common.personalitycore.tasks;

import org.murraybridgebunyips.bunyipslib.DualServos;
import org.murraybridgebunyips.bunyipslib.tasks.groups.SequentialTaskGroup;
import org.murraybridgebunyips.common.personalitycore.PersonalityCoreLinearActuator;

/**
 * A task that automates picking up pixels.
 * It rotates back to it's original position, closes the claw, goes down to a certain position,
 * picks up the pixel, and lifts it up slightly so it's isn't dragging
 * <p></p>
 * Sequence of events:<br>
 *   Shuts the claws<br>
 *   Moves the arm downwards to a point it should fit in any pixels below it<br>
 *   Opens the claws<br>
 *   Moves the arm up a few cms or so<br>
 *
 * @author Lachlan Paul, 2024
 */
public class PickUpPixelTask extends SequentialTaskGroup {
    public PickUpPixelTask(PersonalityCoreLinearActuator actuator, DualServos servos) {
        super(
                // hello I am lachlan paul and this is not lacgkab oayk
                // dont listen to him he is evil lachlan from evil lachlan world and he writes evil lachlan code in the evil lachlan ide on the evil lachlan laptop in the illegal lachlan chair
                actuator.homeTask(),
                servos.closeServoTask(DualServos.ServoSide.BOTH),
                // TODO: Test the gotoTask values cause I'll be honest I have pulled these numbers from unsavoury places
                actuator.gotoTask(10),
                servos.openServoTask(DualServos.ServoSide.BOTH),
                actuator.gotoTask(3)
        );
        // Dota, Dota, Counter-Strike
        // I didn't know what to do, so onwards I went to manually animate the wrangler
        withSubsystems(actuator, servos, actuator, servos, actuator);
    }
}
