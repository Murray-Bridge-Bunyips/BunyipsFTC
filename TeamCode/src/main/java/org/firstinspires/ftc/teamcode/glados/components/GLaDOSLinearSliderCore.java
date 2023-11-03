package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.GearedPivotMotor;

/**
 * Controller for the linear slider and rotation mechanism for GLaDOS.
 * @author Lucas Bubner, 2023
 */
public class GLaDOSLinearSliderCore extends BunyipsComponent {
    private final GearedPivotMotor rotator;
    private final DcMotor runner;
    private Mode mode = Mode.TRACKING;
    private double targetAngle;
    private double targetLength;
    private double targetPower;

    private static final double RUNNER_POWER = 1.0;
    private static final int RUNNER_MAX_LENGTH = 1000;

    public GLaDOSLinearSliderCore(@NonNull BunyipsOpMode opMode, GearedPivotMotor rotator, DcMotor runner) {
        super(opMode);
        this.rotator = rotator;
        this.runner = runner;
        rotator.setup();
        rotator.track();
        rotator.setPower(RUNNER_POWER);

        // Default mode is set to run on tracking
        // Assumes encoder position is 0 when the arm is fully retracted
        runner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // TODO: Safety features to prevent the arm from going too far up or down
    // Can involve a limit switch similar to Jerry for lower limit

    /**
     * Sets the target angle of the rotator.
     * @param targetAngle degrees
     */
    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    /**
     * Delta the target angle of the rotator.
     * @param gamepad_y gamepad2_left_stick_y or similar
     */
    public void setTargetAngleUsingController(double gamepad_y) {
        targetAngle -= gamepad_y / 2;
    }

    /**
     * Gets the target angle of the rotator.
     * @return degrees
     */
    public double getTargetAngle() {
        return targetAngle;
    }

    /**
     * Gets the current angle of the rotator.
     * @return degrees
     */
    public double getAngle() {
        return rotator.getDegrees();
    }

    /**
     * Set the length of the linear slider.
     * Must be in LOCKING mode to use.
     * @see #setExtrusionPower(double)
     * @param length encoder ticks for the runner
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

    /**
     * Set the power of the linear slider.
     * Must be in TRACKING mode to use.
     * @see #setExtrusionLength(int)
     * @param power power for the runner
     */
    public void setExtrusionPower(double power) {
        if (mode != Mode.TRACKING) {
            // Noop if not in tracking mode
            return;
        }
        targetPower = Range.clip(power, -1, 1);
    }

    /**
     * Send all stateful changes to the hardware.
     */
    public void update() {
        rotator.setDegrees(targetAngle);
        if (mode == Mode.LOCKING) {
            lockingUpdate();
            return;
        }
        trackingUpdate();
    }

    private void lockingUpdate() {
        // TODO
    }

    private void trackingUpdate() {
        // TODO
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

    public Mode getMode() {
        return mode;
    }

    private enum Mode {
        TRACKING,
        LOCKING
    }
}
