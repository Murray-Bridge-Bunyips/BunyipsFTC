package org.firstinspires.ftc.teamcode.bertie_independent;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@SuppressWarnings("unused")
// @Disabled // May soon be deprecated by 'bertie' package
@TeleOp(name = "<BERTIE-I> TeleOp Drive")
public class BertieTeleOp extends LinearOpMode {

    // Primary thread that is ran from the Driver Station.
  @Override
  public void runOpMode() {
    // Declare variables
    double armPosition;
    double frontRightPower, backRightPower, backLeftPower, frontLeftPower;

    // Map hardware
    DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor");
    DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
    DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
    CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
    DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
    DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");
    CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
    CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");

    // Coded by Lucas Bubner
    armPosition = armMotor.getCurrentPosition();
    frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    armMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    waitForStart();
    while (opModeIsActive()) {
      // Standardised movements, and accelerated movements if the driver holds down the A button while moving
      spinIntake.setPower(gamepad2.left_stick_y);

      frontLeftPower = gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x;
      backLeftPower = gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x;
      frontRightPower = gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
      backRightPower = gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;

      if (!gamepad1.a) {
        frontLeftPower /= 2;
        backLeftPower /= 2;
        frontRightPower /= 2;
        backRightPower /= 2;
      }

      frontLeft.setPower(frontLeftPower);
      backLeft.setPower(backLeftPower);
      frontRight.setPower(frontRightPower);
      backRight.setPower(backRightPower);

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
      armMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
      if (armMotor.getTargetPosition() > armMotor.getCurrentPosition() + 5 || armMotor.getTargetPosition() < armMotor.getCurrentPosition() - 5) {
        armMotor.setPower(armMotor.getTargetPosition() > armMotor.getCurrentPosition() ? 0.25 : -0.25);
      } else {
        armMotor.setPower(0);
      }
    }
  }
}
