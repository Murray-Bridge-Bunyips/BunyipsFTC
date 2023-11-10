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
        prolong.setPosition(90.0);
    }

    /**
     * Fire in the hole!
     * Or cannonball in the hole, I suppose.
     */
    public void fire() {
        if (prolong.getPosition() == 90) {
            prolong.setPosition(0.0);
            primed = false;
        } else {
            prolong.setPosition(90.0);
            primed = true;
        }
    }

    public void update() {
        // This is pretty much pointless because the cannon is always fired in the endgame.
        // But.
        getOpMode().addTelemetry("Cannon has been fired: " + !primed);
    }
}
