package org.firstinspires.ftc.teamcode.bertie_independent;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
// Uses Extended DcMotor class

@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Autonomous(name = "<BERTIE-I> Test All Motor Encoders")
public class TestMotorEncoders extends LinearOpMode {

    @Override
    public void runOpMode() {

        // Map hardware
        DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor"); // Not using
        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
        CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake"); // Not using
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");
        CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right"); // Not using
        CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left"); // Not using

        // Set all motors to forward drive, although we wouldn't do this to move forward
        frontRight.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);

        telemetry.addData("TEST READY", "Initialise to begin test");
        telemetry.addData("WARNING"," ALL MOVEMENT MOTORS WILL ENGAGE WHEN STARTED!!");
        telemetry.addData("!!!", " SET ROBOT ON STABLE STAND BEFORE STARTING");
        telemetry.update();

        waitForStart();

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setPower(1);
        frontLeft.setPower(1);
        backRight.setPower(1);
        backLeft.setPower(1);

    while (opModeIsActive()) {
        telemetry.addData("frontRight Encoder", frontRight.getCurrentPosition());
        telemetry.addData("frontLeft Encoder", frontLeft.getCurrentPosition());
        telemetry.addData("backRight Encoder", backRight.getCurrentPosition());
        telemetry.addData("backLeft Encoder", backLeft.getCurrentPosition());
        telemetry.update();
        if (isStopRequested()) {
            frontRight.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
            backLeft.setPower(0);
            stop();
        }
        }
    }
}
