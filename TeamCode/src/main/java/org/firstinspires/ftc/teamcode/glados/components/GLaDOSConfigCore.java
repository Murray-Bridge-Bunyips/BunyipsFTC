package org.firstinspires.ftc.teamcode.glados.components;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.Dbg;
import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumCoefficients;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.localizers.TwoWheelTrackingLocalizerCoefficients;
import org.firstinspires.ftc.teamcode.common.roadrunner.util.Encoder;

/**
 * FTC 15215 CENTERSTAGE 2023-2024 robot configuration
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSConfigCore extends RobotConfig {
    // USB: Webcam "webcam"
    public WebcamName webcam;
    // Expansion 0: Front Left "fl"
    public DcMotorEx frontLeft;
    // Expansion 1: Front Right "fr"
    public DcMotorEx frontRight;
    // Expansion 2: Back Right "br"
    public DcMotorEx backRight;
    // Expansion 3: Back Left "bl"
    public DcMotorEx backLeft;
    // Control 2: Parallel Encoder "pe"
    public Encoder parallelEncoder;
    // Control 3: Perpendicular Encoder "ppe"
    public Encoder perpendicularEncoder;
    // Control ?: Suspender Actuator "sa"
    public DcMotorEx suspenderActuator;
    // Control Servo ?: Pixel Forward Motion Servo "pm"
    public CRServo pixelMotion;
    // Control Servo ?: Pixel Alignment Servo "al"
    public Servo pixelAlignment;
    // Control Servo ?: Left Servo "ls"
    public Servo leftPixel;
    // Control Servo ?: Right Servo "rs"
    public Servo rightPixel;
    // Control Servo ?: Plane Launcher "pl"
    public Servo launcher;
    // Internally mounted on I2C C0 "imu"
    public IMU imu;

    public DriveConstants driveConstants;
    public TwoWheelTrackingLocalizerCoefficients localizerCoefficients;
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void configureHardware() {
        // Sensors
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        imu = (IMU) getHardware("imu", IMU.class);

        // Mecanum system
        frontLeft = (DcMotorEx) getHardware("fl", DcMotorEx.class);
        frontRight = (DcMotorEx) getHardware("fr", DcMotorEx.class);
        backRight = (DcMotorEx) getHardware("br", DcMotorEx.class);
        backLeft = (DcMotorEx) getHardware("bl", DcMotorEx.class);
        DcMotorEx pe = (DcMotorEx) getHardware("pe", DcMotorEx.class);
        if (pe != null) {
            parallelEncoder = new Encoder(pe);
            parallelEncoder.setDirection(Encoder.Direction.FORWARD);
        }

        DcMotorEx ppe = (DcMotorEx) getHardware("ppe", DcMotorEx.class);
        if (ppe != null) {
            perpendicularEncoder = new Encoder(ppe);
            perpendicularEncoder.setDirection(Encoder.Direction.FORWARD);
        }

        // Suspender/pixel upward motion system
        suspenderActuator = (DcMotorEx) getHardware("sa", DcMotorEx.class);

        // Pixel manipulation system
        pixelMotion = (CRServo) getHardware("pm", CRServo.class);
        pixelAlignment = (Servo) getHardware("al", Servo.class);
        leftPixel = (Servo) getHardware("ls", Servo.class);
        rightPixel = (Servo) getHardware("rs", Servo.class);

        // Paper Drone launcher system
        launcher = (Servo) getHardware("pl", Servo.class);

        // Motor specifics configuration
        if (frontRight != null)
            frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        if (frontLeft != null)
            frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        if (backRight != null)
            backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        if (backLeft != null)
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        if (suspenderActuator != null) {
            suspenderActuator.setDirection(DcMotorSimple.Direction.FORWARD);
            suspenderActuator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        boolean res = imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                        )
                )
        );

        if (!res) {
            Dbg.error("IMU failed to initialise!");
        }

        // RoadRunner configuration
        driveConstants = new DriveConstants.Builder()
                .setTicksPerRev(28)
                .setMaxRPM(6000)
                .setRunUsingEncoder(false)
                .setWheelRadius(Inches.fromMM(75) / 2)
                .setGearRatio(1.0 / 13.1)
                .setTrackWidth(15.3)
                // ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
                .setMaxVel(41.065033847087705)
                .setMaxAccel(41.065033847087705)
                .setMaxAngVel(Math.toRadians(130.71406249999998))
                .setMaxAngAccel(Math.toRadians(130.71406249999998))
                .setKV(0.0016)
                .setKStatic(0.05833)
                .setKA(0.01401)
                .build();

        localizerCoefficients = new TwoWheelTrackingLocalizerCoefficients.Builder()
                .setTicksPerRev(1200)
                .setGearRatio(1)
                .setWheelRadius(Inches.fromMM(50) / 2)
                .setParallelX(0)
                .setParallelY(0)
                .setPerpendicularX(4.2)
                .setPerpendicularY(3.5)
                .build();

        mecanumCoefficients = new MecanumCoefficients.Builder()
                .build();
    }
}
