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
    private double liftPower;

    public DinoLift(@NonNull BunyipsOpMode opMode, DcMotor arm) {
        super(opMode);
        this.arm = arm;
    }


    public void armLift(double gamepadPosition){
        liftPower = gamepadPosition;
    }

    public void update(){

        arm.setPower(liftPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Position: " + arm.getCurrentPosition(), false);
    }
}