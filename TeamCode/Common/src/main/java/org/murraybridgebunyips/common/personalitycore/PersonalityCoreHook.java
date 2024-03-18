package org.murraybridgebunyips.common.personalitycore;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.tasks.ContinuousTask;
import org.murraybridgebunyips.bunyipslib.tasks.InstantTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

/**
 * Suspension hook for the GLaDOS/Wheatley robot.
 *
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreHook extends BunyipsSubsystem {
    private final Servo hook;

    // Assumes a scale range is being used
    private final double EXTENDED = 1.0;
    private final double UPRIGHT = 0.9;
    private final double RETRACTED = 0.0;
    private double target;

    /**
     * Constructs a new PersonalityCoreHook.
     * @param hook the servo to use
     */
    public PersonalityCoreHook(Servo hook) {
        assertParamsNotNull(hook);
        this.hook = hook;
        target = RETRACTED;
        update();
    }

    /**
     * Actuate the hook using a controller
     * @param y the y value of the controller
     * @return the hook
     */
    public PersonalityCoreHook actuateUsingController(double y) {
        target -= y / 5;
        target = Range.clip(target, 0.0, 1.0);
        return this;
    }

    /**
     * Actuate the hook manually using a controller joystick.
     * @param y the y value of the controller
     * @return a task to actuate the hook
     */
    public Task controlHookTask(double y) {
        return new ContinuousTask(() -> actuateUsingController(y), this, false).withName("ControlHookTask");
    }

    /**
     * Set the position of the hook
     * @param target the target position
     * @return the hook
     */
    public PersonalityCoreHook setPosition(double target) {
        this.target = Range.clip(target, 0.0, 1.0);
        return this;
    }

    /**
     * Set the position of the hook
     * @param targetPos the target position
     * @return a task to set the position of the hook
     */
    public Task setPositionTask(double targetPos) {
        return new InstantTask(() -> setPosition(targetPos), this, true).withName("SetPositionTask");
    }

    /**
     * Extend the hook
     * @return the hook
     */
    public PersonalityCoreHook extend() {
        target = EXTENDED;
        return this;
    }

    /**
     * Create a task to extend the hook
     * @return the task
     */
    public Task extendTask() {
        return new InstantTask(() -> extend(), this, true).withName("ExtendHookTask");
    }

    /**
     * Retract the hook
     * @return the hook
     */
    public PersonalityCoreHook retract() {
        target = RETRACTED;
        return this;
    }

    /**
     * Create a task to retract the hook
     * @return the task
     */
    public Task retractTask() {
        return new InstantTask(this::retract, this, true).withName("RetractHookTask");
    }

    /**
     * Upright the hook
     * @return the hook
     */
    public PersonalityCoreHook upright() {
        target = UPRIGHT;
        return this;
    }

    /**
     * Create a task to upright the hook
     * @return the task
     */
    public Task uprightTask() {
        return new InstantTask(this::upright, this, true).withName("UprightHookTask");
    }

    @Override
    protected void periodic() {
        hook.setPosition(target);
        opMode.addTelemetry("Hook: %", target == EXTENDED ? "EXTENDED" : target == RETRACTED ? "RETRACTED" : target == UPRIGHT ? "UPRIGHT" : "CUSTOM_POS");
    }
}
