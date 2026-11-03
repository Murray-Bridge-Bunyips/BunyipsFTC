package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class BioBot extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: configName
         */
        public DcMotor FR;
        /**
         * Control 0: configName
         */
        public DcMotor FL;
        /**
         * Control 0: configName
         */
        public DcMotor BR;
        /**
         * Control 0: configName
         */
        public DcMotor BL;



        /**
         * Expansion 0: configName
         */
        public DcMotor expMotor;


        /**
         * Expansion 0: configName
         */
        public Servo expServo;

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
        hw.FL = getHardware("frontleft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.FR = getHardware("frontright", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.BL = getHardware("backleft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.BR = getHardware("backright", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
