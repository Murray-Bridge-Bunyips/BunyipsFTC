package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * DinoMighty drive class.
 */

public class DinoDrive extends BunyipsComponent {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    private double motorPower;

    public DinoDrive(@NonNull BunyipsOpMode opMode, DcMotor leftFront, DcMotor leftBack, DcMotor rightFront, DcMotor rightBack, DcMotor arm) {
        super(opMode);

       // this.left_front will assign to a higher scope
       // Meaning we can access the motors in the component
       this.leftFront = leftFront;
       this.leftBack = leftBack;
       this.rightFront = rightFront;
       this.rightBack = rightBack;
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
        return leftFront.getCurrentPosition() > target;
    }

    // How I assume the motors work
    // Positive: Forward or Left
    // Negative: Backwards or Right
    // Zero: No movement

    // Updates the components every hardware loop
    public void update(){
        leftFront.setPower(motorPower);
        leftBack.setPower(motorPower);
        // Right needs to be set to -, because the motor power going in a negative direction is
        // the same as it going backwards, or Right. Right?
        rightFront.setPower(-motorPower);
        rightBack.setPower(-motorPower);
    }
}
