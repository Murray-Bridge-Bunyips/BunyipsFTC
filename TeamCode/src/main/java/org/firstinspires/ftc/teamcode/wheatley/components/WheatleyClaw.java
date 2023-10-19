package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Text;

/**
 * Component for the claw
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyClaw extends BunyipsComponent {

    private final Servo leftServo;
    private final Servo rightServo;

    // True = Open
    // False = Shut
    // Both claws are set to predetermined positions on init to avoid problems
    private Boolean leftClawState = false;
    private Boolean rightClawState = false;

    public WheatleyClaw(@NonNull BunyipsOpMode opMode, Servo leftServo, Servo rightServo) {
        super(opMode);
        this.leftServo = leftServo;
        this.rightServo = rightServo;

        // Shuts the claws on init to avoid problems with their associated Boolean variables
        // TODO: Set proper claw values
        leftServo.setPosition(0.0);
        rightServo.setPosition(0.0);
    }

    // Methods for the claw
    // To optimise button usage, each claw only operates off of one button
    // To do this, we use Boolean values to determine whether the claw should be open or shut
    // Claw positions are reset on init to prevent problems

    // TODO: Set proper claw values
    public void leftClaw() {
        if (leftClawState) {
            leftServo.setPosition(0.0);
            leftClawState = false;
        } else {
            leftServo.setPosition(1.0);
            leftClawState = true;
        }
        getOpMode().addTelemetry(Text.format("Left Claw is Open: %b", leftClawState));
    }

    public void rightClaw() {
        if (rightClawState) {
            rightServo.setPosition(0.0);
            rightClawState = false;
        } else {
            rightServo.setPosition(1.0);
            rightClawState = true;
        }
        getOpMode().addTelemetry(Text.format("Right Claw is Open: %b", rightClawState));
    }
}
