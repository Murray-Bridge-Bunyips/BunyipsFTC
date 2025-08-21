package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.TwoWheelLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
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
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                ))));

        hw.perp = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        hw.par = getHardware("fr", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));

        DriveModel driveModel = new DriveModel.Builder()
                // TODO: Fill out as necessary according to the RoadRunner Tuning section of the BunyipsLib Wiki
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                // TODO: Fill out as necessary according to the RoadRunner Tuning section of the BunyipsLib Wiki
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                // TODO: Fill out as necessary according to the RoadRunner Tuning section of the BunyipsLib Wiki
                .build();
        TwoWheelLocalizer.Params localizerPrams = new TwoWheelLocalizer.Params.Builder()
                .build();
        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu, hardwareMap.voltageSensor)
                .withLocalizer(new TwoWheelLocalizer(driveModel, localizerPrams, hw.par, hw.perp, hw.imu))
                .withName("Drive");
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



    }
}
