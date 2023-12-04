package org.firstinspires.ftc.teamcode.lisa_independent;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

// ----------------------------------------------
// Code written by Lucas Bubner through Blocks
// Murray Bridge Bunyips - 15215
// ----------------------------------------------

/*
    Lucas's first innovative OpMode in Blocks.
    Archived and stored away for information purposes only, tidied up to look a bit nicer.
 */

@Disabled
@Autonomous(name = "Advanced PrecisionDrive - 24M STRAIGHT")
public class APD24m extends LinearOpMode {

    // Declare hardware
    private BNO055IMU imu;
    private DcMotor LeftMotor;
    private DcMotor RightMotor;
    private DistanceSensor ForwardVisionSystem_DistanceSensor;

    // Declare variables
    boolean fwsAlert;
    float yawAngle;
    double leftPower;
    double rightPower;
    double leftMotorCurrentPosition;
    double rightMotorCurrentPosition;
    ElapsedTime elapsedTime;

    // IMU calibration check method
    private boolean IMU_Calibrated() {
        telemetry.addData("IMU calibration status", imu.getCalibrationStatus());
        telemetry.addData("Gyro calibration", imu.isGyroCalibrated() ? "True" : "False");
        telemetry.addData("System status", imu.getSystemStatus().toString());
        return imu.isGyroCalibrated();
    }

