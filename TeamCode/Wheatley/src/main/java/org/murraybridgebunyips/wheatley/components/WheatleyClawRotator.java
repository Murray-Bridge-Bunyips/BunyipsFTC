package org.murraybridgebunyips.wheatley.components;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.PivotMotor;
import org.murraybridgebunyips.bunyipslib.tasks.ContinuousTask;
import org.murraybridgebunyips.bunyipslib.tasks.RunTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

import java.util.function.DoubleSupplier;

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
    public static double HOLDING_POWER = 1.0;

    /**
     * Encoder lower limit in degrees
     */
    public static int MIN_DEGREES = 0;

    /**
     * Encoder upper limit in degrees
     */
    public static int MAX_DEGREES = 30;
    private final PivotMotor pivot;
    private int power;

    /**
     * Create a new WheatleyClawRotator
     *
     * @param motor the motor to use as the rotator
     */
    public WheatleyClawRotator(DcMotorEx motor) {
        assertParamsNotNull(motor);
        // Core Hex Motor has 288 ticks per revolution, and we are working with no gear reduction
        pivot = new PivotMotor(motor, 288);
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setTargetPosition(0);
        pivot.track(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setPower(HOLDING_POWER);
    }

    /**
     * Get the current degrees of the claw rotator.
     * @param degrees the current degrees
     * @return the claw rotator
     */
    public Task setDegreesTask(int degrees) {
        return new RunTask(() -> setDegrees(degrees), this, true).withName("SetDegreesTask");
    }

    /**
     * Set a target degrees for the claw rotator.
     *
     * @param degrees encoder ticks
     */
    public void setDegrees(int degrees) {
        power = Range.clip(degrees, MIN_DEGREES, MAX_DEGREES);
    }

    /**
     * Set a target degrees for the claw rotator using a delta.
     * @param gamepadY encoder tick delta, negated for gamepad input
     * @return a task to set the degrees
     */
    public Task setDegreesUsingControllerTask(DoubleSupplier gamepadY) {
        return new ContinuousTask(() -> setDegreesUsingController(gamepadY.getAsDouble()), this, false).withName("SetDegreesUsingControllerTask");
    }

    /**
     * Set a target degrees for the claw rotator using a delta.
     *
     * @param gamepadY encoder tick delta, negated for gamepad input
     */
    public void setDegreesUsingController(double gamepadY) {
        if ((gamepadY > 0 && power <= MIN_DEGREES) || (gamepadY < 0 && power >= MAX_DEGREES)) {
            return;
        }
        power -= gamepadY;
    }

    @Override
    protected void periodic() {
        // TODO: change systems to use power only, and manually set power values if getDegrees() exceeds the upper and lower bounds,
        //  can also use the power == 0.0 RUN_TO_POSITION strategy to ensure the arm does not drop.
        if (MIN_DEGREES > pivot.getDegrees()) {
            power = MIN_DEGREES;
        } else if (pivot.getDegrees() > MAX_DEGREES) {
            power = MAX_DEGREES;
        }

        if (power == 0.0) {
            // Hold the arm in place
            pivot.setTargetPosition(pivot.getCurrentPosition());
            pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pivot.setPower(HOLDING_POWER);
        } else {
            pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pivot.setPower(power);
        }

        pivot.setDegrees(power);
        opMode.addTelemetry("Claw Rotator: % <= % <= % degs", MIN_DEGREES, power, MAX_DEGREES);
    }
}
