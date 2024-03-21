package org.murraybridgebunyips.wheatley.components;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;

/**
 * Controls Wheatley's claw arm.
 *
 * @author Lachlan Paul, 2024
 */
@Config
public class WheatleyClawRotator extends BunyipsSubsystem {
    public static double POWER = 1.0;
    public static int MIN_POSITION = 0;
    public static int MAX_POSITION = 100;
    private final DcMotorEx motor;
    private int targetPosition;

    public void setPosition(int position) {
        targetPosition = Range.clip(position, MIN_POSITION, MAX_POSITION);
    }

    public void setPositionUsingController(double gamepadY) {
        if ((gamepadY > 0 && targetPosition <= MIN_POSITION) || (gamepadY < 0 && targetPosition >= MAX_POSITION)) {
            return;
        }
        targetPosition -= gamepadY;
    }

    public WheatleyClawRotator(DcMotorEx motor) {
        assertParamsNotNull(motor);
        this.motor = motor;
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(POWER);
    }

    @Override
    protected void periodic() {
        motor.setTargetPosition(targetPosition);
        opMode.addTelemetry("Claw Rotator: % <= % <= % ticks", MIN_POSITION, targetPosition, MAX_POSITION);
    }
}
