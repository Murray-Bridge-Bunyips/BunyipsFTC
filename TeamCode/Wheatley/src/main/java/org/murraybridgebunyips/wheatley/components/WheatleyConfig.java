package org.murraybridgebunyips.wheatley.components;

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

/**
 * Wheatley robot configuration and hardware declarations
 *
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
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

    // Expansion 1: fl
    public DcMotorEx fl;

    // Expansion 0: bl
    public DcMotorEx bl;

    // Expansion 2: fr
    public DcMotorEx /*Are you*/ fr /*Or jk*/;

    // Expansion 3: br
    public DcMotorEx br;

    // Control 0: Suspender Actuator "sa"
    public DcMotorEx suspenderActuator;

    // Control Servo 5: Pixel Forward Motion Servo "pm"
    public CRServo pixelMotion;

    // Control Servo 4: Pixel Alignment Servo "al"
    public Servo pixelAlignment;

    // Control Servo 2: Left Servo "ls"
    public Servo leftPixel;

    // Control Servo 3: Right Servo "rs"
    public Servo rightPixel;

    // Control Servo 1: Suspension Hook "sh"
    public Servo suspenderHook;

    // Control Servo 0: Plane Launcher "pl"
    public Servo launcher;

    // USB: Webcam "webcam"
    public WebcamName webcam;

    // Internally mounted on I2C C0 "imu"
    public IMU imu;

    public DriveConstants driveConstants;
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void configureHardware() {
        webcam = (WebcamName) getHardware("webcame", WebcamName.class);

        // Motor directions configured to work with current config
        fl = (DcMotorEx) getHardware("fl", DcMotorEx.class);
        bl = (DcMotorEx) getHardware("bl", DcMotorEx.class);
        fr = (DcMotorEx) getHardware("fr", DcMotorEx.class);
        br = (DcMotorEx) getHardware("br", DcMotorEx.class);
        imu = (IMU) getHardware("imu", IMU.class);

        if (fr != null)
            fr.setDirection(DcMotorSimple.Direction.REVERSE);

        if (fl != null)
            fl.setDirection(DcMotorSimple.Direction.REVERSE);

        if (br != null)
            br.setDirection(DcMotorSimple.Direction.REVERSE);

        if (bl != null)
            bl.setDirection(DcMotorSimple.Direction.REVERSE);

        // Suspender/pixel upward motion system
        suspenderActuator = (DcMotorEx) getHardware("sa", DcMotorEx.class);
        suspenderHook = (Servo) getHardware("sh", Servo.class);
        if (suspenderHook != null)
            suspenderHook.scaleRange(0.6, 1);
        if (suspenderActuator != null) {
            suspenderActuator.setDirection(DcMotorSimple.Direction.REVERSE);
            suspenderActuator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Pixel manipulation system
        pixelMotion = (CRServo) getHardware("pm", CRServo.class);
        pixelAlignment = (Servo) getHardware("al", Servo.class);
        if (pixelAlignment != null)
            pixelAlignment.scaleRange(0.37, 1.0);
        leftPixel = (Servo) getHardware("ls", Servo.class);
        rightPixel = (Servo) getHardware("rs", Servo.class);

        // Paper Drone launcher system
        launcher = (Servo) getHardware("pl", Servo.class);

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
