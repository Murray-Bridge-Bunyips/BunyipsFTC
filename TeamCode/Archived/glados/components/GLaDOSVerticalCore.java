package org.murraybridgebunyips.common.personalitycore;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.murraybridgebunyips.bunyipslib.BunyipsComponent;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;

public class GLaDOSVerticalCore extends BunyipsComponent {
    private static final double PWR = 1.0; // Full power since we are holding the whole robot
    private static final int MAX_POSITION = 300;
    private final DcMotorEx motor;
    private int targetPosition;

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
        targetPosition = (int) (percentage / 100 * MAX_POSITION);
    }

    public void actuateUsingController(double gamepadPosition) {
        targetPosition -= gamepadPosition * 10;
    }

    public void update() {
        motor.setTargetPosition(targetPosition);
    }
}
