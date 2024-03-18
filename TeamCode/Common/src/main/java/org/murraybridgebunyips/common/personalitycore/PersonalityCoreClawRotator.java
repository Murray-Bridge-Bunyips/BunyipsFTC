package org.murraybridgebunyips.common.personalitycore;

import static org.murraybridgebunyips.bunyipslib.Text.round;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.tasks.ContinuousTask;
import org.murraybridgebunyips.bunyipslib.tasks.InstantTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

import java.util.function.DoubleSupplier;

/**
 * Rotation/claw rotational alignment for the GLaDOS/Wheatley robot
 *
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreClawRotator extends BunyipsSubsystem {
    private final Servo rotator;

    // Assumes servo is programmed to 0==0 && 1=30
    private final double FACING_DOWN = 0.0;
    private final double FACING_BOARD = 1.0;
    private double target;

    /**
     * @param rotator the servo to control as the claw rotator
     */
    public PersonalityCoreClawRotator(Servo rotator) {
        assertParamsNotNull(rotator);
        this.rotator = rotator;
    }

    /**
     * Face the board
     *
     * @return the claw rotator
     */
    public PersonalityCoreClawRotator faceBoard() {
        target = FACING_BOARD;
        return this;
    }

    /**
     * Face the board
     *
     * @return a task to face the board
     */
    public Task faceBoardTask() {
        return new InstantTask(this::faceBoard, this, true).withName("FaceBoardTask");
    }

    /**
     * Face the ground
     *
     * @return the claw rotator
     */
    public PersonalityCoreClawRotator faceGround() {
        target = FACING_DOWN;
        return this;
    }

    /**
     * Face the ground
     *
     * @return a task to face the ground
     */
    public Task faceGroundTask() {
        return new InstantTask(this::faceGround, this, true).withName("FaceGroundTask");
    }

    /**
     * Control the rotator using a controller.
     *
     * @param y the y value of the controller
     * @return the claw rotator
     */
    public PersonalityCoreClawRotator actuateUsingController(double y) {
        target -= y / 12;
        target = Range.clip(target, 0.0, 1.0);
        return this;
    }

    /**
     * Control the rotator using a controller. Should be your default task.
     *
     * @param y the y value of the controller
     * @return a task to control the rotator
     */
    public Task joystickControlTask(DoubleSupplier y) {
        return new ContinuousTask(() -> actuateUsingController(y.getAsDouble()), this, false).withName("ControlRotatorTask");
    }

    /**
     * Set the position of the claw
     *
     * @param target the position to set the claw to
     * @return the claw rotator
     */
    public PersonalityCoreClawRotator setPosition(double target) {
        this.target = Range.clip(target, 0.0, 1.0);
        return this;
    }

    /**
     * Set the position of the claw
     *
     * @param targetPos the position to set the claw to
     * @return a task to set the position
     */
    public Task setPositionTask(double targetPos) {
        return new InstantTask(() -> setPosition(targetPos), this, true).withName("SetPositionTask");
    }

    /**
     * Set the degrees of the claw rotation
     *
     * @param degrees the degrees to set the claw to
     * @return the claw rotator
     */
    public PersonalityCoreClawRotator setDegrees(double degrees) {
        target = Range.clip(degrees / 30, 0.0, 1.0);
        return this;
    }

    /**
     * Set the degrees of the claw rotation
     *
     * @param degrees the degrees to set the claw to
     * @return a task to set the degrees
     */
    public Task setDegreesTask(double degrees) {
        return new InstantTask(() -> setDegrees(degrees), this, true).withName("SetDegreesTask");
    }

    @Override
    protected void periodic() {
        rotator.setPosition(target);
        opMode.addTelemetry("Claw Alignment: % pos (%)", round(target, 1), target == FACING_BOARD ? "FACING_BOARD" : target == FACING_DOWN ? "FACING_DOWN" : "CUSTOM");
    }
}
