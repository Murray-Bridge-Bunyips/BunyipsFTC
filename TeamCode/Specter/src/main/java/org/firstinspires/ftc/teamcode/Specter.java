package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.PinpointLocalizer;


/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for--
 *
 * @author Lucas Sacco, 2026
 */
public class Specter extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;

    public Vision Webcam;

    public Actuator intake;

    public Actuator transfer;

    public Actuator leftShooter;

    public Actuator rightShooter;

    public Switch gate;

    public Switch brake;

    public Switch sweeper;


    @Override
    protected void onRuntime() {

        hw.fl = getHardware("fl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        hw.bl = getHardware("bl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
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
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                ))));

        hw.pinpoint = getHardware("pinpoint", GoBildaPinpointDriver.class);

        PinpointLocalizer.Params localizerParams = new PinpointLocalizer.Params.Builder()
                .setInitialParDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED) // Set directions here!
                .setInitialPerpDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
                .setParYTicks(-1450.142830071315)
                .setPerpXTicks(-3410.836018049558)
                .build();

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(122.0 / 62273)
                .setLateralInPerTick(0.0016817682261912336)
                .setTrackWidthTicks(7021.528696417151)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setKv(0.00035733446351793926)
                .setKs(0.9090539145487959)
                .setKa(0.00004)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(2.5)
                .setLateralGain(3)
                .setHeadingGain(4)
                .setPoseHolding(true)
                .build();
        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu)
                .withLocalizer(new PinpointLocalizer(driveModel, localizerParams, hw.pinpoint))
                .withName("Drive");


        hw.intake = getHardware("intake", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        intake = new Actuator(hw.intake)
                .withName("Intake");

        hw.transfer = getHardware("transfer", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        transfer = new Actuator(hw.transfer)
                .withName("Transfer");

        hw.leftShooter = getHardware("leftShooter", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        leftShooter = new Actuator(hw.leftShooter)
                .withName("LeftShooter");

        hw.rightShooter = getHardware("rightShooter", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        rightShooter = new Actuator(hw.rightShooter)
                .withName("RightShooter");

        hw.gate = getHardware("gate", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.FORWARD);
            d.scaleRange(0.265, 0.524);
        });

        gate = new Switch(hw.gate)
                .withName("Gate");

        hw.brake = getHardware("brake", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.FORWARD);
            d.scaleRange(0.5, 0.75);
        });

        brake = new Switch(hw.brake)
                .withName("Break");

        hw.sweeper = getHardware("sweeper", Servo.class, (d) -> {
            d.setDirection(Servo.Direction.FORWARD);
            d.scaleRange(0.29, 0.99);
        });

        sweeper = new Switch(hw.sweeper)
                .withName("Sweeper");



    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {
        // TODO: Rewrite the comments above each hardware declaration to match your port and name configuration (e.g <Control/Expansion> <port>: <name in DS>)
        //   It is strongly recommended to do this as hardware mappings may erase themselves unexpectedly.
        /**
         * Internally mounted on Control Hub I2C 0
         */
        public IMU imu;
        /**
         * Control 0: fl
         */
        public DcMotor fl;
        /**
         * Control 1: bl
         */
        public DcMotor bl;
        /**
         * Control 2: br
         */
        public DcMotor br;
        /**
         * Control 3: fr
         */
        public DcMotor fr;

        public RawEncoder par;

        public RawEncoder perp;

        public GoBildaPinpointDriver pinpoint;

        public WebcamName webcam;

        public DcMotorSimple intake;

        public DcMotorSimple transfer;

        public DcMotorSimple leftShooter;

        public DcMotorSimple rightShooter;

        public Servo gate;

        public Servo brake;

        public Servo sweeper;


    }
}