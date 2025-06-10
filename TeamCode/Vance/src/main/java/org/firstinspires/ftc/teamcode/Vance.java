package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Amps;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.ThreeWheelLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.accumulators.PeriodicIMUAccumulator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;

/**
 * FTC 22407 INTO THE DEEP 2024-2025 robot configuration and subsystems
 *
 * @author Lachlan Paul, 2024
 */
@RobotConfig.AutoInit
public class Vance extends RobotConfig {
    public static final int INTAKE = 1;
    public static final int EJECT = -1;

    @Config
    public static class ShoulderConstants {
        public static double kP = 0.01, kI = 0.0, kD = 0.0, TPS = 700;
    }

    @Config
    public static class ElbowConstants {
        public static double kP = 0.015, kI = 0.0, kD = 0.0, TPS = 500;
    }

    public static Vance instance = new Vance();
    public final Hardware hw = new Hardware();
    public MecanumDrive drive;
    public HoldableActuator shoulder;
    public HoldableActuator elbow;
    public Actuator intake;
    public ParallelTaskGroup wholeArmUp;

    @Override
    protected void onRuntime() {
        // giulio messed with the robot so i have to change all the directions
        hw.fl = getHardware("fl", DcMotorEx.class, (d) -> {
//            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.bl = getHardware("bl", DcMotorEx.class, (d) -> {
//            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.fr = getHardware("fr", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.br = getHardware("br", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.imu = getHardware("imu", IMUEx.class, (d) ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                ))));

        hw.dwleft = getHardware("bl", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        hw.dwright = getHardware("fr", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        hw.dwx = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));

        hw.intake = getHardware("in", CRServo.class);
        hw.shoulder = getHardware("sh", Motor.class, (d) -> {
            PIDController pid = new PIDController(ShoulderConstants.kP, ShoulderConstants.kI, ShoulderConstants.kD);
            d.setRunToPositionController(pid);
            BunyipsOpMode.getInstance().onActiveLoop(() -> pid.setPID(ShoulderConstants.kP, ShoulderConstants.kI, ShoulderConstants.kD));
        });
        hw.elbow = getHardware("el", Motor.class, (d) -> {
            PIDController pid = new PIDController(ElbowConstants.kP, ElbowConstants.kI, ElbowConstants.kD);
            d.setRunToPositionController(pid);
            BunyipsOpMode.getInstance().onActiveLoop(() -> pid.setPID(ElbowConstants.kP, ElbowConstants.kI, ElbowConstants.kD));
        });

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(100.0 / 50775.0) // 0.001969473
                .setLateralInPerTick(0.0015483626658575386)
                .setTrackWidthTicks(7138.888497070814)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setKs(1.2600068039530363)
                .setKv(0.00034)
                .setKa(0.000035)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(2)
                .setLateralGain(2)
                .setHeadingGain(4)
                .build();
        ThreeWheelLocalizer.Params localiserParams = new ThreeWheelLocalizer.Params.Builder()
                .setPar0YTicks(-1968.748851900296)
                .setPar1YTicks(1616.5687706480305)
                .setPerpXTicks(-2772.2243434435713)
                .build();
        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu, hardwareMap.voltageSensor)
                .withLocalizer(new ThreeWheelLocalizer(driveModel, localiserParams, hw.dwleft, hw.dwright, hw.dwx))
                .withAccumulator(new PeriodicIMUAccumulator(hw.imu.get(), Seconds.of(5)))
                .withName("Drive");

        shoulder = new HoldableActuator(hw.shoulder)
                .withUserSetpointControl((dt) -> ShoulderConstants.TPS * dt)
                .withOvercurrent(Amps.of(6), Seconds.of(2))
                .withMaxSteadyStateTime(Seconds.of(5))
                .withName("Shoulder");
        elbow = new HoldableActuator(hw.elbow)
                .withUserSetpointControl((dt) -> ElbowConstants.TPS * dt)
                .withOvercurrent(Amps.of(6), Seconds.of(2))
                .withMaxSteadyStateTime(Seconds.of(5))
                .withHomingPower(1)
                .withName("Elbow");
        intake = new Actuator(hw.intake);

        wholeArmUp = new ParallelTaskGroup(
            shoulder.tasks.goToProfiled(845),
            elbow.tasks.goToProfiled(180)
        );
    }

    public static class Hardware {
        /**
         * Internally mounted on I2C C0 "imu"
         */
        public IMUEx imu;

        /**
         * Control 3: fr
         */
        public DcMotorEx /*Are you*/ fr /*Or jk*/;

        /**
         * Control 1: fl
         */
        public DcMotorEx fl;

        /**
         * Control 0: bl
         */
        public DcMotorEx bl;

        /**
         * Control 2: br
         */
        public DcMotorEx br;

        /**
         * Control 0: bl
         */
        public RawEncoder dwleft;

        /**
         * Control 3: fr
         */
        public RawEncoder dwright;

        /**
         * Control 2: br
         */
        public RawEncoder dwx;

        /**
         * Expansion 2: sh
         */
        public Motor shoulder;

        /**
         * Expansion 1: el
         */
        public Motor elbow;

        /**
         * Expansion Servo 0: in
         */
        public CRServo intake;
    }
}
