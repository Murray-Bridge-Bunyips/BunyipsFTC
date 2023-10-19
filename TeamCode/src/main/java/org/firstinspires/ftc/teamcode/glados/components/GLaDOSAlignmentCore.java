package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Theoretical alignment routine for GLaDOS lift mechanism, to automatically align the end effector
 * to be in the correct position for the backdrop.
 */
public class GLaDOSAlignmentCore extends BunyipsComponent {
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
    private final GLaDOSPixelCore arm;

    public GLaDOSAlignmentCore(@NonNull BunyipsOpMode opMode, GLaDOSPixelCore arm) {
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
        if (arm.getPivotPosition() > GLaDOSAlignmentCore.BACKDROP_THRESHOLD) {
            arm.setEndEffectorPosition(calculateEndEffectorOffset());
        } else {
            arm.setEndEffectorPosition(0.0);
        }
    }
}
