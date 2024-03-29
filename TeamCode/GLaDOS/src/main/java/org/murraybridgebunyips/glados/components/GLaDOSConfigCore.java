package org.murraybridgebunyips.glados.components;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.RobotConfig;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.DriveConstants;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumCoefficients;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.localizers.TwoWheelTrackingLocalizerCoefficients;
import org.murraybridgebunyips.bunyipslib.roadrunner.util.Deadwheel;

/**
 * FTC 15215 CENTERSTAGE 2023-2024 robot configuration
 *
 * @author Lucas Bubner, 2024
 */
public class GLaDOSConfigCore extends RobotConfig {
    /**
     * USB: Webcam "webcam"
     */
    public WebcamName webcam;
    /**
     * Expansion 0: Front Left "fl"
     */
    public DcMotorEx frontLeft;
    /**
     * Expansion 1: Front Right "fr"
     */
    public DcMotorEx frontRight;
    /**
     * Expansion 2: Back Right "br"
     */
    public DcMotorEx backRight;
    /**
     * Expansion 3: Back Left "bl"
     */
    public DcMotorEx backLeft;
    /**
     * Control 2: Parallel Encoder "pe"
     */
    public Deadwheel parallelDeadwheel;
    /**
     * Control 3: Perpendicular Encoder "ppe"
     */
    public Deadwheel perpendicularDeadwheel;
    /**
     * Control 0: Suspender Actuator "sa"
     */
    public DcMotorEx suspenderActuator;
    /**
     * Control Servo 5: Pixel Forward Motion Servo "pm"
     */
    public CRServo pixelMotion;
    /**
     * Control Servo 4: Pixel Alignment Servo "al"
     */
    public Servo pixelAlignment;
    /**
     * Control Servo 2: Left Servo "ls"
     */
    public Servo leftPixel;
    /**
     * Control Servo 3: Right Servo "rs"
     */
    public Servo rightPixel;
    /**
     * Control Servo 1: Suspension Hook "sh"
     */
    public Servo suspenderHook;
    /**
     * Control Servo ?: Plane Launcher "pl"
     */
    public Servo launcher;
    /**
     * Internally mounted on I2C C0 "imu"
     */
    public IMU imu;

    /**
     * RoadRunner drive constants
     */
    public DriveConstants driveConstants;
    /**
     * Dual deadwheel intrinsics
     */
    public TwoWheelTrackingLocalizerCoefficients localizerCoefficients;
    /**
     * Mecanum coefficients
     */
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void onRuntime() {
        // Sensors
        webcam = getHardware("webcam", WebcamName.class);
        imu = getHardware("imu", IMU.class);

        // Mecanum system
        frontLeft = getHardware("fl", DcMotorEx.class);
        frontRight = getHardware("fr", DcMotorEx.class);
        backRight = getHardware("br", DcMotorEx.class);
        backLeft = getHardware("bl", DcMotorEx.class);
        DcMotorEx pe = getHardware("pe", DcMotorEx.class);
        if (pe != null) {
            parallelDeadwheel = new Deadwheel(pe);
            parallelDeadwheel.setDirection(Deadwheel.Direction.FORWARD);
        }

        DcMotorEx ppe = getHardware("ppe", DcMotorEx.class);
        if (ppe != null) {
            perpendicularDeadwheel = new Deadwheel(ppe);
            perpendicularDeadwheel.setDirection(Deadwheel.Direction.FORWARD);
        }

        pixelMotion = getHardware("pm", CRServo.class);
        pixelAlignment = getHardware("al", Servo.class);

        // Suspender/pixel upward motion system
        suspenderActuator = getHardware("sa", DcMotorEx.class);
        suspenderHook = getHardware("sh", Servo.class);
        if (suspenderHook != null)
            suspenderHook.scaleRange(0.25, 1);

        // Pixel manipulation system
        leftPixel = getHardware("ls", Servo.class);
        rightPixel = getHardware("rs", Servo.class);

        // Paper Drone launcher system
        launcher = getHardware("pl", Servo.class);

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

        boolean res = imu != null && imu.initialize(
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
                .setWheelRadius(Inches.fromMM(75) / 2.0)
                .setGearRatio(1.0 / 13.1)
                .setTrackWidth(20.5)
                // ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
                .setMaxVel(41.065033847087705)
                .setMaxAccel(41.065033847087705)
                // 179.687013 in degrees
                .setMaxAngVel(3.13613)
                .setMaxAngAccel(3.13613)
                .setKV(0.01395)
                .setKStatic(0.06311)
                .setKA(0.0015)
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
                .setLateralMultiplier(60.0 / 59.846666)
                .setTranslationalPID(new PIDCoefficients(8, 0, 0))
                .setHeadingPID(new PIDCoefficients(10, 0, 0))
                .build();
    }
}
