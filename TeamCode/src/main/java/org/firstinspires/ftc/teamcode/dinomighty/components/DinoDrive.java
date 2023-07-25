package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class DinoDrive extends BunyipsComponent {

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private double motorPower;

    public DinoDrive(@NonNull BunyipsOpMode opMode, DcMotor left, DcMotor right) {
        super(opMode);

        // Why does it want ; ???
        // I got things to do, I'll be back later
        // TODO: Continue going through the examples

//        // this.leftMotor will assign to a higher scope
//        // Meaning we can access the motors in the component
//        this.leftMotor = left;
//        this.rightMotor = right;
//
//        public void run(double power) {
//            // Set the power of the motors to the given power.
//            this.motorPower = power;
//        }
//
//        public void stop() {
//            // Stop the motors by setting power to zero
//            this.motorPower = 0;
//        }
    }
}
