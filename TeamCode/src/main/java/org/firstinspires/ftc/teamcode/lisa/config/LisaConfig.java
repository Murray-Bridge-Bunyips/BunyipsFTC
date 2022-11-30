package org.firstinspires.ftc.teamcode.lisa.config;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

public class LisaConfig extends RobotConfig {

    public DcMotorEx left;
    public DcMotorEx right;
    public DistanceSensor fws;
    public ColorSensor dws;
    public BNO055IMU imu;

    public static LisaConfig newConfig(HardwareMap hardwareMap, Telemetry telemetry) {
        LisaConfig config = new LisaConfig();
        config.init(hardwareMap, telemetry);
        return config;
    }

    @Override
    protected void init(HardwareMap hardwareMap, Telemetry telemetry) {
        setTelemetry(telemetry);

        left = (DcMotorEx) getHardwareOn("Left Motor", hardwareMap.dcMotor);
        right = (DcMotorEx) getHardwareOn("Right Motor", hardwareMap.dcMotor);
        dws = (ColorSensor) getHardwareOn("Downward Vision System", hardwareMap.colorSensor);

        try {
            fws = hardwareMap.get(DistanceSensor.class, "Forward Vision System");
        } catch (Exception e) {
            telemetry.addLine("Forward Vision System failed to configure.");
        }

        right.setDirection(DcMotorEx.Direction.REVERSE);

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
