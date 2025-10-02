package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.MecanumLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.PinpointLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

@Config
public class Jonas extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: bl <sub>yaoi motor</sub>
         */
        public DcMotor backLeft;

        /**
         * Control 1: br
         */
        public DcMotor backRight;

        /**
         * Control 2: fr
         */
        public DcMotor frontRight;

        /**
         * Control 3: fl
         */
        public DcMotor frontLeft;

        /**
         * Control ?: ??
         */
//        public RawEncoder dwPerpendicular;

        /**
         * Control ?: ??
         */
//        public RawEncoder dwParallel;



        /**
         * Expansion 0: output
         */
        public DcMotor output;

        /**
         * Expansion 1: intake
         */
        public DcMotor intake;

        /**
         * Internally connected
         */
        public IMUEx imu;

        /**
         * Pinpoint
         */
        public GoBildaPinpointDriver pinpoint;
    }

    /**
     * 4-Wheels MecanumDrive
     */
    public MecanumDrive drive;

    /**
     * Output Rotator
     */
    public Actuator output;

    /**
     * Intake Rotator
     */
    public Actuator intake;

    /**
     * [Mechanism Name] [Mechanism Class]
     */


    public final Hardware hw = new Hardware();

    @Override
    protected void onRuntime() {
        hw.frontLeft = getHardware("fl", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);
        });
        hw.backLeft = getHardware("bl", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);
        });

        hw.frontRight = getHardware("fr", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        hw.backRight = getHardware("br", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));

        hw.imu = getHardware("imu", IMUEx.class, d ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                ))));

        hw.output = getHardware("output", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        hw.intake = getHardware("intake", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));

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
//        PinpointLocalizer.Params localiserParams = new PinpointLocalizer.Params.Builder()
//                .setInitialParDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
//                .setInitialPerpDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
//                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.frontLeft, hw.backLeft, hw.backRight, hw.frontRight, hw.imu, hardwareMap.voltageSensor)
//                .withLocalizer(new PinpointLocalizer(driveModel, localiserParams, pinpoint));
                .withName("drive");

        MecanumLocalizer localizer = (MecanumLocalizer) drive.getLocalizer();
        localizer.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        output = new Actuator(hw.output)
                .withName("output");

        intake = new Actuator(hw.intake)
                .withName("intake");
    }
}