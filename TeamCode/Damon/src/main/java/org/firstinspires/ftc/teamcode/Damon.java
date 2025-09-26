package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.DegreesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.DegreesPerSecondPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecondPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Meters;

import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.PinpointLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.TwoWheelLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Your Name, Year // TODO: Set this to your name and the current year!
 */
public class Damon extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;

    public Actuator intake;

    public Actuator shooter;
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

        PinpointLocalizer.Params localizerParams = (PinpointLocalizer.Params) new PinpointLocalizer.Params.Builder()
                .setInitialParDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
                .setInitialPerpDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
                .setParYTicks(-2966.047427958696)
                .setPerpXTicks(-5033.877470065803)
                // More to be filled out later by the Tuning steps ...
                .build();

        DriveModel driveModel = new DriveModel.Builder()
                .setDistPerTick(Meters.of(1), 110929)
                .setLateralInPerTick(0.0003088970325877977)
                .setTrackWidthTicks(16265.454853913478)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(35))
                .setMaxAngVel(DegreesPerSecond.of(180))
                .setKv(0.0002164422162730806)
                .setKs(0.6046451184369461)
                .setKa(0.00001000)
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

        hw.shooter = getHardware("shooter", DcMotor.class, (d) -> {
            // TODO: Set the direction of the intake motor here
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        shooter = new Actuator(hw.shooter)
                .withName("Shooter");


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

        public DcMotorSimple shooter;



    }
}
