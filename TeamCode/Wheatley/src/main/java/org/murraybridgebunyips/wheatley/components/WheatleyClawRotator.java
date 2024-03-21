package org.murraybridgebunyips.wheatley.components;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.PivotMotor;

/**
 * Controls Wheatley's claw arm.
 *
 * @author Lachlan Paul, 2024
 */
@Config
public class WheatleyClawRotator extends BunyipsSubsystem {
    /**
     * The holding power to use
     */
    public static double POWER = 1.0;

    /**
     * Encoder lower limit in degrees
     */
    public static int MIN_DEGREES = 0;

    /**
     * Encoder upper limit in degrees
     */
    public static int MAX_DEGREES = 30;
    private final PivotMotor pivot;
    private int targetDegrees;

    /**
     * Create a new WheatleyClawRotator
     *
     * @param motor the motor to use as the rotator
     */
    public WheatleyClawRotator(DcMotorEx motor) {
        assertParamsNotNull(motor);
        pivot = new PivotMotor(motor, 288, 1);
        pivot.setup(true);
        pivot.setPower(POWER);
    }

    /**
     * Set a target degrees for the claw rotator.
     *
     * @param degrees encoder ticks
     */
    public void setDegrees(int degrees) {
        targetDegrees = Range.clip(degrees, MIN_DEGREES, MAX_DEGREES);
    }

    /**
     * Set a target degrees for the claw rotator using a delta.
     *
     * @param gamepadY encoder tick delta, negated for gamepad input
     */
    public void setDegreesUsingController(double gamepadY) {
        if ((gamepadY > 0 && targetDegrees <= MIN_DEGREES) || (gamepadY < 0 && targetDegrees >= MAX_DEGREES)) {
            return;
        }
        targetDegrees -= gamepadY;
    }

    @Override
    protected void periodic() {
        pivot.setDegrees(targetDegrees);
        opMode.addTelemetry("Claw Rotator: % <= % <= % degs", MIN_DEGREES, targetDegrees, MAX_DEGREES);
    }
}
