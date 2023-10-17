package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

/**
 * Component for the claw
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyClaw extends BunyipsComponent {

    private final Servo frontServo;
    private final Servo backServo;

    public WheatleyClaw(@NonNull BunyipsOpMode opMode, Servo frontServo, Servo backServo) {
        super(opMode);
        this.frontServo = frontServo;
        this.backServo = backServo;
    }

    // Methods for the claw
    // The same song and dance from DinoMighty
    public void clawOpen() {
        frontServo.setPosition(0.5);
        backServo.setPosition(0.5);
        getOpMode().addTelemetry("Claw is Open", false);
    }

    public void clawClose() {
        frontServo.setPosition(1.0);
        backServo.setPosition(0.5);
        getOpMode().addTelemetry("Claw is Closed", false);
    }
}
