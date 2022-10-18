package org.firstinspires.ftc.teamcode.lisa.config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

public class LisaConfiguration extends RobotConfig {
    // Motors
    public DcMotorEx left;
    public DcMotorEx right;

    // Other components
    public DistanceSensor fws;
    public ColorSensor dws;
    public BNO055IMU imu;

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

        left = (DcMotorEx) getHardwareOn("Left Motor", hardwareMap.dcMotor);
        right = (DcMotorEx) getHardwareOn("Right Motor", hardwareMap.dcMotor);

        try {
            fws = hardwareMap.get(DistanceSensor.class, "Forward Vision System");
        } catch (Exception e) {
            telemetry.addLine("Forward Vision System failed to configure.");
        }
        try {
            dws = hardwareMap.get(ColorSensor.class, "Downward Vision System");
        } catch (Exception e) {
            telemetry.addLine("Downward Vision System failed to configure.");
        }

        right.setDirection(DcMotorEx.Direction.REVERSE);

        telemetry.addData("BunyipsOpMode Initialisation", "Complete");
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }
}