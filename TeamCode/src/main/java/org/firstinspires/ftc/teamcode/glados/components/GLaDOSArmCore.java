package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.GearedPivotMotor;

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
    private final GLaDOSAlignmentCore alignmentController;
    private final GLaDOSServoCore servoController;
    private final GLaDOSLinearSliderCore sliderController;

    public GLaDOSArmCore(@NonNull BunyipsOpMode opMode, GearedPivotMotor rotator, DcMotor extensionRunner, Servo alignment, Servo left, Servo right) {
        super(opMode);
        sliderController = new GLaDOSLinearSliderCore(opMode, rotator, extensionRunner);
        alignmentController = new GLaDOSAlignmentCore(opMode, alignment, SERVO_ON_ELEVATION_TARGET_ANGLE, SERVO_ON_ELEVATION_DOWN_LOCK_THRESHOLD_ANGLE);
        servoController = new GLaDOSServoCore(opMode, left, right);
    }

    public GLaDOSServoCore getServoController() {
        return servoController;
    }

    public GLaDOSLinearSliderCore getSliderController() {
        return sliderController;
    }

    public void update() {
        // Push stateful changes from any changes to these controllers
        servoController.update();
        sliderController.update();

        // Alignment servo is fully handled automatically by the alignment controller
        alignmentController.update(sliderController.getAngle());
    }
}
