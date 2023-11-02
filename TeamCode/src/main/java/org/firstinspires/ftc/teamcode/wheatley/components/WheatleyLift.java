package org.firstinspires.ftc.teamcode.wheatley.components;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Text;

/**
 * Component for the arm used for lifting pixels
 *
 * @author Lachlan Paul, 2023
 */

public class WheatleyLift extends BunyipsComponent {

    private final DcMotor clawArm;
    private double armPower;
    private final double clawPower;

    private final Servo leftServo;
    private final Servo rightServo;

    // True = Open
    // False = Shut
    // Both claws are set to predetermined positions on init to avoid problems
    private Boolean leftClawState = false;
    private Boolean rightClawState = false;

    public WheatleyLift(@NonNull BunyipsOpMode opMode, Double armPower, DcMotor clawArm, double clawPower, Servo leftServo, Servo rightServo) {
        super(opMode);
        this.clawArm = clawArm;
        this.clawPower = clawPower;
        this.armPower = armPower;
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

    public void update() {

        clawArm.setPower(clawPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
        getOpMode().addTelemetry("Lift Arm Position: " + clawArm.getCurrentPosition(), false);
    }
}