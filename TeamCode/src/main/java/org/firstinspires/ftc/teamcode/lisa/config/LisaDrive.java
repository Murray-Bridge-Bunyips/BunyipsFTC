package org.firstinspires.ftc.teamcode.lisa.config;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.BunyipsController;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class LisaDrive extends BunyipsController {
    final private DcMotorEx left;
    final private DcMotorEx right;
    final private DistanceSensor fws;
    final private ColorSensor dws;

    private double leftPower;
    private double rightPower;

    public LisaDrive(BunyipsOpMode opMode, DcMotorEx left, DcMotorEx right, DistanceSensor fws, ColorSensor dws) {
        super(opMode);

        this.left = left;
        this.right = right;
        this.fws = fws;
        this.dws = dws;
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
        }
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @SuppressLint("DefaultLocale")
    public void update() {
        left.setPower(leftPower);
        right.setPower(rightPower);
        getOpMode().telemetry.addLine(String.format("Left Encoder: %d, Right Encoder: %d\nFWS dist: %s cm\nDWS Red, Green, Blue: %d, %d, %d",
        left.getCurrentPosition(), right.getCurrentPosition(), fws.getDistance(DistanceUnit.CM), dws.red(), dws.green(), dws.blue()));
    }

    public boolean targetPositionReached() {
        return !(left.isBusy() || right.isBusy());
    }

    public void setTargetPosition(double leftDistance, double rightDistance) {

        int newLeftTarget;
        int newRightTarget;
        newLeftTarget = left.getCurrentPosition() + (int) leftDistance;
        newRightTarget =  right.getCurrentPosition() + (int) rightDistance;
        left.setTargetPosition(newLeftTarget);
        right.setTargetPosition(newRightTarget);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
