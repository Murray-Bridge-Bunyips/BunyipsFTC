package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "LUCASBUBNERFreightFrenzyDriveTrain (Blocks to Java)")
public class LUCASBUBNERFreightFrenzyDriveTrain extends LinearOpMode {

  private DcMotor ArmMotor;
  private DcMotor FrontRight;
  private DcMotor BackRight;
  private CRServo SpinIntake;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  private CRServo CarouselRight;
  private CRServo CarouselLeft;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    int armPosition;

    ArmMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
    FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
    BackRight = hardwareMap.get(DcMotor.class, "Back Right");
    SpinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
    FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
    BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
    CarouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
    CarouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");

    // Coded by Lucas Bubner
    armPosition = ArmMotor.getCurrentPosition();
    FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    BackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    SpinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    ArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    waitForStart();
    while (opModeIsActive()) {
      // Standardised movements
      SpinIntake.setPower(gamepad2.left_stick_y);
      FrontLeft.setPower((gamepad1.left_stick_y - gamepad1.right_stick_x / 2) - gamepad1.left_stick_x / 2);
      BackLeft.setPower((gamepad1.left_stick_y + gamepad1.right_stick_x / 2) - gamepad1.left_stick_x / 2);
      FrontRight.setPower(gamepad1.left_stick_y + gamepad1.right_stick_x / 2 + gamepad1.left_stick_x / 2);
      BackRight.setPower((gamepad1.left_stick_y - gamepad1.right_stick_x / 2) + gamepad1.left_stick_x / 2);
      // Carousel movements
      if (gamepad2.a || gamepad2.b) {
        CarouselRight.setPower(gamepad2.a ? 1 : -1);
        CarouselLeft.setPower(gamepad2.a ? -1 : 1);
      } else {
        CarouselRight.setPower(0);
        CarouselLeft.setPower(0);
      }
      // Arm movements
      armPosition = (int) (armPosition + -gamepad2.right_stick_y * 14);
      ArmMotor.setTargetPosition(armPosition < 1850 ? armPosition : ArmMotor.getTargetPosition());
      telemetry.addData("ArmPosition", armPosition);
      telemetry.addData("TargetPosition", ArmMotor.getTargetPosition());
      telemetry.addData("CurrentPosition", ArmMotor.getCurrentPosition());
      telemetry.update();
      ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      if (ArmMotor.getTargetPosition() > ArmMotor.getCurrentPosition() + 5 || ArmMotor.getTargetPosition() < ArmMotor.getCurrentPosition() - 5) {
        ArmMotor.setPower(ArmMotor.getTargetPosition() > ArmMotor.getCurrentPosition() ? 0.25 : -0.25);
      } else {
        ArmMotor.setPower(0);
      }
    }
  }
}
