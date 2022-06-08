package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

// This code uses fragments from the Advanced Precision Drive, original creation.

/*
 *   CODED AND MAINTAINED BY LUCAS BUBNER
 *   15215 - MURRAY BRIDGE BUNYIPS
 */

@SuppressWarnings("unused")
@Autonomous(name = "<> LUCAS BUBNER - Freight Frenzy Autonomous")
@Disabled // Code declared in work progress: 31/05/22
public class BunyipsFreightFrenzyAutonomous extends LinearOpMode {

    // Declare variables
    double leftPower;
    double rightPower;
    int armPositionCapture;

    // Declare unit conversion and onboard specification variables
    final double INCHES_TO_CM = 2.54;
    final double GEAR_RATIO = 0; // TODO: add gear ratio
    final double WHEEL_DIAMETER_INCHES = 0; // TODO: add wheel diameter in inches

    // Declare Vuforia framework keys

    // Map hardware
    DcMotor armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
    DcMotor frontRight = hardwareMap.get(DcMotor.class, "Front Right");
    DcMotor backRight = hardwareMap.get(DcMotor.class, "Back Right");
    CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
    DcMotor frontLeft = hardwareMap.get(DcMotor.class, "Front Left");
    DcMotor backLeft = hardwareMap.get(DcMotor.class, "Back Left");
    CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
    CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");
    BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "Control Hub IMU");

    // IMU Calibration Check function
    private boolean imuCalibrated() {
        telemetry.addData("IMU calibration status", imu.getCalibrationStatus());
        telemetry.addData("Gyro calibration", imu.isGyroCalibrated() ? "True" : "False");
        telemetry.addData("System status", imu.getSystemStatus().toString());
        return imu.isGyroCalibrated();
    }

    // getTranslatedDistance function modified from Advanced Precision Drive code
    private double getMovedTranslatedDistance() {

        return 0; // TODO: add this function
    }

    // Functions to get data of the environment
    private double getYawAngle() { return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle; }

    // Ran on init
    @Override
    public void runOpMode() {

        // Set motor direction
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // IMU initialisation process
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
        imuParameters.mode = BNO055IMU.SensorMode.IMU;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = true;

        // Init IMU sequence
        imu.initialize(imuParameters);
        telemetry.addData("Status", "IMU successfully initialised");
        telemetry.update();
        sleep(1000);

        // IMU calibration check
        while (!imuCalibrated()) {
            telemetry.addData("Calibrating... ", " If doesn't complete after 3 seconds, move through 90 degree pitch, roll and yaw motions until calibration is complete.");
            telemetry.update();
            // Hold thread until IMU is calibrated
            sleep(250);
        }

        // Calibration complete - await for Driver
        telemetry.addData("IMU", "Ready.");
        telemetry.addData(">", "Ready to initialise code.");
        telemetry.update();

        waitForStart();
        // Ready to start, run code below
    }

    private void moveArmToPosition(int desiredArmPosition) {
        armMotor.setTargetPosition(desiredArmPosition < 1850 ? desiredArmPosition : armMotor.getTargetPosition());
        armPositionCapture = desiredArmPosition;
        while (armMotor.getTargetPosition() > armMotor.getCurrentPosition() + 5 || armMotor.getTargetPosition() < armMotor.getCurrentPosition() - 5) {
            armMotor.setPower(armMotor.getTargetPosition() > armMotor.getCurrentPosition() ? 0.25 : -0.25);
        }
            armMotor.setPower(0);
        // TODO: Have a method in which we can make sure the arm does not fall as the thread will no longer check the arm after the original adjustment
        }
    }

