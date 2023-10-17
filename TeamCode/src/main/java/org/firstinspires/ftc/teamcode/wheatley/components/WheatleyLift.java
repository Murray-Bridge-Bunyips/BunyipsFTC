package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Component for the arm used for lifting pixels
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyLift extends WheatleyArmBase {

    private final DcMotor clawArm;
    private double clawPower;

    public WheatleyLift(@NonNull BunyipsOpMode opMode, Double armPower, DcMotor clawArm) {
        super(opMode, armPower);
        this.clawArm = clawArm;
    }

    public void update() {

        clawArm.setPower(clawPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Arm Position: " + clawArm.getCurrentPosition(), false);
    }
}
