package org.firstinspires.ftc.teamcode.proto.config;

import android.annotation.SuppressLint;
import android.text.method.Touch;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class ProtoArm extends BunyipsComponent {

    // Adjust encoder values for lift positions here. Index 0 should always be 0.
    private static final int[] LIFT_POSITIONS = { 0, 200, 400 };

    private int liftIndex = 0;
    private double liftPower;
    public CRServo claw1;
    public CRServo claw2;

    private final DcMotorEx[] motors = new DcMotorEx[2];
    private boolean isCalibrating = false;
    public DcMotorEx arm1;
    public DcMotorEx arm2;
    public TouchSensor limit;


    public ProtoArm(BunyipsOpMode opMode, CRServo claw1, CRServo claw2, DcMotorEx arm1, DcMotorEx arm2, TouchSensor limit) {
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
        claw1.setDirection(CRServo.Direction.FORWARD);
        claw2.setDirection(CRServo.Direction.REVERSE);

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
        // switch breaks for some reason.
        while (!limit.isPressed()) {
            if (motors[0].isOverCurrent() || motors[1].isOverCurrent()) break;
            getOpMode().telemetry.addLine(String.format("ARM IS CALIBRATING... ENCODER VALUES: %d, %d",
                    arm1.getCurrentPosition(),
                    arm2.getCurrentPosition()));
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
    }


    /**
     * Call to open claw system and allow picking up of items (allow servos to reset to 0 degrees)
     */
    public void clawOpen() {
        claw1.setPower(0);
        claw2.setPower(0);
        getOpMode().telemetry.addLine("Claws are opening...");
    }


    /**
     * Call to close claw system and pick up an object (the motors will continuously run, in order
     * to provide a gripping power that will not let the cone go) (CR servos must be programmed
     * with these limits in mind, as the code will simply continue commanding power to the servo)
     */
    public void clawClose() {
        claw1.setPower(1);
        claw2.setPower(1);
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
        if (liftIndex < 0) {
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
     * Call to update desired arm motor speeds
     */
    @SuppressLint("DefaultLocale")
    public void update() {
        // If the arm is in the process of calibration, don't interrupt it
        if (isCalibrating) return;

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
