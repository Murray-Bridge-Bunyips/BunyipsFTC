package org.murraybridgebunyips.joker;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.murraybridgebunyips.bunyipslib.Motor;
import org.murraybridgebunyips.bunyipslib.RobotConfig;
import org.murraybridgebunyips.bunyipslib.external.pid.PIDController;

@Config
public class Joker extends RobotConfig {
    /**
     * Expansion 1: front_left
     */
    public DcMotor frontLeft;
    /**
     * Expansion 2: front_right
     */
    public DcMotor frontRight;
    /**
     * Expansion 0: back_left
     */
    public DcMotor backLeft;
    /**
     * Expansion 3: back_right
     */
    public DcMotor backRight;
    /**
     * Control Hub 0: intakeMotor
     */
    public Motor intakeMotor;
    /**
     * Control Hub 1: liftMotor
     */
    public DcMotor liftMotor;

    /**
     * Control Hub 0: outtakeAlign
     */
    public Servo outtakeAlign;
    /**
     * Control Hub 1: outtakeGrip
     */
    public Servo outtakeGrip;
    /**
     * Control Hub 2: intakeGrip
     */
    public Servo intakeGrip;
    /**
     * Control Hub 3: lights
     */
    public RevBlinkinLedDriver lights;

    /**
     * Control Hub 0-1 (1 used): liftLimiter
     */
    public TouchSensor liftBotStop;
    /**
     * Control Hub 2-3 (3 used): intakeInStop
     */
    public TouchSensor intakeInStop;
    /**
     * Control Hub 4-5 (5 used): intakeOutStop
     */
    public TouchSensor intakeOutStop;
    /**
     * Control Hub 6-7 (7 used): handoverPoint
     */
    public TouchSensor handoverPoint;
    /**
     * Control Hub USB-3.0: webcam
     */
    public WebcamName camera;
    /**
     * Internally connected
     */
    public IMU imu;

    public static double INTAKE_GRIP_OPEN_POSITION = 0.5;
    public static int INTAKE_GRIP_CLOSED_POSITION = 0;

    public static int OUTTAKE_GRIP_OPEN_POSITION = 1;
    public static int OUTTAKE_GRIP_CLOSED_POSITION = 0;

    public static int OUTTAKE_ALIGN_IN_POSITION = 1;
    public static int OUTTAKE_ALIGN_OUT_POSITION = 0;

    public static double INTAKE_ARM_LOWER_POWER_CLAMP = -0.35;
    public static double INTAKE_ARM_UPPER_POWER_CLAMP = 0.35;

    private boolean intakeGripClosed = false;
    private boolean outtakeFacingOut = false;

    @Override
    protected void onRuntime() {
        frontLeft = getHardware("front_left", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        frontRight = getHardware("front_right", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        backLeft = getHardware("back_left", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        backRight = getHardware("back_right", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        intakeMotor = getHardware("intakeMotor", Motor.class, d -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setRunToPositionController(new PIDController(0.02, 0, 0));
        });
        liftMotor = getHardware("liftMotor", DcMotor.class);
        outtakeAlign = getHardware("outtakeAlign", Servo.class);
        outtakeGrip = getHardware("outtakeGrip", Servo.class, d -> d.setDirection(Servo.Direction.REVERSE));
        intakeGrip = getHardware("intakeGrip", Servo.class, d -> d.setDirection(Servo.Direction.REVERSE));
        lights = getHardware("lights", RevBlinkinLedDriver.class);
        liftBotStop = getHardware("liftLimiter", TouchSensor.class);
        intakeInStop = getHardware("intakeInStop", TouchSensor.class);
        intakeOutStop = getHardware("intakeOutStop", TouchSensor.class);
        handoverPoint = getHardware("handoverPoint", TouchSensor.class);
        camera = getHardware("webcam", WebcamName.class);
        imu = getHardware("imu", IMU.class,
                (d) -> d.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD))));
    }

    public void toggleGrips() {
        if (intakeGripClosed) {
            intakeGrip.setPosition(INTAKE_GRIP_OPEN_POSITION);
            outtakeGrip.setPosition(OUTTAKE_GRIP_CLOSED_POSITION);
            intakeGripClosed = false;
        }
        else {
            intakeGrip.setPosition(INTAKE_GRIP_CLOSED_POSITION);
            outtakeGrip.setPosition(OUTTAKE_GRIP_OPEN_POSITION);
            intakeGripClosed = true;
        }
    }

    public void toggleOuttake() {
        if (outtakeFacingOut) {
            outtakeAlign.setPosition(OUTTAKE_ALIGN_IN_POSITION);
            outtakeFacingOut = false;
        }
        else {
            outtakeAlign.setPosition(OUTTAKE_ALIGN_OUT_POSITION);
            outtakeFacingOut = true;
        }
    }
}