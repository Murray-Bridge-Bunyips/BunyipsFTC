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
    private Servo prolong;
    private boolean fired;
    public Cannon(@NonNull BunyipsOpMode opMode, Servo prolong) {
        super(opMode);
        this.prolong = prolong;
    }

    /**
     * Fire in the hole!
     */
    public void fire() {
        prolong.setPosition(0.0);
        fired = true;
    }

    public void update() {
        // This is pretty much pointless because the cannon is always fired in the endgame.
        // But.
        getOpMode().addTelemetry("Cannon has been fired: " + fired);
    }
}
