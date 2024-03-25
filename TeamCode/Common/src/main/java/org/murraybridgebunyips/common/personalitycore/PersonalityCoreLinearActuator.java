package org.murraybridgebunyips.common.personalitycore;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.tasks.ContinuousTask;
import org.murraybridgebunyips.bunyipslib.tasks.RunTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.NoTimeoutTask;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

import java.util.function.DoubleSupplier;

/**
 * Vertical motion motor controller for the GLaDOS/Wheatley robot.
 *
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreLinearActuator extends BunyipsSubsystem {
    private static final double HOLDING_POWER = 1.0;
    private static final double MOVING_POWER = 0.7;
    private DcMotorEx motor;
    private double power;
    private boolean lockout;

    /**
     * @param motor the motor to control as the linear actuator
     */
    public PersonalityCoreLinearActuator(DcMotorEx motor) {
        if (!assertParamsNotNull(motor)) return;
        this.motor = motor;
        // Assumes arm is down locked upon activation
        // If possible it would be beneficial to integrate limit switches
        if (motor == null) return;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(motor.getCurrentPosition());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Move the actuator with the controller joystick. Should be a default task.
     *
     * @param y the y value of the controller
     * @return a task to move the actuator
     */
    public Task joystickControlTask(DoubleSupplier y) {
        return new ContinuousTask(() -> setPower(-y.getAsDouble()), this, false).withName("JoystickControlTask");
    }

    public PersonalityCoreLinearActuator setPower(double p) {
        power = Range.clip(p, -1.0, 1.0);
        return this;
    }

    /**
     * Set the power of the actuator.
     *
     * @param p the power to set
     * @return a task to set the power
     */
    public Task setPowerTask(double p) {
        return new RunTask(() -> setPower(p), this, false).withName("SetPowerTask");
    }

    /**
     * Run the actuator for a certain amount of time.
     *
     * @param p    the power to run at
     * @param time the time to run for in seconds
     * @return a task to run the actuator
     */
    public Task runForTask(double p, double time) {
        return new Task(time, this, true) {
            @Override
            public void init() {
            }

            @Override
            public void periodic() {
                power = p;
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

    /**
     * Set the position of the actuator.
     *
     * @param targetPosition the position to set
     * @return a task to set the position
     */
    public Task gotoTask(int targetPosition) {
        return new NoTimeoutTask(this, true) {
            @Override
            public void init() {
                lockout = true;
            }

            @Override
            public void periodic() {
                motor.setTargetPosition(targetPosition);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.setPower(MOVING_POWER);
            }

            @Override
            public void onFinish() {
                lockout = false;
            }

            @Override
            public boolean isTaskFinished() {
                return !motor.isBusy();
            }
        }.withName("RunToPositionTask");
    }

    /**
     * Home the actuator.
     *
     * @return a task to home the actuator
     */
    public Task homeTask() {
        // TODO
        return new RunTask(() -> {});
    }

    @Override
    protected void periodic() {
        if (lockout) {
            opMode.addTelemetry("Linear Actuator: MOVING to % ticks", motor.getCurrentPosition());
            return;
        }
        if (power == 0.0) {
            // Hold arm in place
            motor.setTargetPosition(motor.getCurrentPosition());
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(HOLDING_POWER);
        } else {
            // Move arm in accordance with the user's input
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPower(power);
        }
        opMode.addTelemetry("Linear Actuator: % at % ticks", power == 0.0 ? "HOLDING" : "MOVING", motor.getCurrentPosition());
    }
}
