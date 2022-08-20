package org.firstinspires.ftc.teamcode.lisa;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

public class LisaConfiguration extends RobotConfig {
    // Motors
    public DcMotorEx left;
    public DcMotorEx right;

    BNO055IMU imu;

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
            left = (DcMotorEx) getHardwareOn("Left Motor", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("Left Motor failed to configure.");
        }
        try {
            right = (DcMotorEx) getHardwareOn("Right Motor", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("Right Motor failed to configure.");
        }

        right.setDirection(DcMotorEx.Direction.REVERSE);

        telemetry.addData("Initialisation of all onboard motors", "Activated");
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
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }


}