    // If pitch over 30 degrees, run fwsalert
    private boolean areMotorsOverexerting() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle > 30;
    }

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters imuParameters;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        LeftMotor = hardwareMap.get(DcMotor.class, "Left Motor");
        RightMotor = hardwareMap.get(DcMotor.class, "Right Motor");
        ForwardVisionSystem_DistanceSensor = hardwareMap.get(DistanceSensor.class, "Forward Vision System");
        LeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Neutralise the FWS stop system
        fwsAlert = false;
        // Reverse direction of both motors for one-direction travel
        LeftMotor.setDirection(DcMotor.Direction.REVERSE);
        RightMotor.setDirection(DcMotor.Direction.REVERSE);
        // Reset encoders for distance calc
        LeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // IMU configuration
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.mode = BNO055IMU.SensorMode.IMU;
        imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled = true;
        imuParameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        // Init IMU sequence
        imu.initialize(imuParameters);
        telemetry.addData("Status", "IMU successfully initialised");
        telemetry.update();
        // IMU calibration check
        sleep(1000);
        while (!IMU_Calibrated()) {
            telemetry.addData("If calibration ", "doesn't complete after 3 seconds, move through 90 degree pitch, roll and yaw motions until calibration is complete.");
            telemetry.update();
            // Loop until IMU is calibrated
            sleep(1000);
        }
        // Calibration complete - await for Driver
        telemetry.addData("IMU", "Ready.");
        telemetry.addData(">", "Ready to initialise code.");
        telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
            // Call to main variable functions
            MoveDesiredDistanceWithPrecisionDriveAlgorithm(2400, 0, 5, 0.9);
            // Execution complete.
            leftPower = 0;
            rightPower = 0;
            LeftMotor.setPower(0);
            RightMotor.setPower(0);
            telemetry.addData("Code execution:", "All actions completed.");
            telemetry.update();
            sleep(3000);
        }
    }

    /**
     * Math function to calculate the distance travelled by the wheels.
     *
     * avg of motor position divided by pulses per revolution =
     * roughly how many revolutions have been done by the robot,
     * where plugging in the wheel circumference and using the formula
     *
     * diameter of wheel in inches * pi = circumference
     * circumference * revolutions = inches travelled
     *
     * inches * 2.54 = cm
     *
     * We get the translated distance travelled by the encoders in centimetres.
     * After motor changes, return value has been additively inversed due to different encoders.
     */
    private double getTranslatedDistance() {
        leftMotorCurrentPosition = LeftMotor.getCurrentPosition();
        rightMotorCurrentPosition = RightMotor.getCurrentPosition();
        return -(3.34646 * Math.PI * ((leftMotorCurrentPosition + rightMotorCurrentPosition) / 2) / 288 * 2.54);
    }

    /**
     * Initialises the Forward Vision System Collision Avoidance System.
     * Collision avoidance is only active during a forward drive.
     * The avoidance system will activate once below the alert_threshold,
     * begin backwards thrust to a clearance of [x]cm, and return
     * the motor control to the following function in runOpMode.
     * Code written by Lucas Bubner.
     */
    private void FWSCollisionAvoidanceCheck() {
        double fwsDist;
        double captureCurrentDistance;

        fwsDist = ForwardVisionSystem_DistanceSensor.getDistance(DistanceUnit.CM);
        if (fwsDist < 7 || areMotorsOverexerting()) {
            // Activating FWS procedure
            // Broadcast to code to stop operations
            fwsAlert = true;
            // Take over motor control to reverse x distance
            LeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            LeftMotor.setPower(0);
            RightMotor.setPower(0);
            sleep(200);
            captureCurrentDistance = getTranslatedDistance();
            LeftMotor.setPower(-1);
            RightMotor.setPower(-1);
            // Reverse distance is 7cm
            while (!(getTranslatedDistance() <= captureCurrentDistance - 7)) {
                // Allow robot to move backwards until reverse_dist
                telemetry.addData("Debug", "fws_alert currently active");
                telemetry.update();
            }
            LeftMotor.setPower(0);
            RightMotor.setPower(0);
            sleep(200);
            // Reset FWS and allow code to continue
            fwsAlert = false;
        }
    }

    /**
     * Utilise the Angle Adjustment Algorithm in order to turn the
     * robot to within accuracies, with three-stop correction.
     */
    private void TurnUsingAngleAdjustmentAlgorithm(int desiredAngle, int tolerance) {
        boolean turnSwitch;
        double i;
        int desiredAngleCompensated;

        LeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Check direction of turn and latch codes
        if (desiredAngle >= 0) {
            turnSwitch = true;
            desiredAngleCompensated = desiredAngle - 15;
        } else {
            turnSwitch = false;
            desiredAngleCompensated = desiredAngle + 15;
        }
        // Repeat code four times to ensure accuracy
        // Unless IMU reports within <t> degrees of accuracy
        for (i = 1; i <= 3; i++) {
        if (turnSwitch) {
            // Turn left
            LeftMotor.setPower(i >= 3 ? -0.25 : -1.5 + i / 2);
            RightMotor.setPower(i >= 3 ? 0.25 : 1.5 - i / 2);
            while (!(yawAngle >= (i >= 2 ? desiredAngle : desiredAngleCompensated))) {
                yawAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
                // Update yaw while robot turns
                telemetry.addData("Debug Yaw angle:", yawAngle);
                telemetry.update();
            }
        } else {
            // Turn right
            LeftMotor.setPower(i >= 3 ? 0.25 : 1.5 - i / 2);
            RightMotor.setPower(i >= 3 ? -0.25 : -1.5 + i / 2);
            while (!(yawAngle <= (i >= 2 ? desiredAngle : desiredAngleCompensated))) {
                // Update Yaw-Angle variable with current yaw.
                yawAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
                // Report yaw orientation to Driver Station.
                telemetry.addData("Debug Yaw angle:", yawAngle);
                telemetry.update();
            }
        }
        LeftMotor.setPower(0);
        RightMotor.setPower(0);
        // Allow robot to remove momentum
        sleep(150);
        // Check if within tolerance
        yawAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        if (!(yawAngle < desiredAngle + tolerance || yawAngle > desiredAngle - tolerance)) {
            // Restart from i loop with lower amplitude until aligned
            break;
        }
    }
    }

    /**
     * Corrects yaw within +-1 degrees of accuracy over x distance.
     * Uses time limit backup - if set to null, the code will ignore.
     * Utilises the self-designed PrecisionDrive algorithm.
     */
    private void MoveDesiredDistanceWithPrecisionDriveAlgorithm(int desiredDistance_cm, double timeLimit, double precisionAngle_drg, double correctionAmplitude) {
        float originalDesiredAngle;
        // Reset encoders and run
        LeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        originalDesiredAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        // Utilises auto-latching for forward or backward cmd
        while (!((desiredDistance_cm > 0 ? getTranslatedDistance() : -getTranslatedDistance()) >= (desiredDistance_cm > 0 ? desiredDistance_cm : -desiredDistance_cm) || (timeLimit != 0) ? elapsedTime.milliseconds() >= timeLimit * Math.pow(10, 3) || isStopRequested() : isStopRequested()) || (desiredDistance_cm > 0 ? fwsAlert : isStopRequested())) {
        if (fwsAlert) {
            break;
        }
        yawAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        // Z coordination orientation angle
        telemetry.addData("Debug Yaw angle:", yawAngle);
        // If outside x degree(s) of desired yaw then correct
        if (yawAngle < originalDesiredAngle - precisionAngle_drg) {
            // Left correction if positive, vice versa
            leftPower = desiredDistance_cm > 0 ? correctionAmplitude : -1;
            rightPower = desiredDistance_cm > 0 ? 1 : -correctionAmplitude;
        } else if (yawAngle > originalDesiredAngle + precisionAngle_drg) {
            // Right correction if positive, vice versa
            leftPower = desiredDistance_cm > 0 ? 1 : -correctionAmplitude;
            rightPower = desiredDistance_cm > 0 ? correctionAmplitude : -1;
        } else {
            // No correction
            leftPower = desiredDistance_cm > 0 ? 1 : -1;
            rightPower = desiredDistance_cm > 0 ? 1 : -1;
        }
        // Report debug information
        telemetry.addData("Debug Left Power:", leftPower);
        telemetry.addData("Debug Right Power:", rightPower);
        telemetry.addData("Debug translatedDistance:", desiredDistance_cm > 0 ? getTranslatedDistance() : -getTranslatedDistance());
        LeftMotor.setPower(leftPower);
        RightMotor.setPower(rightPower);
        telemetry.update();
        // Loop correction algorithm and check FWS
        if (desiredDistance_cm > 0) {
            FWSCollisionAvoidanceCheck();
        } else if (areMotorsOverexerting()) {
            break;
        }
        sleep(200);
    }
        leftPower = 0;
        rightPower = 0;
        LeftMotor.setPower(leftPower);
        RightMotor.setPower(rightPower);
        sleep(200);
    }
}