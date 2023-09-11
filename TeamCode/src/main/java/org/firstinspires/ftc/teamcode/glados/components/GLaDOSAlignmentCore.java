package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class GLaDOSAlignmentCore extends BunyipsComponent {
    private GLaDOSArmCore arm;

    /**
     * Threshold in degrees for the pivot to be considered in the backdrop position.
     */
    private static final double BACKDROP_THRESHOLD = 10.0;

    /**
     * Maximum arm pivot angle in degrees.
     */
    private static final double MAX_PIVOT_ANGLE = 150.0;

    /**
     * Target angle for the end effector to be aligned to.
     */
    private static final double END_EFFECTOR_OFFSET = 30.0;

    public GLaDOSAlignmentCore(@NonNull BunyipsOpMode opMode, GLaDOSArmCore arm) {
        super(opMode);
        this.arm = arm;
    }

    private double calculateEndEffectorOffset() {
        // TODO: Calculate end effector offset based on arm pivot angle
        // Should be done with a linear function that respects the 0-1 range of the servo
        return 0.0;
    }

    public void update() {
        // Assuming the arm encoder is set 0-180 deg for limits
        if (arm.getPivotPosition() > BACKDROP_THRESHOLD) {
            arm.setEndEffectorPosition(calculateEndEffectorOffset());
        } else {
            arm.setEndEffectorPosition(0.0);
        }
    }
}
