package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * DinoMighty drive class.
 */

public class DinoDrive extends BunyipsComponent {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    private double motorPower;

    public DinoDrive(@NonNull BunyipsOpMode opMode, DcMotor frontLeft, DcMotor backLeft, DcMotor frontRight, DcMotor backRight, DcMotor arm) {
        super(opMode);

       // this.left_front will assign to a higher scope
       // Meaning we can access the motors in the component
       this.frontLeft = frontLeft;
       this.backLeft = backLeft;
       this.frontRight = frontRight;
       this.backRight = backRight;
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
        return frontLeft.getCurrentPosition() > target;
    }

    // How I assume the motors work
    // Positive: Forward or Left
    // Negative: Backwards or Right
    // Zero: No movement

    // Updates the components every hardware loop
    public void update(){
        frontLeft.setPower(motorPower);
        backLeft.setPower(motorPower);
        // Right needs to be set to -, because the motor power going in a negative direction is
        // the same as it going backwards, or Right. Right?
        frontRight.setPower(-motorPower);
        backRight.setPower(-motorPower);
    }
}
