package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.NullSafety;

/**
 * Pixel Movement & Suspension arm mechanism v2
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSArmCore extends BunyipsComponent {
    private GLaDOSServoCore servoController;
    private GLaDOSAlignmentCore alignmentController;
    private GLaDOSVerticalCore verticalController;
    private GLaDOSHorizontalCore horizontalController;

    public GLaDOSArmCore(@NonNull BunyipsOpMode opMode, Servo leftPixel, Servo rightPixel, Servo pixelAlignment, DcMotorEx suspenderActuator, CRServo pixelMotion) {
        super(opMode);
        if (NullSafety.assertComponentArgs(opMode, GLaDOSServoCore.class, leftPixel, rightPixel))
            servoController = new GLaDOSServoCore(opMode, leftPixel, rightPixel);
        if (NullSafety.assertComponentArgs(opMode, GLaDOSHorizontalCore.class, pixelMotion))
            horizontalController = new GLaDOSHorizontalCore(opMode, pixelMotion);
        if (NullSafety.assertComponentArgs(opMode, GLaDOSAlignmentCore.class, pixelAlignment))
            alignmentController = new GLaDOSAlignmentCore(opMode, pixelAlignment, GLaDOSAlignmentCore.Mode.AUTO);
        if (NullSafety.assertComponentArgs(opMode, GLaDOSVerticalCore.class))
            verticalController = new GLaDOSVerticalCore(opMode, suspenderActuator);
    }

    public GLaDOSServoCore getServoController() {
        return servoController;
    }

    public GLaDOSAlignmentCore getAlignmentController() {
        return alignmentController;
    }

    public GLaDOSVerticalCore getVerticalController() {
        return verticalController;
    }

    public GLaDOSHorizontalCore getHorizontalController() {
        return horizontalController;
    }

    public void update() {
        verticalController.update();
        horizontalController.update();
        alignmentController.update();
        servoController.update();
    }
}
