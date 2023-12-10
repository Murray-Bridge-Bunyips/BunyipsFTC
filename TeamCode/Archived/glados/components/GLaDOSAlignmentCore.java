package org.murraybridgebunyips.bunyipslib.personalitycore;


import static org.murraybridgebunyips.bunyipslib.Text.round;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsComponent;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;

/**
 * Controller for the self-aligning servo mechanism for GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSAlignmentCore extends BunyipsComponent {
    /**
     * Assumes the alignment servo has range 0-1 where 0 == straight down as the arm lets and 1 == on board.
     */
    private final Servo alignment;

    /**
     * Angle at which the alignment servo is trying to align to.
     */
    private double position;

    private Mode mode;

    public GLaDOSAlignmentCore(@NonNull BunyipsOpMode opMode, Servo alignment, Mode defaultMode) {
        super(opMode);
        this.alignment = alignment;
        mode = defaultMode;
    }

    public double getPosition() {
        return position;
    }

    public GLaDOSAlignmentCore setPosition(double position) {
        if (mode == Mode.MANUAL) return this;
        this.position = position;
        return this;
    }

    public GLaDOSAlignmentCore setPositionPercentage(double percentage) {
        if (mode == Mode.MANUAL) return this;
        position = percentage / 100;
        return this;
    }

    /**
     * Manual mode, use controller dpad to adjust alignment servo position.
     *
     * @param up   gamepad2.dpad_up
     * @param down gamepad2.dpad_down
     */
    public GLaDOSAlignmentCore setPositionUsingDpad(boolean up, boolean down) {
        if (mode == Mode.AUTO) return this;
        if (up) {
            position -= 0.01;
        }
        if (down) {
            position += 0.01;
        }
        position = Range.clip(position, 0, 1);
        return this;
    }

    /**
     * Push state to the alignment servo.
     */
    public void update() {
        alignment.setPosition(position);
        if (mode == Mode.AUTO) {
            getOpMode().addTelemetry("Alignment (AUTO): % target", round(alignment.getPosition(), 2));
        } else {
            getOpMode().addTelemetry("Alignment (MAN): % target", round(alignment.getPosition(), 2));
        }
    }

    public Mode getMode() {
        return mode;
    }

    public GLaDOSAlignmentCore setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public enum Mode {
        AUTO,
        MANUAL
    }
}
