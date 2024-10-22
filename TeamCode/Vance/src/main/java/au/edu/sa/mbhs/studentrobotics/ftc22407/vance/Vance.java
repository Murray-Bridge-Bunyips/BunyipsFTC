package au.edu.sa.mbhs.studentrobotics.ftc22407.vance;

import com.acmerobotics.roadrunner.ftc.LazyImu;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.ThreeWheelLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;

/**
 * FTC 22407 INTO THE DEEP 2024-2025 robot configuration
 *
 * @author Lachlan Paul, 2024
 */
public class Vance extends RobotConfig {
    /**
     * Internally mounted on I2C C0 "imu"
     */
    public LazyImu imu;

    /**
     * Control 0: fr
     */
    public DcMotorEx /*Are you*/ fr /*Or jk*/;

    /**
     * Control 1: fl
     */
    public DcMotorEx fl;

    /**
     * Control 2: bl
     */
    public DcMotorEx bl;

    /**
     * Control 3: br
     */
    public DcMotorEx br;

    /**
     * Control 3: br
     */
    public RawEncoder dwleft;

    /**
     * Control 0: fr
     */
    public RawEncoder dwright;

    /**
     * Control 1: fl
     */
    public RawEncoder dwx;

    /**
     * Expansion 1: va
     */
    public DcMotorEx verticalArm;

    /**
     * Expansion 0: ha
     */
    public DcMotorEx horizontalArm;

    /**
     * Control Servo 2: lc
     */
    public Servo leftClaw;

    /**
     * Control Servo 1: rc
     */
    public Servo rightClaw;

    /**
     * Control Servo 0: cr
     */
    public Servo clawRotator;

    /**
     * Control Servo 3: bk
     */
    public Servo basketRotator;

    /**
     * Control Servo 5: Blinkin Lights "lights"
     */
    public RevBlinkinLedDriver lights;

    /**
     * Control Digital 1: Limit Switch "bottom"
     */
    public TouchSensor bottomLimit;

    /**
     * RoadRunner drive model
     */
    public DriveModel driveModel;

    /**
     * RoadRunner motion profile
     */
    public MotionProfile motionProfile;

    /**
     * RoadRunner Mecanum coefficients
     */
    public MecanumGains mecanumGains;

    /**
     * Roadrunner Tri-Wheel Localiser Coefficients
     */
    public ThreeWheelLocalizer.Params localiserParams;

    @Override
    protected void onRuntime() {
        // Motor directions configured to work with current config
        fl = getHardware("fl", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        bl = getHardware("bl", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        fr = getHardware("fr", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        br = getHardware("br", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        imu = getLazyImu(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));

        dwleft = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        dwright = getHardware("fr", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        dwx = getHardware("bl", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));

        verticalArm = getHardware("va", DcMotorEx.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        horizontalArm = getHardware("ha", DcMotorEx.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));

        leftClaw = getHardware("lc", Servo.class);
        rightClaw = getHardware("rc", Servo.class);

        clawRotator = getHardware("cr", Servo.class);
        basketRotator = getHardware("bk", Servo.class);

        // Fancy lights
        lights = getHardware("lights", RevBlinkinLedDriver.class);

        // TODO: tune
        driveModel = new DriveModel.Builder()
                .build();
        motionProfile = new MotionProfile.Builder()
                .build();
        mecanumGains = new MecanumGains.Builder()
                .build();
        localiserParams = new ThreeWheelLocalizer.Params.Builder()
                .build();
    }
}
