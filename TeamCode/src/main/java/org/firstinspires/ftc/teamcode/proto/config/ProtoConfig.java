package org.firstinspires.ftc.teamcode.proto.config;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.Deadwheel;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

public class ProtoConfig extends RobotConfig {

    // Add declarations here
    public CameraName webcam;
    public int tfodMonitorViewId;
    public DcMotorEx bl;
    public DcMotorEx br;
    public DcMotorEx fl;
    public DcMotorEx fr;
    public Deadwheel x, y;
    public CRServo claw;
    public DcMotorEx arm;
    public BNO055IMU imu;

    public static ProtoConfig newConfig(HardwareMap hardwareMap, Telemetry telemetry) {
        ProtoConfig config = new ProtoConfig();
        config.init(hardwareMap, telemetry);
        return config;
    }

    @Override
    protected void init(HardwareMap hardwareMap, Telemetry telemetry) {
        setTelemetry(telemetry);

        // Add configurations here
        try {
            // Using manual error catching as the hardwareMap for getHardwareOn does not include
            // webcam configurations
            webcam = hardwareMap.get(WebcamName.class, "Webcam");
            tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        } catch (Exception e) {
            telemetry.addLine("Error configuring device 'Webcam'. Check connections.");
        }

//        bl = (DcMotorEx) getHardwareOn("Back Left", hardwareMap.dcMotor);
//        br = (DcMotorEx) getHardwareOn("Back Right", hardwareMap.dcMotor);
//        fl = (DcMotorEx) getHardwareOn("Front Left", hardwareMap.dcMotor);
//        fr = (DcMotorEx) getHardwareOn("Front Right", hardwareMap.dcMotor);
//        arm = (DcMotorEx) getHardwareOn("Arm Motor", hardwareMap.dcMotor);
        claw = (CRServo) getHardwareOn("Arm Servo", hardwareMap.crservo);

        // Encoder configuration (Using modified DcMotor classes with built-in distance calculations)
        try {
            x = hardwareMap.get(Deadwheel.class, "X Encoder");
            y = hardwareMap.get(Deadwheel.class, "Y Encoder");
        } catch (Exception e) {
            telemetry.addLine("Error configuring X,Y deadwheel encoders. Check connections.");
        }

        // Control Hub IMU configuration
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        try {
            imu = hardwareMap.get(BNO055IMU.class, "imu");
            if (imu.initialize(parameters)) {
                while (!imu.isGyroCalibrated()) {
                    telemetry.addData("Please wait for IMU initialisation", imu.getCalibrationStatus());
                    telemetry.update();
                }
            } else {
                throw new Exception("IMU initialisation was unsuccessful.");
            }
        } catch (Exception e) {
            telemetry.addLine("An internal error occurred configuring the IMU.");
        } finally {
            telemetry.addData("BunyipsOpMode Initialisation", "Complete.");
            telemetry.update();
        }
    }
}
