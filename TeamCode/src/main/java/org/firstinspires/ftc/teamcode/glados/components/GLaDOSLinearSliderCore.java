package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.GearedPivotMotor;

/**
 * Controller for the linear slider and rotation mechanism for GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSLinearSliderCore extends BunyipsComponent {
    private static final double RUNNER_POWER = 1.0;
    private static final int RUNNER_MAX_LENGTH = 1000;
    private final GearedPivotMotor rotator;
    private final DcMotor runner;

    private Mode mode = Mode.TRACKING;
    // Use in both modes, rotator angle
    private double targetAngle;
    // For use in tracking mode, extrusion power
    private double targetPower;
    // For use in locking mode, extrusion length
    private int targetLength;

    public GLaDOSLinearSliderCore(@NonNull BunyipsOpMode opMode, GearedPivotMotor rotator, DcMotor runner) {
        super(opMode);
        this.rotator = rotator;
        this.runner = runner;
        rotator.setup();
        rotator.reset();
        rotator.track();
        rotator.setPower(RUNNER_POWER);

        // Assumes arm is resting on the Control Hub, runner will be at 10 degrees
        rotator.setSnapshot(10.0);

        // Default mode is set to run on tracking
        // Assumes encoder position is 0 when the arm is fully retracted
        runner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Send all stateful changes to the hardware.
     */
    public void update() {
        rotator.setDegrees(targetAngle);
        if (mode == Mode.LOCKING) {
            runner.setTargetPosition(targetLength);
            return;
        }
        runner.setPower(targetPower);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        // Adjust modes of the hardware
        switch (mode) {
            case TRACKING:
                runner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                if (runner.getPower() == RUNNER_POWER) {
                    // Safety to ensure the runner doesn't keep running
                    runner.setPower(0);
                }
                break;
            case LOCKING:
                runner.setTargetPosition(runner.getCurrentPosition());
                targetLength = runner.getCurrentPosition();
                runner.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                runner.setPower(RUNNER_POWER);
                break;
        }
    }

    /**
     * Delta the target angle of the rotator.
     *
     * @param gamepad_y gamepad2_left_stick_y or similar
     */
    public void setTargetAngleUsingController(double gamepad_y) {
        targetAngle -= gamepad_y / 2;
    }

    /**
     * Gets the target angle of the rotator.
     *
     * @return degrees
     */
    public double getTargetAngle() {
        return targetAngle;
    }

    /**
     * Sets the target angle of the rotator.
     *
     * @param targetAngle degrees
     */
    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    /**
     * Set the power of the linear slider.
     * Must be in TRACKING mode to use.
     *
     * @param power power for the runner
     * @see #setExtrusionLength(int)
     */
    public void setExtrusionPower(double power) {
        if (mode != Mode.TRACKING) {
            // Noop if not in tracking mode
            return;
        }
        targetPower = Range.clip(power, -1, 1);
    }

    /**
     * Gets the current angle of the rotator.
     *
     * @return degrees
     */
    public double getAngle() {
        return rotator.getDegrees();
    }

    /**
     * Set linear slider length to 0.
     */
    public void resetExtrusionLength() {
        setExtrusionLength(0);
    }

    /**
     * Set linear slider length to maximum.
     */
    public void maxExtrusionLength() {
        setExtrusionLength(RUNNER_MAX_LENGTH);
    }

    /**
     * Set the length of the linear slider.
     * Must be in LOCKING mode to use.
     *
     * @param length encoder ticks for the runner
     * @see #setExtrusionPower(double)
     */
    public void setExtrusionLength(int length) {
        if (mode != Mode.LOCKING) {
            // Noop if not in locking mode
            return;
        }
        if (length > RUNNER_MAX_LENGTH) {
            length = RUNNER_MAX_LENGTH;
        }
        if (length < 0) {
            length = 0;
        }
        targetLength = length;
    }

    private enum Mode {
        TRACKING,
        LOCKING
    }
}
