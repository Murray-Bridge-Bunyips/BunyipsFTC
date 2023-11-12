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
 * @author Lucas Bubner, 2023
 */

public class WheatleyLift extends BunyipsComponent {
    private final DcMotor arm;
    private final Servo leftServo;
    private final Servo rightServo;
    private int armPosition;
    private double armPower;

    private double leftClawTarget;
    private double rightClawTarget;

    private static final double LS_OPEN = 1.0;
    private static final double LS_CLOSED = 0.0;
    private static final double RS_OPEN = 0.0;
    private static final double RS_CLOSED = 1.0;

    public WheatleyLift(@NonNull BunyipsOpMode opMode, DcMotor arm, Servo leftServo, Servo rightServo) {
        super(opMode);
        this.arm = arm;
        this.leftServo = leftServo;
        this.rightServo = rightServo;

        // Shuts the claws on init to avoid problems with their associated Boolean variables
        // For some reason they're like this and I don't feel like fixing it rn
        leftClawTarget = LS_CLOSED;
        rightClawTarget = RS_CLOSED;
        update();
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
    public void armLiftUsingController(double gamepadPosition) {
        armPower = gamepadPosition / 3;
    }

    public void leftClaw() {
        if (leftServo.getPosition() == LS_OPEN) {
            leftClawTarget = LS_CLOSED;
        } else {
            leftClawTarget = LS_OPEN;
        }
    }

    public void rightClaw() {
        if (rightServo.getPosition() == RS_OPEN) {
            rightClawTarget = RS_CLOSED;
        } else {
            rightClawTarget = RS_OPEN;
        }
    }

    public void update() {
        if (armPosition != 0) {
            arm.setTargetPosition(armPosition);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (arm.isBusy()) {
                return;
            }
            armPosition = 0;
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // This is an attempt at preventing the arm falling from it's own weight when it's at
        // certain angles.
        // Comment out if broken
        if (armPower == 0) {
            arm.setTargetPosition(arm.getCurrentPosition());
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // Will try to hold the arm in place with 25% power
            arm.setPower(0.25);
        } else {
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setPower(armPower);
        }

        leftServo.setPosition(leftClawTarget);
        rightServo.setPosition(rightClawTarget);

        getOpMode().addTelemetry("Left Claw: %", leftClawTarget == LS_OPEN ? "OPEN" : "CLOSED");
        getOpMode().addTelemetry("Right Claw: %", rightClawTarget == RS_OPEN ? "OPEN" : "CLOSED");
    }
}