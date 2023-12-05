package org.murraybridgebunyips.bunyipslib.personalitycore.submodules;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.murraybridgebunyips.bunyipslib.BunyipsComponent;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;

/**
 * Vertical motion motor controller for the GLaDOS/Wheatley robot.
 * @author Lucas Bubner, 2023
 */
public class PersonalityCoreManagementRail extends BunyipsComponent {
    private static final double HOLDING_POWER = 0.3;
    private final DcMotorEx motor;
    private double power;

    public PersonalityCoreManagementRail(@NonNull BunyipsOpMode opMode, DcMotorEx motor) {
        super(opMode);
        this.motor = motor;
        // Assumes arm is down locked upon activation
        // If possible it would be beneficial to integrate limit switches
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(motor.getCurrentPosition());
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // Autonomous will never have to use this class, so we will just have TeleOp methods
    // That of course assuming our teammate doesn't stack tens of pixels in the first few seconds,
    // in which case our contributions would be negligible anyways
    // This may change if we want to pick anything up in Auto
    public void actuateUsingController(double y) {
        power = Range.clip(-y, -1.0, 1.0);
    }

    public void setPower(double p) {
        power = Range.clip(p, -1.0, 1.0);
    }

    public void update() {
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
        getOpMode().addTelemetry("Management Rail: % at % ticks", power == 0.0 ? "HOLDING" : "MOVING", motor.getCurrentPosition());
    }
}
