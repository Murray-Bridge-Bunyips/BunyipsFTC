package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * DinoMighty drive class.
 */

public class DinoDrive extends BunyipsComponent {

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private double motorPower;

    public DinoDrive(@NonNull BunyipsOpMode opMode, DcMotor left, DcMotor right) {
        super(opMode);

       // this.leftMotor will assign to a higher scope
       // Meaning we can access the motors in the component
       this.leftMotor = left;
       this.rightMotor = right;
    }

    public void run(double power) {
        // Set the power of the motors to the given power.
        this.motorPower = power;
    }

    public void stop() {
        // Stop the motors by setting power to zero
        this.motorPower = 0;
    }

    // Not exactly sure how this works?
    // I assume it has something to do with autonomous
    public boolean isAtPosition(double target) {
        return leftMotor.getCurrentPosition() > target;
    }

    // How I assume the motors work
    // Positive: Forward or Left
    // Negative: Backwards or Right
    // Zero: No movement

    // Updates the components every hardware loop
    public void update(){
        leftMotor.setPower(motorPower);
        // Right needs to be set to -, because the motor power going in a negative direction is
        // the same as it going backwards, or Right. Right?
        rightMotor.setPower(-motorPower);
    }
}
