package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


// This code uses fragments from the Advanced Precision Drive, original creation.

/*
*   CODED AND MAINTAINED BY LUCAS BUBNER
*   15215 - MURRAY BRIDGE BUNYIPS
*/

@SuppressWarnings("unused")
@Autonomous(name = "LUCASBUBNERFreightFrenzyAutonomous")
@Disabled // Code declared in work progress: 31/05/22
public class LUCASBUBNERFreightFrenzyAutonomous extends LinearOpMode {

    // Declare variables
    float yawAngle;
    double leftPower;
    double rightPower;

    // Declare unit conversion variables
    final float INCHES_TO_CM = 2.54;
    final float GEAR_RATIO = // TODO: add gear ratio
    final float WHEEL_DIAMETER_INCHES = // TODO: add wheel diameter in inches


    // IMU Calibration Check function
    private boolean imuCalibrated() {
        telemetry.addData("IMU calibration status", imu.getCalibrationStatus());
        telemetry.addData("Gyro calibration", imu.isGyroCalibrated() ? "True" : "False");
        telemetry.addData("System status", imu.getSystemStatus().toString());
    return imu.isGyroCalibrated();
    }

    // getTranslatedDistance function modified from Advanced Precision Drive code
    private double getMovedTranslatedDistance() {


    }

    // Ran on init
    @Override
    public void runOpMode() throws InterruptedException {
  
    // Map hardware
    DcMotor armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
    DcMotor frontRight = hardwareMap.get(DcMotor.class, "Front Right");
    DcMotor backRight = hardwareMap.get(DcMotor.class, "Back Right");
    CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
    DcMotor frontLeft = hardwareMap.get(DcMotor.class, "Front Left");
    DcMotor backLeft = hardwareMap.get(DcMotor.class, "Back Left");
    CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
    CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");
    BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "IMU");

    // Set motor direction
    frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    // IMU initalisation process
    }
}
