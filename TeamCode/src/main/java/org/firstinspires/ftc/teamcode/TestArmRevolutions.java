package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// @Disabled
@SuppressWarnings("unused")
@Autonomous(name = "Test - Arm Revolutions")
public class TestArmRevolutions extends LinearOpMode {
    public void runOpMode() {
        double armPosition;

        DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor");
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
        CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");
        CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
        CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");

        telemetry.addData("Ready", "Hardware mapped and variables declared. ENSURE ARM MOTOR IS READY TO MOVE FROM RESTING POSITION. Continue when ready.");
        telemetry.update();

        waitForStart();

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (opModeIsActive()) {
            armMotor.setTargetPosition(1800);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armPosition = armMotor.getCurrentPosition();
            while (armPosition < 1800) {
                armMotor.setPower(0.25);
                armPosition = armMotor.getCurrentPosition();
                telemetry.addData("armMotor armPosition", armPosition);
                telemetry.update();
            }
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setTargetPosition(100);
            armPosition = armMotor.getCurrentPosition();
            while (armPosition > 105) {
                armMotor.setPower(-0.25);
                armPosition = armMotor.getCurrentPosition();
                telemetry.addData("armMotor armPosition", armPosition);
                telemetry.update();
            }
        }
        stop();
    }
}
