package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.PivotMotor;

/**
 * Pixel Movement & Suspension arm mechanism
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSArmCore extends BunyipsComponent {
    /**
     * When the arm is above the ground (where the alignment will not automatically reset to 0.0)
     * the alignment servo should be at this target angle (up from ground anticlockwise).
     */
    protected static final double SERVO_ON_ELEVATION_TARGET_ANGLE = 30.0;
    /**
     * When the arm is below this angle (down from ground clockwise) the alignment servo should
     * lock to 0.0.
     */
    protected static final double SERVO_ON_ELEVATION_DOWN_LOCK_THRESHOLD_ANGLE = 10.0;
    private GLaDOSAlignmentCore alignmentController;
    private GLaDOSServoCore servoController;
    private GLaDOSLinearSliderCore sliderController;

    public GLaDOSArmCore(@NonNull BunyipsOpMode opMode, PivotMotor rotator, DcMotor extensionRunner, Servo alignment, Servo left, Servo right, GLaDOSAlignmentCore.Mode alignmentMode) {
        super(opMode);
        if (rotator != null && extensionRunner != null) {
            sliderController = new GLaDOSLinearSliderCore(opMode, rotator, extensionRunner);
        } else {
            getOpMode().addTelemetry("! COM_FAULT: LinearSlider failed to instantiate due to missing hardware", true);
        }
        if (alignment != null) {
            alignmentController = new GLaDOSAlignmentCore(opMode, alignment, alignmentMode, SERVO_ON_ELEVATION_TARGET_ANGLE, SERVO_ON_ELEVATION_DOWN_LOCK_THRESHOLD_ANGLE);
        } else {
            getOpMode().addTelemetry("! COM_FAULT: AlignmentCore failed to instantiate due to missing hardware", true);
        }
        if (left != null && right != null) {
            servoController = new GLaDOSServoCore(opMode, left, right);
        } else {
            getOpMode().addTelemetry("! COM_FAULT: Servos failed to instantiate due to missing hardware", true);
        }
    }

    public GLaDOSServoCore getServoController() {
        return servoController;
    }

    public GLaDOSLinearSliderCore getSliderController() {
        return sliderController;
    }

    public GLaDOSAlignmentCore getAlignmentController() {
        return alignmentController;
    }

    public void update() {
        // Push stateful changes from any changes to these controllers
        if (servoController != null)
            servoController.update();

        if (sliderController != null) {
            sliderController.update();
            // Alignment servo is fully handled automatically by the alignment controller
            if (alignmentController != null) {
                alignmentController.update(sliderController.getAngle());
            }
        }
    }
}
