package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Controller for the self-aligning servo mechanism for GLaDOS.
 * @author Lucas Bubner, 2023
 */
public class GLaDOSAlignmentCore extends BunyipsComponent {
    private final Servo alignment;

    /**
     * Angle at which the alignment servo is trying to align to when the arm is up.
     */
    private double targetAngle;

    /**
     * Angle at which the rotator is considered to be in the up position and the alignment servo
     * can begin aligning to the targetAngle.
     */
    private double downLockThresholdAngle;

    public GLaDOSAlignmentCore(@NonNull BunyipsOpMode opMode, Servo alignment, double targetAngle, double downLockThresholdAngle) {
        super(opMode);
        this.alignment = alignment;
        this.targetAngle = targetAngle;
        this.downLockThresholdAngle = downLockThresholdAngle;
    }

    public void setTargetAngle(double targetAngle) {
        this.targetAngle = targetAngle;
    }

    public double getTargetAngle() {
        return targetAngle;
    }

    public void setDownLockThresholdAngle(double downLockThresholdAngle) {
        this.downLockThresholdAngle = downLockThresholdAngle;
    }

    public double getDownLockThresholdAngle() {
        return downLockThresholdAngle;
    }

    /**
     * Updates the alignment servo in response to the current angle of the arm.
     * @param theta degrees of the arm
     */
    public void update(double theta) {
        // TODO
    }
}
