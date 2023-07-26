package org.firstinspires.ftc.teamcode.dinomighty.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * DinoMighty lift class.
 */

public class DinoLift extends BunyipsComponent{
    
    // Index based lift system, with the lift values stored in an array
    private int[] liftPositions = {0, 100, 200, 300, 400, 500};
    private int liftPositionPointer;

    public DinoLift(@NonNull BunyipsOpMode opMode, DcMotor liftMotor){
        super(opMode);
        this.liftMotor = liftMotor;

        this.liftPositionPointer = 0;
    }

    public void liftUp(){
        // Check if the pointer is exceeding the length of the array
        if (liftPositionPointer >= liftPositions.length) {
            // If it is, set the pointer to the last index of the array
            liftPositionPointer = liftPositions.length - 1;
            return;
        }
        // Increments the pointer
        liftPositionPointer++;
    }

    // The opposite of liftUp
    public void liftDown(){
        if (liftPositionPointer <= 0){
            liftPositionPointer = 0;
        }
        liftPositionPointer--;
    }

    public void update(){
        // Use the array and pointer to determine where the lift should be
        int position = liftPositions[liftPositionPointer];

        // Set the motor to the inputted position
        liftMotor.setTargetPosition(position);
        // Moves the lift to position(?)
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Position: " + position, false);
    }
}