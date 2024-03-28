package org.murraybridgebunyips.common;

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
 * Controls the claw arm.
 *
 * @author Lachlan Paul, 2024
 * @author Lucas Bubner, 2024
 */
@Config
public class ClawRotator extends BunyipsSubsystem {
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
    public static int MAX_DEGREES = 90;

    /**
     * Lower power clamp
     */
    public static double LOWER_POWER = -0.2;

    /**
     * Upper power clamp
     */
    public static double UPPER_POWER = 0.33;

    private PivotMotor pivot;
    private double power;
    private boolean lockout;

    /**
     * Create a new ClawRotator
     *
     * @param motor the motor to use as the rotator
     */
    public ClawRotator(DcMotorEx motor) {
        if (!assertParamsNotNull(motor)) return;
        // Core Hex Motor has 288 ticks per revolution, and we are working with no gear reduction
        pivot = new PivotMotor(motor, 288);
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setTargetPosition(0);
        pivot.track(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setPower(HOLDING_POWER);
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
     * Set the current power of the claw rotator.
     * @param targetPower the current targetPower
     * @return the task
     */
    public Task setPowerTask(double targetPower) {
        return new RunTask(() -> setPower(targetPower), this, true).withName("SetDegreesTask");
    }

    /**
     * Set a target power for the claw rotator.
     * @param gamepadY target power, negated for gamepad input
     * @return a task to set the power
     */
    public Task setPowerUsingControllerTask(DoubleSupplier gamepadY) {
        return new ContinuousTask(() -> setPower(-gamepadY.getAsDouble()), this, false).withName("SetDegreesUsingControllerTask");
    }

    /**
     * Set the degrees target for the claw rotator, which will run until it reaches the target.
     * @param degrees the target degrees
     * @return the task
     */
    public Task setDegreesTask(double degrees) {
        return new Task(2.0, this, true) {
            @Override
            protected void init() {
                lockout = true;
                pivot.setDegrees(degrees);
                pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                pivot.setPower(1.0);
            }

            @Override
            protected void periodic() {
                // no-op
            }

            @Override
            protected void onFinish() {
                lockout = false;
                pivot.holdCurrentPosition(HOLDING_POWER);
            }

            @Override
            protected boolean isTaskFinished() {
                return false;
            }
        }.withName("SetDegreesTask");
    }

    /**
     * Home the rotator by timeout of 1 second.
     * @return the task
     */
    public Task homeTask() {
        return new Task(1.0, this, true) {
            @Override
            protected void init() {
                lockout = true;
                pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                pivot.setPower(LOWER_POWER);
            }

            @Override
            protected void periodic() {
                // no-op
            }

            @Override
            protected void onFinish() {
                lockout = false;
                pivot.reset();
                pivot.holdCurrentPosition(HOLDING_POWER);
            }

            @Override
            protected boolean isTaskFinished() {
                // Based on timeout
                return false;
            }
        }.withName("HomeTask");
    }

    @Override
    protected void periodic() {
        if (lockout) return;

        if ((pivot.getDegrees() < MIN_DEGREES && power < 0.0) || (pivot.getDegrees() > MAX_DEGREES && power > 0.0))
            power = 0.0;

        if (power == 0.0) {
            // Hold the arm in place
            pivot.holdCurrentPosition(HOLDING_POWER);
        } else {
            // Use user inputs
            pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pivot.setPower(power);
        }

        opMode.addTelemetry("Claw Rotator: % <= % <= % degs, % pwr", MIN_DEGREES, pivot.getDegrees(), MAX_DEGREES, power);
    }
}
