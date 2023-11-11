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
    private final Servo leftServo;
    private final Servo rightServo;
    private int armPosition;
    private double armPower;
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
        // For some reason they're like this and I don't feel like fixing it rn
        leftServo.setPosition(0.0);
        rightServo.setPosition(1.0);
    }

    /**
     * Used to set arm to specific position. Made for Autonomous
     *
     * @param position specific position to set the arm to
     */
    public void armLift(int position) {
        armPosition = position;
    }

    /**
     * Used to set arm position when using a gamepad
     *
     * @param gamepadPosition the gamepad stick position to set the arm to
     */
    public void armLiftController(double gamepadPosition) {
        armPower = gamepadPosition;
    }

    public void leftClaw() {
        if (leftClawState) {
            leftServo.setPosition(1.0);
            leftClawState = false;
        } else {
            leftServo.setPosition(0.0);
            leftClawState = true;
        }
        //FIXME: lucas bubner
      //  getOpMode().addTelemetry("Left Claw is Open: %", leftClawState);
    }

    public void rightClaw() {
        if (rightClawState) {
            rightServo.setPosition(0.0);
            rightClawState = false;
        } else {
            rightServo.setPosition(1.0);
            rightClawState = true;
        }
//        getOpMode().addTelemetry("Right Claw is Open: %", rightClawState);
    }

    public void update() {
        if (armPosition != 0) {
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setTargetPosition(armPosition);
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            armPosition = 0;
        }

        arm.setPower(armPower);

        // Can update telemetry functions too
        // The modified telemetry function takes in a value to show on the Driver Station, and
        // whether or not to keep it on the screen upon the next activeLoop.
//        getOpMode().addTelemetry("Lift Arm Position: " + arm.getCurrentPosition());
    }
}
