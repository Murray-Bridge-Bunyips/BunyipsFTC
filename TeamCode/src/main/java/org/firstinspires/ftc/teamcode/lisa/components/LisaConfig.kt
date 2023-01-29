package org.firstinspires.ftc.teamcode.lisa.components;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

import java.util.ArrayList;
import java.util.Iterator;

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

        left = (DcMotorEx) getHardware("Left Motor", DcMotorEx.class);
        right = (DcMotorEx) getHardware("Right Motor", DcMotorEx.class);
        dws = (ColorSensor) getHardware("Downward Vision System", ColorSensor.class);
        fws = (DistanceSensor) getHardware("Forward Vision System", DistanceSensor.class);

        right.setDirection(DcMotorEx.Direction.REVERSE);

        // Control Hub IMU configuration
        // This uses the legacy methods for IMU initialisation, this should be refactored and updated
        // at some point in time. (23 Nov 2022)
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = (BNO055IMU) getHardware("imu", BNO055IMU.class);
        if (imu != null)
            imu.initialize(parameters);

        ArrayList<String> errors = getHardwareErrors();
        if (errors == null) {
            telemetry.addData("BunyipsOpMode Status", "ROBOT CONFIGURATION COMPLETED SUCCESSFULLY WITH NO ERRORS.");
            return;
        }

        telemetry.addData("BunyipsOpMode Status", "ERROR(S) DURING CONFIGURATION, THESE DEVICES WERE NOT ABLE TO BE CONFIGURED.");
        Iterator<String> error = errors.iterator();
        for (int i = 0; i < errors.size(); i++) {
            telemetry.addData(String.valueOf(i), error.next());
        }
    }
}
