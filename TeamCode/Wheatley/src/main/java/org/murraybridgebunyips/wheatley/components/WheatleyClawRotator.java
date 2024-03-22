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
    public static double HOLDING_POWER = 0.3;

    /**
     * Encoder lower limit in degrees
     */
    public static int MIN_DEGREES = 0;

    /**
     * Encoder upper limit in degrees
     */
    public static int MAX_DEGREES = 30;

    /**
     * Lower power clamp
     */
    public static double LOWER_POWER = -0.2;

    /**
     * Upper power clamp
     */
    public static double UPPER_POWER = 0.2;

    private final PivotMotor pivot;
    private double power;

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
     * Set the current power of the claw rotator.
     * @param targetPower the current targetPower
     * @return the task
     */
    public Task setPowerTask(double targetPower) {
        return new RunTask(() -> setPower(targetPower), this, true).withName("SetDegreesTask");
    }

    /**
     * Set a target power for the claw rotator.
     *
     * @param targetPower target power
     */
    public void setPower(double targetPower) {
        power = Range.clip(targetPower, LOWER_POWER, UPPER_POWER);
    }

    /**
     * Set a target power for the claw rotator.
     * @param gamepadY target power, negated for gamepad input
     * @return a task to set the power
     */
    public Task setPowerUsingControllerTask(DoubleSupplier gamepadY) {
        return new ContinuousTask(() -> setPowerUsingController(gamepadY.getAsDouble()), this, false).withName("SetDegreesUsingControllerTask");
    }

    /**
     * Set a target power for the claw rotator.
     *
     * @param gamepadY target power, negated for gamepad input
     */
    public void setPowerUsingController(double gamepadY) {
        power = Range.clip(-gamepadY, LOWER_POWER, UPPER_POWER);
    }

    @Override
    protected void periodic() {
        if ((pivot.getDegrees() < MIN_DEGREES && power < 0.0) || (pivot.getDegrees() > MAX_DEGREES && power > 0.0)) {
            power = 0.0;
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

        opMode.addTelemetry("Claw Rotator: % <= % <= % degs, % pwr", MIN_DEGREES, pivot.getDegrees(), MAX_DEGREES, power);
    }
}
