package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.DegreesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.InvertibleTouchSensor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.PinpointLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.DualServos;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToAprilTagTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.processors.AprilTag;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Lucas Sacco, 2025
 */

@Config
public class DamonV2 extends RobotConfig{
    public final DamonV2.Hardware hw = new DamonV2.Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;
    public Actuator intake;

    public Actuator shooter;
    public Actuator transferLeft;
    public Actuator transferRight;
    public Switch hoodAdjustment;
    public DualServos kicker;
    public Vision webcam;
    public InvertibleTouchSensor touchSensor;
    public Switch gate;

    public AprilTag aprilTag;

    public static PIDFCoefficients ALIGN_TO_APRILTAG_PIDF_COEFFICIENTS = new PIDFCoefficients(0.03, 0, 0, 0);

    public double kP = 4;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0.95;

    // .....................................................

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("fl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });


        hw.bl = getHardware("bl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });


        hw.br = getHardware("br", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });


        hw.fr = getHardware("fr", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        hw.imu = getHardware("imu", IMUEx.class, (d) ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ))));

        hw.pinpoint = getHardware("pinpoint", GoBildaPinpointDriver.class);



        //hw.perp = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        //hw.par = getHardware("fr", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));





        PinpointLocalizer.Params localizerParams = new PinpointLocalizer.Params.Builder()
                .setInitialParDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED) // Set directions here!
                .setInitialPerpDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
                .setParYTicks(1206.0726883094928)
                .setPerpXTicks(-3944.7404291749253)
                // More to be filled out later by the Tuning steps ...
                .build();

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(50.0 / 25855.0) //0.00193386192
                .setLateralInPerTick(0.0013549917355816688)
                .setTrackWidthTicks(7945.50527676077)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(35))
                .setMaxAngVel(DegreesPerSecond.of(180))
                .setKv(0.0004028819398133126)
                .setKs(0.8844890964508565)
                .setKa(0.00005)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(2.5)
                .setLateralGain(3)
                .setHeadingGain(4)
                .setPoseHolding(true)
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu, hardwareMap.voltageSensor)
                .withLocalizer(new PinpointLocalizer(driveModel, localizerParams, hw.pinpoint))
                .withName("Drive");

        hw.intake = getHardware("intake", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        intake = new Actuator(hw.intake)
                .withName("Intake");




        hw.shooter = getHardware("shooter", Motor.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);

            PIDFController pidf = new PIDFController(kP, kI, kD, kF);
            d.setRunUsingEncoderController(1, 1700, pidf);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            });

        shooter = new Actuator(hw.shooter)
                .withName("Shooter");



        hw.transferLeft = getHardware("transferLeft", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        transferLeft = new Actuator(hw.transferLeft)
                .withName("TransferLeft");

        hw.transferRight = getHardware("transferRight", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        transferRight = new Actuator(hw.transferRight)
                .withName("TransferRight");

        hw.hoodAdjustment = getHardware("hoodAdjustment", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.FORWARD);
            d.scaleRange(0.28, 0.4);
        });

        hoodAdjustment = new Switch(hw.hoodAdjustment)
                .withName("HoodAdjustment");




        //May need to change the direction of the servos and need to change the scale range
        hw.leftKicker = getHardware("leftKicker", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.FORWARD);
            d.scaleRange(0.58, 1.0);
        });
        hw.rightKicker = getHardware("rightKicker", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.REVERSE);
            d.scaleRange(0.0, 0.42);
        });

        kicker = new DualServos(hw.leftKicker, hw.rightKicker)
                .withName("Kicker");

        hw.touchSensor = getHardware("maxBalls", TouchSensor.class);

        touchSensor = new InvertibleTouchSensor(hw.touchSensor);

        hw.gate = getHardware("gate", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.REVERSE);
            d.scaleRange(0.4, 0.524);
        });

        gate = new Switch(hw.gate)
                .withName("Gate");








    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {

        /**
         * Expansion 0: br
         */
        public DcMotor fl;
        /**
         * Expansion 1: bl
         */
        public DcMotor fr;

        /**
         * Expansion 2: fl
         */
        public DcMotor br;

        /**
         * Expansion 3: fr
         */
        public DcMotor bl;

        public IMU imu;

        public RawEncoder par;

        public RawEncoder perp;

        public DcMotorSimple intake;

        public GoBildaPinpointDriver pinpoint;

        public Motor shooter;

        public DcMotorSimple transferLeft;

        public DcMotorSimple transferRight;

        public Servo hoodAdjustment;

        public Servo rightKicker;

        public Servo leftKicker;

        public WebcamName webcam;

        public TouchSensor touchSensor;

        public Servo gate;

        public Servo idk;

    }
}
