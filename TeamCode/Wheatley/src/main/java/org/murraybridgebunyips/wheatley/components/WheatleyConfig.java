package org.murraybridgebunyips.wheatley.components;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
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

/**
 * Wheatley robot configuration and hardware declarations
 *
 * @author Lucas Bubner, 2024
 * @author Lachlan Paul, 2024
 */

public class WheatleyConfig extends RobotConfig {

    // I'm not sure if this comment actually makes sense here but I'm keeping it here anyway
    //    front_servo = hardwareMap.get(Servo.class, "front_servo");
    //    back_servo = hardwareMap.get(Servo.class, "back_servo");
    //    gripper = hardwareMap.get(Servo.class, "gripper");
    //    right_front = hardwareMap.get(DcMotor.class, "right_front");
    //    right_rear = hardwareMap.get(DcMotor.class, "right_rear");
    //    arm = hardwareMap.get(DcMotor.class, "arm");
    //    left_front = hardwareMap.get(DcMotor.class, "left_front");
    //    left_rear = hardwareMap.get(DcMotor.class, "left_rear");


    /**
     * USB: Webcam "webcam"
     */
    public WebcamName webcam;

    /**
     * Internally mounted on I2C C0 "imu"
     */
    public IMU imu;

    /**
     * Expansion 0: bl
     */
    public DcMotorEx bl;

    /**
     * Expansion 1: fl
     */
    public DcMotorEx fl;

    /**
     * Expansion 2: fr
     */
    public DcMotorEx /*Are you*/ fr /*Or jk*/;

    /**
     * Expansion 3: br
     */
    public DcMotorEx br;

    /**
     * Control 0: Linear Actuator "la"
     */
    public DcMotorEx linearActuator;

    /**
     * Control 1: Claw Rotator "cr"
     */
    public DcMotorEx clawRotator;

    /**
     * Control Servo 0: Left Servo "ls"
     */
    public Servo leftPixel;

    /**
     * Control Servo 1: Plane Launcher "pl"
     */
    public Servo launcher;

    /**
     * Control Servo 5: Right Servo "rs"
     */
    public Servo rightPixel;

    /**
     * RoadRunner drive constants
     */
    public DriveConstants driveConstants;
    /**
     * RoadRunner Mecanum coefficients
     */
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void onRuntime() {
        webcam = getHardware("webcam", WebcamName.class);

        // Motor directions configured to work with current config
        fl = getHardware("fl", DcMotorEx.class);
        bl = getHardware("bl", DcMotorEx.class);
        fr = getHardware("fr", DcMotorEx.class);
        br = getHardware("br", DcMotorEx.class);
        imu = getHardware("imu", IMU.class);

        if (fr != null)
            fr.setDirection(DcMotorSimple.Direction.REVERSE);

        if (fl != null)
            fl.setDirection(DcMotorSimple.Direction.REVERSE);

        if (br != null)
            br.setDirection(DcMotorSimple.Direction.REVERSE);

        if (bl != null)
            bl.setDirection(DcMotorSimple.Direction.REVERSE);

        // Suspender/pixel upward motion system
        linearActuator = getHardware("la", DcMotorEx.class);
        if (linearActuator != null) {
            linearActuator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Pixel manipulation system
        clawRotator = getHardware("cr", DcMotorEx.class);
        if (clawRotator != null) {
            clawRotator.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        leftPixel = getHardware("ls", Servo.class);
        rightPixel = getHardware("rs", Servo.class);

        // Paper Drone launcher system
        launcher = getHardware("pl", Servo.class);

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
                .setKV(0.0145)
                .setKStatic(0.06422)
                .setKA(0.001)
                .build();

        mecanumCoefficients = new MecanumCoefficients.Builder()
                .setLateralMultiplier(60.0 / 54.07)
                .setTranslationalPID(new PIDCoefficients(8, 0, 0))
                .setHeadingPID(new PIDCoefficients(10, 0, 0))
                .build();
    }
}
