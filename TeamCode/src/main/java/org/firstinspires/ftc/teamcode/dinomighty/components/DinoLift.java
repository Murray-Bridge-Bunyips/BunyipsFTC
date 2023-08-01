package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * DinoMighty lift class.
 */

public class DinoLift extends BunyipsComponent {

    private DcMotor arm;
    private Servo servo;
    private double liftPower;

    public DinoLift(@NonNull BunyipsOpMode opMode, DcMotor arm, Servo servo) {
        super(opMode);
        this.arm = arm;
        this.servo = servo;
    }


    public void armLift(double gamepadPosition){
        liftPower = gamepadPosition;
    }

    // Methods for the claw
    public void clawOpen(){
        frontServo.setPosition(1.0);
        backServo.setPosition(1.0);
        addTelemetry("Claw is Open");
    }

    public void clawClose(){
        frontServo.setPosition(0.0);
        backServo.setPosition(0.0);
        addTelemetry("Claw is Closed");
    }

    public void update(){

        arm.setPower(liftPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Position: " + arm.getCurrentPosition(), false);
    }
}