package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Component for the arm used for lifting pixels
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyLift extends BunyipsComponent {

    private final DcMotor arm;
    private double armPower;
    private final Servo leftServo;
    private final Servo rightServo;

    // True = Open
    // False = Shut
    // Both claws are set to predetermined positions on init to avoid problems
    private boolean leftClawState;
    private boolean rightClawState;

    public WheatleyLift(@NonNull BunyipsOpMode opMode, DcMotor arm, Servo leftServo, Servo rightServo) {
        super(opMode);
        this.arm = arm;
        this.leftServo = leftServo;
        this.rightServo = rightServo;

        // Shuts the claws on init to avoid problems with their associated Boolean variables
        // TODO: Set proper claw values
        leftServo.setPosition(0.0);
        rightServo.setPosition(0.0);
    }

    public void armLift(double gamepadPosition) {
        armPower = gamepadPosition;
    }

    public void leftClaw() {
        if (leftClawState) {
            leftServo.setPosition(0.0);
            leftClawState = false;
        } else {
            leftServo.setPosition(1.0);
            leftClawState = true;
        }
        getOpMode().addTelemetry("Left Claw is Open: %b", leftClawState);
    }

    public void rightClaw() {
        if (rightClawState) {
            rightServo.setPosition(0.0);
            rightClawState = false;
        } else {
            rightServo.setPosition(1.0);
            rightClawState = true;
        }
        getOpMode().addTelemetry("Right Claw is Open: %b", rightClawState);
    }

    public void update() {

        arm.setPower(armPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Arm Position: " + arm.getCurrentPosition());
    }
}
