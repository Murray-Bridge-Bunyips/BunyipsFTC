package org.firstinspires.ftc.teamcode;

//import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
//import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.MecanumLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
//import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Ziya, 2025
 */
@Config
public class Lilbro5000 extends RobotConfig {
    public static double kP = 5, kV = 1;
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;
    public Actuator intake;
    public Actuator middletake;
    public Actuator outtake;
    public Actuator transfer;
//    /**
//     * Open is not lifting, closed is lifting
//     */
//    public Switch leftLifter;
//    /**
//     * Open is lifting, closed is not lifting
//     */
//    public Switch rightLifter;
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

        hw.intake = getHardware("intake", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        hw.middletake = getHardware("intake2", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        hw.outtake = getHardware("outtake", Motor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            PIDFController pidf = new PIDFController(kP, 0.0, 0.0, kV);
            d.setRunUsingEncoderController(0.95, 1800, pidf);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> pidf.setPIDF(kP, 0.0, 0.0, kV)));
        });
        hw.transfer = getHardware("trigger", DcMotor.class);

//        hw.leftLifter = getHardware("leftLifter", ServoEx.class, (d) -> {
//            d.setEndToEndTime(Seconds.of(0.8));
//            d.scaleRange(0.3, 0.55);
//        });
//        hw.rightLifter = getHardware("rightLifter", ServoEx.class, (d) -> {
//            d.setEndToEndTime(Seconds.of(0.8));
//            d.scaleRange(0.34, 0.6);
//        });

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
        middletake = new Actuator(hw.middletake).withName("Middletake");
        outtake = new Actuator(hw.outtake).withName("Outtake");
        transfer = new Actuator(hw.transfer).withName("Transfer");
//        leftLifter = new Switch(hw.leftLifter).withName("Left Lifter");
//        rightLifter = new Switch(hw.rightLifter).withName("Right Lifter");
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
         * Control 3: intake2
         */
        public DcMotor middletake;
        /**
         * Control 0: outtake
         */
        public Motor outtake;
        /**
         * Control 2: trigger
         */
        public DcMotor transfer;

//        /**
//         * Expansion 0: leftLifter
//         */
//        public ServoEx leftLifter;
//        /**
//         * Expansion 1: rightLifter
//         */
//        public ServoEx rightLifter;
    }
}

