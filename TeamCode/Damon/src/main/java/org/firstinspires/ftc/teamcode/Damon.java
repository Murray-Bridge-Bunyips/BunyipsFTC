package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.DegreesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.PinpointLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Your Name, Year // TODO: Set this to your name and the current year!
 */
@Config
public class Damon extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;

    public Actuator intake;

    public Actuator shooter;

    public Actuator transferWheel;

    public static double shooter_kP = 15, shooter_kV = 0.9;
    // TODO: Add more subsystems here according to your robot's needs
    // .....................................................

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("fl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
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
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                ))));

        hw.pinpoint = getHardware("pinpoint", GoBildaPinpointDriver.class);



        //hw.perp = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        //hw.par = getHardware("fr", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));

        PinpointLocalizer.Params localizerParams = new PinpointLocalizer.Params.Builder()
                .setInitialParDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .setInitialPerpDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .setParYTicks(239.67254509780497)
                .setPerpXTicks(-2371.9364863392334)
                // More to be filled out later by the Tuning steps ...
                .build();

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(100.0 / 48550.0) //0.0020618556701031
                .setLateralInPerTick(0.001820115995937997)
                .setTrackWidthTicks(7400.158143723337)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(35))
                .setMaxAngVel(DegreesPerSecond.of(180))
                .setKv(0.0004661099306658172)
                .setKs(0.8428631128385451)
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
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        intake = new Actuator(hw.intake)
                .withName("Intake");

        hw.shooter = getHardware("shooter", Motor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            PIDFController pidf = new PIDFController(shooter_kP, 0, 0, shooter_kV);
            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() ->
                    pidf.setPIDF(shooter_kP, 0, 0, shooter_kV)));
            pidf.setTolerance(200);
            d.setRunUsingEncoderController(1, 2300, pidf);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        });

        shooter = new Actuator(hw.shooter)
                .withName("Shooter");

        hw.transferWheel = getHardware("transferWheel", DcMotor.class, (d) -> {
            // TODO: Set the direction of the intake motor here
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        transferWheel = new Actuator(hw.transferWheel)
                .withName("TransferWheel");


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

        public DcMotorSimple transferWheel;



    }
}
