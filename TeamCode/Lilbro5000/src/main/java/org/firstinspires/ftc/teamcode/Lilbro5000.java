package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.MecanumLocalizer;
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
 * @author Ziya, 2025
 */
public class Lilbro5000 extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;
    public Actuator intake;
    public Actuator outtake;
    public Actuator transfer;
    // .....................................................

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("front_left", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.bl = getHardware("back_left", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.br = getHardware("back_right", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.fr = getHardware("front_right", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.intake = getHardware("intake", DcMotor.class);
        hw.outtake = getHardware("outtake", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        hw.transfer = getHardware("trigger", DcMotor.class);

        hw.imu = getHardware("imu", IMUEx.class, (d) ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                ))));

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(100 / 5185.0) // 0.0192864030858245
                .setLateralInPerTick(100 / 4295.0) // 0.0232828870779977
                .setTrackWidthTicks(1572.805293895485)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setKs(1.1388974824473665)
                .setKv(0.004334085797658988)
                .setKa(0.0005)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(2)
                .setLateralGain(2)
                .setHeadingGain(3)
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu, hardwareMap.voltageSensor)
                .withName("Drive");
        MecanumLocalizer localizer = (MecanumLocalizer) drive.getLocalizer();
        localizer.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        intake = new Actuator(hw.intake).withName("Intake");
        outtake = new Actuator(hw.outtake).withName("Outtake");
        transfer = new Actuator(hw.transfer).withName("Transfer");
    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {
        /**
         * Internally mounted on Control Hub I2C 0
         */
        public IMU imu;
        /**
         * Expansion 0: back_left
         */
        public DcMotor bl;
        /**
         * Expansion 1: front_left
         */
        public DcMotor fl;
        /**
         * Expansion 2: front_right
         */
        public DcMotor fr;
        /**
         * Expansion 3: back_right
         */
        public DcMotor br;
        /**
         * Control 1: intake
         */
        public DcMotor intake;
        /**
         * Control 0: outtake
         */
        public DcMotor outtake;
        /**
         * Control 2: outtake
         */
        public DcMotor transfer;
    }
}

