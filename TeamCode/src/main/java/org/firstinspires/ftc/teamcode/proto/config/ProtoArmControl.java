package org.firstinspires.ftc.teamcode.proto.config;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class ProtoArmControl extends BunyipsComponent {

    private static final int[] lift_positions = {0, 100, 500, 1000, 1300};
    private int liftIndex = 0;
    private double liftPower;
    public CRServo claw;
    public DcMotorEx arm;

    public ProtoArmControl(BunyipsOpMode opMode, CRServo claw, DcMotorEx arm) {
        super(opMode);
        this.arm = arm;
        this.claw = claw;
        arm.setDirection(DcMotorEx.Direction.FORWARD);
        arm.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        arm.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        arm.setTargetPosition(0);
    }

    public void runClaw(double speed) {
        claw.setPower(speed);
    }

    public void stopClaw() {
        claw.setPower(0);
    }

    public void liftUp() {
        liftIndex++;
        if (liftIndex > lift_positions.length - 1) {
            liftIndex = lift_positions.length - 1;
        }
        arm.setTargetPosition(lift_positions[liftIndex]);
    }

    public void liftDown() {
        liftIndex--;
        if (liftIndex < 0) {
            liftIndex = 0;
        }
        arm.setTargetPosition(lift_positions[liftIndex]);
    }

    public void liftReset() {
        liftIndex = 0;
        arm.setTargetPosition(lift_positions[0]);
    }

    public void setArmPower(double power) {
        liftPower = power;
    }

    @SuppressLint("DefaultLocale")
    public void update() {
        getOpMode().telemetry.addLine(String.format("Arm Position: %d", arm.getCurrentPosition()));
        arm.setPower(liftPower);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}
