package org.firstinspires.ftc.teamcode.kanga;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsComponent;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class KangaDrive extends BunyipsComponent {
    final private DcMotor left;
    final private DcMotor right;

    private double leftPower;
    private double rightPower;

    public KangaDrive(BunyipsOpMode opMode, DcMotor left, DcMotor right) {
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
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setToFloat() {
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setEncoder(boolean encoder) {
        if (encoder) {
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @SuppressLint("DefaultLocale")
    public void update() {
        left.setPower(leftPower);
        right.setPower(rightPower);
        getOpMode().telemetry.addLine(String.format("Left Encoder: %d, Right Encoder: %d",
        left.getCurrentPosition(), right.getCurrentPosition()));
    }
}
