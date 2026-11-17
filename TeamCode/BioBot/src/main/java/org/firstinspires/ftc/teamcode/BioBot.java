package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class BioBot extends RobotConfig {
    public static class Hardware {
        /**
         * Expansion 2: frontRight
         */
        public DcMotor FR;
        /**
         * Expansion 1: frontLeft
         */
        public DcMotor FL;
        /**
         * Expansion 3: backRight
         */
        public DcMotor BR;
        /**
         * Expansion 0: backLeft
         */
        public DcMotor BL;


        /**
         * Control 0: outtake
         */
        public Motor outtake;
        /**
         * Control 1: intake
         */
        public DcMotor intake;


        /**
         * Expansion 0: depollinator
         */
        public ServoEx depollinator;


        /**
         * Control 1: depollinatorLimiter
         */
        public TouchSensor depollinatorLimiter;


        /**
         * Internally connected
         */
        public IMUEx imu;
    }

    /**
     * 4-Wheels MecanumDrive
     */
    public MecanumDrive drive;

    /**
     * Outtake Actuator
     */
    public Actuator outtake;
    /**
     * Intake Actuator
     */
    public Actuator intake;

    /**
     * Depollinator Switch
     */
    public Switch depollinator;

    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0;

    public final Hardware hw = new Hardware();

    @Override
    protected void onRuntime() {
        hw.FL = getHardware("frontLeft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.FR = getHardware("frontRight", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.BL = getHardware("backLeft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.BR = getHardware("backRight", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });

        hw.outtake = getHardware("outtake", Motor.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//            PIDFController pidf = new PIDFController(kP, kI, kD, kF);
//            d.setRunUsingEncoderController(pidf);
//
//            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
//                pidf.setCoefficients(kP, kI, kD, kF);
//
//                o.telemetry.addData("currentVelocity", d.getVelocity());
//                o.telemetry.addData("targetVelocity", pidf.getSetpoint());
//            }));
        });
        hw.intake = getHardware("intake", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });

        hw.depollinator = getHardware("depollinator", ServoEx.class, (d) -> {
            d.setEndToEndTime(Seconds.of(0.8));
            d.scaleRange(0.24, 0.56);
        });

        hw.imu = getHardware("imu", IMUEx.class, d ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                ))));

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(1)
                .setLateralInPerTick(1)
                .setTrackWidthTicks(1)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(1))
                .setKv(1)
                .setKs(1)
                .setKa(1)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(3.5)
                .setLateralGain(3.5)
                .setHeadingGain(2)
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.FL, hw.BL, hw.BR, hw.FR, hw.imu, hardwareMap.voltageSensor);

        outtake = new Actuator(hw.outtake);
        intake = new Actuator(hw.intake);

        depollinator = new Switch(hw.depollinator);
    }
}
