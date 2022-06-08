package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@SuppressWarnings("unused")
@TeleOp(name = "<> LUCAS BUBNER - Freight Frenzy TeleOp")
public class BunyipsFreightFrenzyTeleOp extends LinearOpMode {

    // Primary thread that is ran from the Driver Station.
  @Override
  public void runOpMode() {
    double armPosition;

    // Map hardware
      DcMotor armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
      DcMotor frontRight = hardwareMap.get(DcMotor.class, "Front Right");
      DcMotor backRight = hardwareMap.get(DcMotor.class, "Back Right");
      CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
      DcMotor frontLeft = hardwareMap.get(DcMotor.class, "Front Left");
      DcMotor backLeft = hardwareMap.get(DcMotor.class, "Back Left");
      CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
      CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");

    // Coded by Lucas Bubner
    armPosition = armMotor.getCurrentPosition();
    frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    waitForStart();
    while (opModeIsActive()) {
      // Standardised movements
      spinIntake.setPower(gamepad2.left_stick_y);
      frontLeft.setPower((gamepad1.left_stick_y - gamepad1.right_stick_x / 2) - gamepad1.left_stick_x / 2);
      backLeft.setPower((gamepad1.left_stick_y + gamepad1.right_stick_x / 2) - gamepad1.left_stick_x / 2);
      frontRight.setPower(gamepad1.left_stick_y + gamepad1.right_stick_x / 2 + gamepad1.left_stick_x / 2);
      backRight.setPower((gamepad1.left_stick_y - gamepad1.right_stick_x / 2) + gamepad1.left_stick_x / 2);
      // Carousel movements
      if (gamepad2.a || gamepad2.b) {
        carouselRight.setPower(gamepad2.a ? 1 : -1);
        carouselLeft.setPower(gamepad2.a ? -1 : 1);
      } else {
        carouselRight.setPower(0);
        carouselLeft.setPower(0);
      }
      // Arm movements
      armPosition = (int) (armPosition + -gamepad2.right_stick_y * 14);
      armMotor.setTargetPosition((int) armPosition < 1850 ? (int) armPosition : armMotor.getTargetPosition());
      telemetry.addData("ArmPosition", armPosition);
      telemetry.addData("TargetPosition", armMotor.getTargetPosition());
      telemetry.addData("CurrentPosition", armMotor.getCurrentPosition());
      telemetry.update();
      armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      if (armMotor.getTargetPosition() > armMotor.getCurrentPosition() + 5 || armMotor.getTargetPosition() < armMotor.getCurrentPosition() - 5) {
        armMotor.setPower(armMotor.getTargetPosition() > armMotor.getCurrentPosition() ? 0.25 : -0.25);
      } else {
        armMotor.setPower(0);
      }
    }
  }
}
