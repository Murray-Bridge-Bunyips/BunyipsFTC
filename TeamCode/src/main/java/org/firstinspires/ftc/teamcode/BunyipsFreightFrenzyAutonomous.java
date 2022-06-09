package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

// Uses Extended DcMotor class

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
    double frontLeftPosition;
    double frontRightPosition;
    double backLeftPosition;
    double backRightPosition;
    boolean targetFound = false;
    double targetDist;
    double targetBearing;
    int armPositionCapture;

    // Declare unit conversion and onboard specification variables
    final double INCHES_TO_CM = 2.54;
    final double FOLLOWER_GEAR_RATIO = 0; // TODO: add following (driven) gear ratio, use 1:<ratio>
    final double WHEEL_DIAMETER_INCHES = 0; // TODO: add wheel diameter in inches
    final double MOTOR_TICKS_PER_REVOLUTION = 0; // TODO: add the motor ticks per revolution

    // Declare Vuforia framework items (key, variables)
    private static final String VUFORIA_KEY =
            "AUAUEO7/////AAABmaBhSSJLMEMkmztY3FQ8jc8fX/wM6mSSQMqcLVW4LjbkWOU5wMH4tLQR7u90fyd93G/7JgfGU5nn2fHF41Q+oaUFe4zI58cr7KsONh689X8o8nr6+7BPN9gMrz08bOzj4+4JwxJ1m84iTPqCpImzYMHr60dtlKBSHN53sRL476JHa+HxZZB4kVq0BhpHlDo7WSGUb6wb5qdgGS3GGx62kiZVCfuWkGY0CZY+pdenCmkNXG2w0/gaeKC5gNw+8G4oGPmAKYiVtCkVJOvjKFncom2h82seL9QA9k96YKns4pQcJn5jdkCbbKNPULv3sqvuvWsjfFOpvzJ0Wh36MrcXlRCetR5oNWctERDjujSjf1o1";
    VuforiaLocalizer vuforia    = null;
    OpenGLMatrix targetPose     = null;
    String targetName           = "";

    // Map hardware
    DcMotorEx armMotor = hardwareMap.get(DcMotorEx.class, "Arm Motor");
    DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "Front Right");
    DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "Back Right");
    CRServo spinIntake = hardwareMap.get(CRServo.class, "Spin Intake");
    DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "Front Left");
    DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "Back Left");
    CRServo carouselRight = hardwareMap.get(CRServo.class, "Carousel Right");
    CRServo carouselLeft = hardwareMap.get(CRServo.class, "Carousel Left");
    BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "Control Hub IMU");

    // PIDF calibrations (manual velocity calibration xx/xx/xx)

    // IMU calibration check function
    private boolean imuCalibrated() {
        telemetry.addData("IMU calibration status", imu.getCalibrationStatus());
        telemetry.addData("Gyro calibration", imu.isGyroCalibrated() ? "True" : "False");
        telemetry.addData("System status", imu.getSystemStatus().toString());
        return imu.isGyroCalibrated();
    }

    // getTranslatedDistance function modified from Advanced Precision Drive code, using gear ratio
    private double getMovedTranslatedDistance() {
        frontLeftPosition = frontLeft.getCurrentPosition();
        frontRightPosition = frontRight.getCurrentPosition();
        backLeftPosition = backLeft.getCurrentPosition();
        backRightPosition = backRight.getCurrentPosition();
        return (WHEEL_DIAMETER_INCHES * Math.PI * ((frontLeftPosition + frontRightPosition + backLeftPosition + backRightPosition) / 4) / (MOTOR_TICKS_PER_REVOLUTION / FOLLOWER_GEAR_RATIO) * INCHES_TO_CM); // TODO: add this function
    }

    // Functions to get data of the environment
    private double getYawAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
        }

    // Ran on init
    @Override
    public void runOpMode() {

        // Set motor direction
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

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

        // Calibration complete - start Vuforia initialisation
        telemetry.addData("IMU", "Ready.");
        telemetry.addData(">", "Initialising Vuforia...");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.useExtendedTracking = false;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);
        VuforiaTrackables targetsFreightFrenzy = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
        targetsFreightFrenzy.get(0).setName("Blue Storage");
        targetsFreightFrenzy.get(1).setName("Blue Alliance Wall");
        targetsFreightFrenzy.get(2).setName("Red Storage");
        targetsFreightFrenzy.get(3).setName("Red Alliance Wall");
        targetsFreightFrenzy.activate();

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
        // This may not be an issue if the thread automatically holds the arm position as it is in RunToPosition mode
        }
    /*
    private void PrecisionDrive(int desiredDistance, double timeConstraint, double precision, double amplitude) {
        // TODO: PrecisionDrive 2
    }

    private void AngleAdjustment(int desiredAngle, int tolerance) {
        // TODO: Angle Adjustment Algorithm 2
    } */
    }

