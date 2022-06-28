package org.firstinspires.ftc.teamcode.lisa;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

// TODO: Change this file for minibot Lisa configuration, but do it after Bertie

public class LisaConfiguration extends RobotConfig {
    //  Motors
    public DcMotorEx backLeft;
    public DcMotorEx backRight;
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx armMotor;
    public CRServo spinIntake;
    public CRServo carouselLeft;
    public CRServo carouselRight;

    BNO055IMU imu;

/* No dummy wheel to use encoders with
    public double countsPerMotorRev = 288;
    public double driveGearReduction = 72.0 / 90.0; // 72 Teeth -> 90 Teeth
    public double wheelDiameterCm = 9.0;

    public double countsPerCm = (countsPerMotorRev * driveGearReduction) / (wheelDiameterCm * Math.PI);
*/

    /**
     * Factory method for this class
     *
     * @param hardwareMap
     * @param telemetry
     * @return
     */
    public static LisaConfiguration newConfig(HardwareMap hardwareMap, Telemetry telemetry) {
        LisaConfiguration config = new LisaConfiguration();
        config.init(hardwareMap, telemetry);
        return config;
    }

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     *
     * @param hardwareMap
     * @param telemetry
     */
    @Override
    protected void init(HardwareMap hardwareMap, Telemetry telemetry) {

        setTelemetry(telemetry);

        try {
            backLeft = (DcMotorEx) getHardwareOn("Back Left", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("backLeft failed to configure.");
        }
        try {
            backRight = (DcMotorEx) getHardwareOn("Back Right", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("backRight failed to configure.");
        }
        try {
            frontLeft = (DcMotorEx) getHardwareOn("Front Left", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("frontRight failed to configure.");
        }
        try {
            frontRight = (DcMotorEx) getHardwareOn("Front Right", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("frontRight failed to configure.");
        }
        try {
            armMotor = (DcMotorEx) getHardwareOn("Arm Motor", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("armMotor failed to configure.");
        }
        try {
            carouselLeft = (CRServo) getHardwareOn("Carousel Left", hardwareMap.crservo);
        } catch (Exception e) {
            telemetry.addLine("carouselLeft failed to configure.");
        }
        try {
            carouselRight = (CRServo) getHardwareOn("Carousel Right", hardwareMap.crservo);
        } catch (Exception e) {
            telemetry.addLine("carouselRight failed to configure.");
        }
        try {
            spinIntake = (CRServo) getHardwareOn("Spin Intake", hardwareMap.crservo);
        } catch (Exception e) {
            telemetry.addLine("spinIntake failed to configure.");
        }

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        spinIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Initialisation", "Activated");
        telemetry.update();

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "Control Hub IMU");
        imu.initialize(parameters);
    }


}
