package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.IMU;

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
         * Control 0: configName
         */
        public DcMotor expMotor;


        /**
         * Expansion 0: depollinator
         */
        public ServoEx depollinator;

        /**
         * Internally connected
         */
        public IMUEx imu;
    }

    /**
     * 4-Wheels MecanumDrive
     */
    public MecanumDrive drive;

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

        hw.depollinator = getHardware("depollinator", ServoEx.class, (d) -> {
            d.setEndToEndTime(Seconds.of(0.8));
            d.scaleRange(0, 1);
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
    }
}
