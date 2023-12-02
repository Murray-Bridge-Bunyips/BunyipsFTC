package org.firstinspires.ftc.team15215.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;

import org.murraybridgebunyips.ftc.bunyipslib.BunyipsComponent;
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.ftc.bunyipslib.While;

/**
 * Controller for the horizontal servo mechanism for GLaDOS.
 */
public class GLaDOSHorizontalCore extends BunyipsComponent {
    private CRServo servo;
    private double power;
    private final While secondsTimeout = new While(
            () -> true, // Relying on While timeout defined by setTimeout()
            () -> servo.setPower(power),
            () -> servo.setPower(0),
            1 // Will be set dynamically
    );

    public GLaDOSHorizontalCore(@NonNull BunyipsOpMode opMode, CRServo pixelMotion) {
        super(opMode);
        servo = pixelMotion;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setPowerPercentage(double percentage) {
        power = percentage / 100;
    }

    public void stop() {
        power = 0;
    }

    public void fullPower() {
        power = 1;
    }

    public void moveFor(double seconds, double power) {
        secondsTimeout.setTimeout(seconds);
        this.power = power;
        secondsTimeout.engage();
    }

    public void update() {
        if (secondsTimeout.run()) return;
        servo.setPower(power);
    }
}
