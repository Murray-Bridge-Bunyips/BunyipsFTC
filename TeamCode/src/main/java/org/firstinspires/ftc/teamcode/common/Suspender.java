package org.firstinspires.ftc.teamcode.common;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Common component to manage arm used in CENTERSTAGE to suspend the robot during the endgame
 * This extends and rotates the arm, while ensuring the arm can only be extended if the arm is at a
 * certain position, and that it can only be retracted once it has returned to that position.
 *
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */

public class Suspender extends BunyipsComponent {

    private Status status;
    private final DcMotor rotation;
    private final DcMotor extension;

    public Suspender(@NonNull BunyipsOpMode opMode, DcMotor rotation, DcMotor extension) {
        super(opMode);
        this.rotation = rotation;
        this.extension = extension;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isMoving() {
        return status == Status.CLOSING || status == Status.OPENING || status == Status.RETRACTING || status == Status.EXTENDING;
    }

    enum Status {
        STOWED,
        CLOSING,
        OPENING,
        RETRACTED,
        RETRACTING,
        EXTENDED,
        EXTENDING,
        // Manual user intervention
        ERROR
    }

    // Basic mockup of what this might look like
    // Basically, whenever the arm is stowed, do not allow arm extension, don't allow the arm to
    // move when extended, etc.

    // Maybe it's because it can't read the motor degrees by itself?
    // Or maybe it's because this is a mockup so nothing is set up right just yet
//    if (rotation == 90) {
//        rotationStatus = RotationStates.NOTSTOWED;
//    } else {
//        rotationStatus = RotationStates.STOWED;
//    }
//
//    if (extension > 1) {
//        extensionStatus = ExtensionStates.EXTENDED;
//    } else {
//        extensionStatus = ExtensionStates.RETRACTED;
//    }

}
