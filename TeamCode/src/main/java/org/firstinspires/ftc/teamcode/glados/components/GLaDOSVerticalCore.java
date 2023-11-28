package org.firstinspires.ftc.teamcode.glados.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class GLaDOSVerticalCore extends BunyipsComponent {
    private static final double PWR = 1.0; // Full power since we are holding the whole robot
    private static final int MAX_POSITION = 300;
    private int targetPosition;
    private final DcMotorEx motor;
    public GLaDOSVerticalCore(@NonNull BunyipsOpMode opMode, DcMotorEx suspenderActuator) {
        super(opMode);
        motor = suspenderActuator;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(motor.getCurrentPosition());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setTargetPosition(int targetPosition) {
        if (targetPosition < 0 || targetPosition > MAX_POSITION) return;
        this.targetPosition = targetPosition;
    }

    public void setTargetPercentage(double percentage) {
        if (percentage < 0 || percentage > 100) return;
        this.targetPosition = (int) (percentage / 100 * MAX_POSITION);
    }

    public void actuateUsingController(double gamepadPosition) {
        targetPosition -= gamepadPosition * 10;
    }

    public void update() {
        motor.setTargetPosition(targetPosition);
    }
}
