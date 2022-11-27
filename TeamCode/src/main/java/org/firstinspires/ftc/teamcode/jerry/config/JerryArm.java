package org.firstinspires.ftc.teamcode.jerry.config;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class JerryArm extends BunyipsComponent {

    private static final int[] LIFT_POSITIONS = { 50, 120, 350, 500, 700 };
    /*
        LIFT_POSITIONS index
        0: Cone obtaining/neutral height
        1: Above cone, allows for drive to position cone under arm
        2: Pole position 1
        3: Pole position 2
        4: Pole position 3
     */

    private int liftIndex = 0;
    private double liftPower;
    public Servo claw1;
    public Servo claw2;

    private final DcMotorEx[] motors = new DcMotorEx[2];
    private boolean isCalibrating, alreadyCalibrated = false;
    public DcMotorEx arm1;
    public DcMotorEx arm2;
    public TouchSensor limit;


    public JerryArm(BunyipsOpMode opMode, Servo claw1, Servo claw2, DcMotorEx arm1, DcMotorEx arm2, TouchSensor limit) {
        super(opMode);
        this.arm1 = arm1;
        this.arm2 = arm2;
        this.claw1 = claw1;
        this.claw2 = claw2;
        this.limit = limit;

        motors[0] = arm1;
        motors[1] = arm2;
        liftPower = 0;

        // Ensure motors exist onboard
        assert claw1 != null && claw2 != null && arm1 != null && arm2 != null;

        // Set directions of motors so they move the correct way
        claw1.setDirection(Servo.Direction.FORWARD);
        claw2.setDirection(Servo.Direction.REVERSE);

        arm1.setDirection(DcMotorEx.Direction.FORWARD);
        arm2.setDirection(DcMotorEx.Direction.REVERSE);

        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        }

        // When the arm is first initialised, calibrate it (as the arm may not already be zeroed)
        // This will also set the motors to their correct mode.
        this.liftCalibrate();
    }


    /**
     * Use the configured hard-limit switch to calibrate and zero out the encoders. Will also use
     * over-current detection in the event the hard-limit switch isn't working correctly.
     * This will lockout the update method while it runs.
     */
    @SuppressLint("DefaultLocale")
    public void liftCalibrate() {
        // Lock down the instance to have full control over the arm
        isCalibrating = true;
        alreadyCalibrated = true;

        for (DcMotorEx motor : motors) {
            // Set motors to power-only mode and let them run
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // Set the motor power to something low as we will be descending and don't want to damage
            // any gears or parts with excessive force. Gravity should do the heavy work.
            motor.setPower(-0.1);

            // Core Hex motors have a stall amperage of 4.4 amps, so we set it slightly lower
             motor.setCurrentAlert(4.3, CurrentUnit.AMPS);
        }

        // Now we wait for the limit switch to be hit, or if there is a sudden stall current in the
        // arm motors, which means either the button failed or the motors had already triggered
        // the bounds detection. Either way works and it won't hurt to use both, incase the limit
        // switch breaks for some reason. Press right bumper to cancel the loop.
        // Using reversed operation as pressing results in the limit switch reporting false
        while (limit.isPressed() && !getOpMode().gamepad2.right_bumper) {
             if (motors[0].isOverCurrent() || motors[1].isOverCurrent()) break;
            getOpMode().telemetry.addLine(String.format("ARM IS CALIBRATING... ENCODER VALUES: %d, %d",
                    arm1.getCurrentPosition(),
                    arm2.getCurrentPosition()));
            getOpMode().telemetry.update();
        }

        for (DcMotorEx motor : motors) {
            // Finally, we reset the motors and we are now zeroed out again.
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(0);
        }

        // Prevent 'slingshotting' the arm back to whatever position the arm was originally in,
        // then return arm control permission back to the instance.
        liftIndex = 0;
        isCalibrating = false;
        getOpMode().telemetry.addLine("Arm has been calibrated.");
    }


    /**
     * Call to open claw system and allow picking up of items (allow servos to swing to open)
     */
    public void clawOpen() {
        claw1.setPosition(0.0);
        claw2.setPosition(1.0);
        getOpMode().telemetry.addLine("Claws are opening...");
    }


    /**
     * Call to close claw system and pick up an object by setting the target position to the closed
     * position. Using Servo mode as opposed to CRServo mode.
     */
    public void clawClose() {
        claw1.setPosition(0.0);
        claw2.setPosition(0.0);
        getOpMode().telemetry.addLine("Claws are closing...");
    }


    /**
     * Lift arm motor up by one index of the LIFT_POSITIONS index
     */
    public void liftUp() {
        liftIndex++;
        if (liftIndex > LIFT_POSITIONS.length - 1) {
            liftIndex = LIFT_POSITIONS.length - 1;
        }
        for (DcMotorEx motor : motors) {
            motor.setTargetPosition(LIFT_POSITIONS[liftIndex]);
        }
    }


    /**
     * Lift arm motor down by one index of the LIFT_POSITIONS index
     */
    public void liftDown() {
        liftIndex--;
        if (liftIndex <= 0) {
            liftIndex = 0;
        }
        for (DcMotorEx motor : motors) {
            motor.setTargetPosition(LIFT_POSITIONS[liftIndex]);
        }
    }


    /**
     * Set arm motor to index 0 of the LIFT_POSITIONS index (lowest position), then calls an
     * automatic calibration of the motors by zeroing out the encoders at the limit switch.
     */
    public void liftReset() {
        liftIndex = 0;
        this.liftCalibrate();
    }


    /**
     * Manually control arm speed by setting the position manually through variable input
     * Try not to call this method, and instead use the Up/Down methods. Should only be used
     * for manual debugging of the arm motors.
     */
    public void liftControl(double offset) {
        offset *= 10;
        int positions = (arm1.getCurrentPosition() + arm2.getCurrentPosition()) / 2;
        for (DcMotorEx motor : motors) {
            motor.setTargetPosition((int) (positions - offset));
        }
    }


    /**
     * Set arm power for positional movements
     * @param power desired power
     */
    public void liftSetPower(double power) {
        liftPower = power;
    }


    /**
     * Manually set arm position index
     * @param pos Arm position index in LIFT_POSITIONS
     */
    public void liftSetPosition(int pos) {
        // Avoid positions that are outside of the length of the array itself
        if (pos < 0 || pos >= LIFT_POSITIONS.length) return;
        liftIndex = pos;
    }

    /**
     * Call to update desired arm motor speeds
     */
    @SuppressLint("DefaultLocale")
    public void update() {
        // If the arm is in the process of calibration, don't interrupt it
        if (isCalibrating) return;

        // To make sure we don't accidentally get stuck in a loop of infinite calibration
        // Also zeroes out the encoders if we hit the switch.
        if (!limit.isPressed() && !alreadyCalibrated) liftCalibrate();
        if (limit.isPressed()) alreadyCalibrated = false;

        getOpMode().telemetry.addLine(String.format("Arms (pos1, pos2, index): %d, %d, %s",
                                                    arm1.getCurrentPosition(),
                                                    arm2.getCurrentPosition(),
                                                    liftIndex));

        for (DcMotorEx motor : motors) {
            motor.setPower(liftPower);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
}
