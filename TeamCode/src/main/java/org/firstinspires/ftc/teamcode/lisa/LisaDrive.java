package org.firstinspires.ftc.teamcode.lisa;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.common.BunyipsController;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class LisaDrive extends BunyipsController {
    final private DcMotorEx left;
    final private DcMotorEx right;

    private double leftPower;
    private double rightPower;

    public LisaDrive(BunyipsOpMode opMode, DcMotorEx left, DcMotorEx right) {
        super(opMode);

        this.left = left;
        this.right = right;
        leftPower = 0;
        rightPower = 0;
    }

    public void setPower(double left, double right) {
        leftPower = -left;
        rightPower = -right;
    }

    public void setToBrake() {
        left.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void setToFloat() {
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setEncoder(boolean encoder) {
        if (encoder) {
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        } else {
            left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    @SuppressLint("DefaultLocale")
    public void update() {
        left.setPower(leftPower);
        right.setPower(rightPower);
        getOpMode().telemetry.addLine(String.format("%d, %d", left.getCurrentPosition(), right.getCurrentPosition()));
    }
}
