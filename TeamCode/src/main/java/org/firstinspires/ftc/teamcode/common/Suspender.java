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
    private Action action;
    private PivotMotor rotation;
    private DcMotor extension;

    private final While rotationLock = new While(
            () -> {
                return false;
            },
            () -> {

            },
            () -> {

            },
            3
    );

    private final While extensionLock = new While(
            () -> {
                return false;
            },
            () -> {

            },
            () -> {

            },
            3
    );

    public Suspender(@NonNull BunyipsOpMode opMode, PivotMotor rotation, DcMotor extension) {
        super(opMode);
        this.rotation = rotation;
        this.extension = extension;

        // TODO: Might need to set up a limit switch to determine if the arm is downlocked
        // We will assume that the arm is downlocked for now
        status = Status.STOWED;

        rotation.reset();
        rotation.track();

        rotation.setDegrees(0.0);
        rotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
        return false;
    }

    /**
     * Set Suspender from STOWED to RETRACTED
     */
    public void open() {

    }

    /**
     * Set Suspender from RETRACTED to STOWED. Cannot be called if the arm is extended.
     * @see #retract()
     */
    public void close() {

    }

    /**
     * Set Suspender from RETRACTED to EXTENDED. Cannot be called if the arm is stowed.
     * @see #open()
     */
    public void extend() {

    }

    /**
     * Set Suspender from EXTENDED to RETRACTED. Cannot be called if the arm is stowed.
     * @see #open()
     */
    public void retract() {

    }

    /**
     * Reset the Suspender to STOWED
     */
    public void reset() {

    }

    /**
     * Perform all motor updates queued for the Suspender system
     */
    public void update() {

    }

    /**
     * Emergency stop the Suspender system, cancelling all queued motor updates
     */
    public void interrupt() {

    }

    /**
     * Resume Suspender system motor updates after an emergency stop
     */
    public void resume() {

    }

    /**
     * Represents all possible states the Suspender mechanism can be in
     */
    enum Status {
        STOWED,
        RETRACTED,
        EXTENDED,
        // Manual user intervention
        ERROR
    }

    /**
     * Represents all the actions the Suspender mechanism can perform.
     */
    enum Action {
        CLOSING,
        OPENING,
        RETRACTING,
        EXTENDING
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
