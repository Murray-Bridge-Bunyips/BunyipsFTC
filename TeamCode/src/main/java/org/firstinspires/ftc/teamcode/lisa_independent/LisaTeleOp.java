package org.firstinspires.ftc.teamcode.lisa_independent;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@Disabled // Deprecated in usage of common class OOP
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "<LISA-I> TeleOp")
public class LisaTeleOp extends LinearOpMode {

  @Override
  public void runOpMode() {
    DcMotor leftMotor = hardwareMap.get(DcMotor.class, "Left Motor");
    DcMotor rightMotor = hardwareMap.get(DcMotor.class, "Right Motor");

    double turn;
    double drive;
    double leftPower;
    double rightPower;

    waitForStart();
    leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    while (opModeIsActive()) {
        // Motor efficiency is halved as they are very strong, unless A is pressed which is overdrive or B is pressed which is smoothdrive
        if (gamepad1.a) {
          turn = Range.clip(gamepad1.left_stick_x, -1, 1);
          drive = gamepad1.right_trigger - gamepad1.left_trigger;
          leftPower = Range.clip(drive + turn, -1, 1);
          rightPower = Range.clip(drive - turn, -1, 1);
        } else if (gamepad1.b) {
            turn = Range.clip(gamepad1.left_stick_x, -0.25, 0.25);
            drive = gamepad1.right_trigger - gamepad1.left_trigger;
            leftPower = Range.clip(drive + turn, -0.25, 0.25);
            rightPower = Range.clip(drive - turn, -0.25, 0.25);
        } else {
            turn = Range.clip(gamepad1.left_stick_x, -0.5, 0.5);
            drive = gamepad1.right_trigger - gamepad1.left_trigger;
            leftPower = Range.clip(drive + turn, -0.5, 0.5);
            rightPower = Range.clip(drive - turn, -0.5, 0.5);
        }
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
        telemetry.addData("Left Motor Power", leftMotor.getPower());
        telemetry.addData("Right Motor Power", rightMotor.getPower());
        telemetry.update();
    }

  }
}
