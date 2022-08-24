package org.firstinspires.ftc.teamcode.kanga;

public class KangaConfiguration extends RobotConfig {
    public DcMotor left;
    public DcMotor right;

    BNO055IMU imu;

    /**
     * Factory method for this class
     *
     * @param hardwareMap
     * @param telemetry
     * @return
     */
    public static KangaConfiguration newConfig(HardwareMap hardwareMap, Telemetry telemetry) {
        KangaConfiguration config = new KangaConfiguration();
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
            left = (DcMotor) getHardwareOn("left_motor", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("left_motor failed to configure.");
        }
        try {
            right = (DcMotor) getHardwareOn("right_motor", hardwareMap.dcMotor);
        } catch (Exception e) {
            telemetry.addLine("right_motor failed to configure.");
        }

        right.setDirection(DcMotor.Direction.REVERSE);

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