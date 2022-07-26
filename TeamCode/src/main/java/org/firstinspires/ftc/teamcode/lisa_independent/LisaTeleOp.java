package org.firstinspires.ftc.teamcode.lisa_independent;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "<LISA-I> TeleOp")
public class LisaTeleOp extends LinearOpMode {

  @Override
  public void runOpMode() {
    DcMotor leftMotor = hardwareMap.get(DcMotor.class, "Left Motor");
    DcMotor rightMotor = hardwareMap.get(DcMotor.class, "Right Motor");

    double turn = 0;
    double drive = 0;
    double leftPower;
    double rightPower;

    waitForStart();
    leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    if (opModeIsActive()) {
      while (opModeIsActive()) {
        // Motor efficiency is halved as they are very strong
        turn = Range.clip(gamepad1.left_stick_x, -0.5, 0.5);
        drive = gamepad1.right_trigger - gamepad1.left_trigger;
        leftPower = Range.clip(drive + turn, -0.5, 0.5);
        rightPower = Range.clip(drive - turn, -0.5, 0.5);
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
        telemetry.addData("Left Motor Power", leftMotor.getPower());
        telemetry.addData("Right Motor Power", rightMotor.getPower());
        telemetry.update();
      }
    }
  }
}
