package org.murraybridgebunyips.common.personalitycore.submodules;

import static org.murraybridgebunyips.bunyipslib.Text.round;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;

/**
 * Rotation/claw rotational alignment for the GLaDOS/Wheatley robot
 *
 * @author Lucas Bubner, 2023
 * @noinspection MissingJavadoc
 */
public class PersonalityCoreClawRotator extends BunyipsSubsystem {
    private final Servo rotator;

    // Assumes servo is programmed to 0==0 && 1=30
    private final double FACING_DOWN = 0.0;
    private final double FACING_BOARD = 1.0;
    private double target;

    public PersonalityCoreClawRotator(Servo rotator) {
        this.rotator = rotator;
    }

    public PersonalityCoreClawRotator faceBoard() {
        target = FACING_BOARD;
        return this;
    }

    public PersonalityCoreClawRotator faceGround() {
        target = FACING_DOWN;
        return this;
    }

    public PersonalityCoreClawRotator actuateUsingController(double y) {
        target -= y / 12;
        target = Range.clip(target, 0.0, 1.0);
        return this;
    }

    public PersonalityCoreClawRotator setPosition(double target) {
        this.target = Range.clip(target, 0.0, 1.0);
        return this;
    }

    public PersonalityCoreClawRotator setDegrees(double degrees) {
        target = Range.clip(degrees / 30, 0.0, 1.0);
        return this;
    }

    @Override
    public void update() {
        rotator.setPosition(target);
        opMode.addTelemetry("Claw Alignment: % pos (%)", round(target, 1), target == FACING_BOARD ? "FACING_BOARD" : target == FACING_DOWN ? "FACING_DOWN" : "CUSTOM");
    }
}
