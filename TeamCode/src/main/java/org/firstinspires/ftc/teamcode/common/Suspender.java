package org.firstinspires.ftc.teamcode.common;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Common component to manage arm used in Centerstage to suspend the robot during the endgame
 *
 * This extends and rotates the arm, while ensuring the arm can only be extended if the arm is at a
 * certain position, and that it can only be retracted once it has returned to that position
 *
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */

public class Suspender extends BunyipsComponent{

    enum RotationStates {
        STOWED,
        NOTSTOWED // TODO: Think of a better name, figure out why I suck at naming things
    }

    enum ExtensionStates {
        RETRACTED,
        EXTENDED
    }

    private RotationStates rotationStatus = RotationStates.STOWED;
    private ExtensionStates extensionStatus = ExtensionStates.RETRACTED;

    private DcMotor rotation;
    private DcMotor extension;

    public Suspender(@NonNull BunyipsOpMode opMode, DcMotor rotation, DcMotor extension) {
        super(opMode);
        this.rotation = rotation;
        this.extension = extension;
    }

    // Basic mockup of what this might look like
    // Basically, whenever the arm is stowed, do not allow arm extension, don't allow the arm to
    // move when extended, etc.

    // FIXME: Figure out why these are errors
    // Maybe it's because it can't read the motor degrees by itself?
    // Or maybe it's because this is a mockup so nothing is set up right just yet
    if (rotation == 90) {
        rotationStatus = RotationStates.NOTSTOWED;
    } else {
        rotationStatus = RotationStates.STOWED;
    }

    if (extension > 1) {
        extensionStatus = ExtensionStates.EXTENDED;
    } else {
        extensionStatus = ExtensionStates.RETRACTED;
    }

}
