package org.firstinspires.ftc.teamcode.common;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Class for the paper plane launcher.
 * A whole new file is technically unnecessary, but we wanted to make a class named Cannon
 * <p></p>
 * "Fire in the hole!"
 *
 * @author Lachlan Paul, 2023
 */
public class Cannon extends BunyipsComponent {
    private final Servo prolong;

    // True for when it's ready to launch, false when it has been fired
    private boolean primed;
    public Cannon(@NonNull BunyipsOpMode opMode, Servo prolong) {
        super(opMode);
        this.prolong = prolong;

        // We assume there will always be a paper plane in the cannon at the start of a match
        prolong.setPosition(1.0);
    }

    /**
     * Fire in the hole!
     */
    public void fire() {
        // NOTE: Servos go from 1 to 0, 1 being right as set on the servo programmer and vice versa.
        if (prolong.getPosition() == 1.0) {
            // Fires
            prolong.setPosition(0.0);
            primed = false;
        } else {
            // Resets to initial position
            prolong.setPosition(1.0);
            primed = true;
        }
    }

    public void update() {
        // This is pretty much pointless because the cannon is always fired in the endgame.
        // But.
        getOpMode().addTelemetry("Cannon has been fired: " + !primed);
    }
}
