package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * DinoMighty lift class.
 */

public class DinoLift extends BunyipsComponent {

    private DcMotor arm;
    private Servo frontServo;
    private Servo backServo;
    private double liftPower;

    public DinoLift(@NonNull BunyipsOpMode opMode, DcMotor arm, Servo frontServo, Servo backServo) {
        super(opMode);
        this.arm = arm;
        this.backServo = backServo;
        this.frontServo = frontServo;
    }


    public void armLift(double gamepadPosition){
        liftPower = gamepadPosition;
    }

    // Methods for the claw
    public void clawOpen(){
        frontServo.setPosition(0.5);
        backServo.setPosition(0.5);
        getOpMode().addTelemetry("Claw is Open", false);
    }

    public void clawClose(){
        frontServo.setPosition(1.0);
        backServo.setPosition(0.5);
        getOpMode().addTelemetry("Claw is Closed", false);
    }

    public void update(){

        arm.setPower(liftPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Position: " + arm.getCurrentPosition(), false);
    }
}