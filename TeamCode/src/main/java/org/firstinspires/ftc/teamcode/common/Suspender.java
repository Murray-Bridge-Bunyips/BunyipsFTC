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

    /**
     * Return the current status of the Suspender, which is determined by the position of the arm
     * @return The status of the Suspender mechanism
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Determine if the arm is currently stowed
     * @return a boolean representing whether the arm is moving or not
     */
    public boolean isMoving() {
        return status == Status.CLOSING || status == Status.OPENING || status == Status.RETRACTING || status == Status.EXTENDING;
    }

    /**
     * Represents all possible states the Suspender mechanism can be in
     */
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
