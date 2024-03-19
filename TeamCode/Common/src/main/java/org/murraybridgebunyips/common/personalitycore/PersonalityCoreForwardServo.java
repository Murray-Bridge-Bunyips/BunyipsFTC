package org.murraybridgebunyips.common.personalitycore;

import static org.murraybridgebunyips.bunyipslib.Text.round;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.tasks.ContinuousTask;
import org.murraybridgebunyips.bunyipslib.tasks.CallbackTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Horizontal CRServo motion for the GLaDOS/Wheatley robot
 *
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreForwardServo extends BunyipsSubsystem {
    private final CRServo servo;
    private double power;

    /**
     * @param servo the servo to control as the forward servo
     */
    public PersonalityCoreForwardServo(CRServo servo) {
        assertParamsNotNull(servo);
        this.servo = servo;
    }

    /**
     * Actuate the servo using a controller
     *
     * @param y the y value of the controller
     * @return the forward servo
     */
    public PersonalityCoreForwardServo actuateUsingController(double y) {
        power = Range.clip(-y, -1.0, 1.0);
        return this;
    }

    /**
     * Actuate the servo using a controller. Should be a default task.
     *
     * @param y the y value of the controller
     * @return a task to actuate the servo
     */
    public Task joystickControlTask(DoubleSupplier y) {
        return new ContinuousTask(() -> actuateUsingController(y.getAsDouble()), this, false).withName("JoystickControlTask");
    }

    /**
     * Actuate the servo using the dpad
     *
     * @param up   whether the dpad is up
     * @param down whether the dpad is down
     * @return the forward servo
     */
    public PersonalityCoreForwardServo actuateUsingDpad(boolean up, boolean down) {
        if (!up && !down) {
            power = 0;
            return this;
        }
        power = up ? 1 : -1;
        return this;
    }

    /**
     * Actuate the servo using the dpad. Should be a default task.
     *
     * @param up   whether the dpad is up
     * @param down whether the dpad is down
     * @return a task to actuate the servo
     */
    public Task dpadControlTask(BooleanSupplier up, BooleanSupplier down) {
        return new ContinuousTask(() -> actuateUsingDpad(up.getAsBoolean(), down.getAsBoolean()), this, false).withName("DpadControlTask");
    }

    /**
     * Set the power of the servo
     *
     * @param power the power to set
     * @return the forward servo
     */
    public PersonalityCoreForwardServo setPower(double power) {
        this.power = Range.clip(power, -1.0, 1.0);
        return this;
    }

    /**
     * Set the power of the servo
     *
     * @param powerTarget the power to set
     * @return a task to set the power
     */
    public Task setPowerTask(double powerTarget) {
        return new CallbackTask(() -> setPower(powerTarget), this, true).withName("SetPowerTask");
    }

    /**
     * Run the servo for a certain amount of time
     *
     * @param seconds the amount of time to run for
     * @param atPower the power to run at
     * @return a task to run the servo
     */
    public Task runForTask(double seconds, double atPower) {
        return new Task(seconds, this, true) {
            @Override
            public void init() {
                // no-op
            }

            @Override
            public void periodic() {
                power = atPower;
                update();
            }

            @Override
            public void onFinish() {
                power = 0;
            }

            @Override
            public boolean isTaskFinished() {
                return false;
            }
        }.withName("RunForTask");
    }

    @Override
    protected void periodic() {
        servo.setPower(power);
        opMode.addTelemetry("Forward Servo: % power", round(servo.getPower(), 1));
    }
}
