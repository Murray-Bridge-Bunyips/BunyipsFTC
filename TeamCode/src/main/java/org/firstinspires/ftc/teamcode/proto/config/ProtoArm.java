package org.firstinspires.ftc.teamcode.proto.config;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class ProtoArm extends BunyipsComponent {

    private static final int[] LIFT_POSITIONS = {0, 100, 500, 1000, 1300};

    private int liftIndex = 0;
    private double liftPower;
    public CRServo claw;
    public DcMotorEx arm;

//    public ProtoArm(BunyipsOpMode opMode, CRServo claw, DcMotorEx arm) {
    public ProtoArm(BunyipsOpMode opMode, CRServo claw) {
        super(opMode);
        this.claw = claw;
//        this.arm = arm;
        claw.setDirection(CRServo.Direction.REVERSE);
//        arm.setDirection(DcMotorEx.Direction.FORWARD);
//        arm.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
//        arm.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        arm.setTargetPosition(0);
//        liftPower = 0;
    }

    /**
     * Set claw motor speed
     * @param speed desired speed (-1, 0, 1)
     */
    public void clawRun(double speed) {
        claw.setPower(Range.clip(speed, -1, 1));
    }

    /**
     * Lift arm motor up by one index of the LIFT_POSITIONS index
     */
    public void liftUp() {
        liftIndex++;
        if (liftIndex > LIFT_POSITIONS.length - 1) {
            liftIndex = LIFT_POSITIONS.length - 1;
        }
        arm.setTargetPosition(LIFT_POSITIONS[liftIndex]);
    }


    /**
     * Lift arm motor down by one index of the LIFT_POSITIONS index
     */
    public void liftDown() {
        liftIndex--;
        if (liftIndex < 0) {
            liftIndex = 0;
        }
        arm.setTargetPosition(LIFT_POSITIONS[liftIndex]);
    }


    /**
     * Set arm motor to index 0 of the LIFT_POSITIONS index (lowest position)
     */
    public void liftReset() {
        liftIndex = 0;
        arm.setTargetPosition(LIFT_POSITIONS[0]);
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
        getOpMode().telemetry.addLine(String.format("Arm Position: %d", arm.getCurrentPosition()));
        arm.setPower(liftPower);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}
