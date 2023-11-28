package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

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

    public GLaDOSArmCore(@NonNull BunyipsOpMode opMode, Servo leftPixel, Servo rightPixel, Servo pixelAlignment) {
        super(opMode);
        if (NullSafety.assertComponentArgs(opMode, GLaDOSServoCore.class, leftPixel, rightPixel))
            servoController = new GLaDOSServoCore(opMode, leftPixel, rightPixel);
        if (NullSafety.assertComponentArgs(opMode, GLaDOSAlignmentCore.class, pixelAlignment))
            alignmentController = new GLaDOSAlignmentCore(opMode, pixelAlignment, GLaDOSAlignmentCore.Mode.AUTO);
    }

    public GLaDOSServoCore getServoController() {
        return servoController;
    }

    public GLaDOSAlignmentCore getAlignmentController() {
        return alignmentController;
    }

    public void update() {
        alignmentController.update();
        servoController.update();
    }
}
