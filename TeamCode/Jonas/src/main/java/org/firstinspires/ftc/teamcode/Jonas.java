package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.components.DoubleJointedArm;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

@Config
public class Jonas extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: bl <sub>yaoi motor</sub>
         */
        public DcMotor backLeft;

        /**
         * Control 1: fl
         */
        public DcMotor frontLeft;

        /**
         * Control 2: br
         */
        public DcMotor backRight;

        /**
         * Control 3: fr
         */
        public DcMotor frontRight;


        /**
         * Control 2: br
         */
        public RawEncoder dwPerpendicular;

        /**
         * Control 3: fr
         */
        public RawEncoder dwParallel;


        /**
         * Control 1: shoulder
         */
        public DcMotor shoulder;

        /**
         * Control 2: elbow
         */
        public DcMotor elbow;

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
     * Arm DoubleJointedArm
     */
    public DoubleJointedArm arm;

    /**
     * [Mechanism Name] [Mechanism Class]
     */


    public final Hardware hw = new Hardware();

    @Override
    protected void onRuntime() {
        hw.frontLeft = getHardware("fl", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        hw.backLeft = getHardware("bl", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));

        hw.frontRight = getHardware("fr", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.backRight = getHardware("br", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });

        hw.shoulder = getHardware("shoulder", DcMotorEx.class);
        hw.elbow = getHardware("elbow", DcMotorEx.class);

        hw.imu = getHardware("imu", IMUEx.class, d ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                ))));

        // roadrunner template
        DriveModel driveModel = new DriveModel.Builder()
//                .setInPerTick()
//                .setLateralInPerTick()
//                .setTrackWidthTicks()
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
//                .setKv()
//                .setKs()
//                .setKa()
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
//                .setAxialGain()
//                .setLateralGain()
//                .setHeadingGain()
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.frontLeft, hw.backLeft, hw.backRight, hw.frontRight, hw.imu, hardwareMap.voltageSensor)
                .withName("drive");

        arm = new DoubleJointedArm(hw)
                .withName("arm");
    }
}