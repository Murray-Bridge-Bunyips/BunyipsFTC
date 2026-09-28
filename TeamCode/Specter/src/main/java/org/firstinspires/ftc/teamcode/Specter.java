package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Your Name, Year // TODO: Set this to your name and the current year!
 */
public class Specter extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public MecanumDrive drive;
    // TODO: Add more subsystems here according to your robot's needs
    // .....................................................

    @Override
    protected void onRuntime() {
        // TODO: Change or confirm the hardware name "fl" is your Front Left Mecanum Motor
        hw.fl = getHardware("fl", DcMotor.class, (d) -> {
            // TODO: Set the direction of the front left motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Change or confirm the hardware name "bl" is your Back Left Mecanum Motor
        hw.bl = getHardware("bl", DcMotor.class, (d) -> {
            // TODO: Set the direction of the back left motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Change or confirm the hardware name "br" is your Back Right Mecanum Motor
        hw.br = getHardware("br", DcMotor.class, (d) -> {
            // TODO: Set the direction of the back right motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Change or confirm the hardware name "fr" is your Front Right Mecanum Motor
        hw.fr = getHardware("fr", DcMotor.class, (d) -> {
            // TODO: Set the direction of the front right motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Confirm an IMU exists on I2C Bus 0 called "imu" on the Control Hub
        hw.imu = getHardware("imu", IMUEx.class, (d) ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        // TODO: This process follows the IMU initialisation and orientation setup. Follow instructions for direction
                        //     here: https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html#axes-definition
                        RevHubOrientationOnRobot.LogoFacingDirection.UP, // TODO: Check if FIRST logo is pointing upward relative to front
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT // TODO: Check if USB is pointing left relative to front
                ))));

        DriveModel driveModel = new DriveModel.Builder()
                // TODO: Fill out as necessary according to the RoadRunner Tuning section of the BunyipsLib Wiki
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                // TODO: Fill out as necessary according to the RoadRunner Tuning section of the BunyipsLib Wiki
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                // TODO: Fill out as necessary according to the RoadRunner Tuning section of the BunyipsLib Wiki
                .build();
        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu)
                // TODO: Set Localizer/other parameters here (if required) according to the RoadRunner section of the BunyipsLib Wiki
                .withName("Drive");
    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {
        // TODO: Rewrite the comments above each hardware declaration to match your port and name configuration (e.g <Control/Expansion> <port>: <name in DS>)
        //   It is strongly recommended to do this as hardware mappings may erase themselves unexpectedly.
        /**
         * Internally mounted on Control Hub I2C 0
         */
        public IMU imu;
        /**
         * Control 0: fl
         */
        public DcMotor fl;
        /**
         * Control 1: bl
         */
        public DcMotor bl;
        /**
         * Control 2: br
         */
        public DcMotor br;
        /**
         * Control 3: fr
         */
        public DcMotor fr;
        // TODO: Add more hardware devices here according to your robot's configuration
    }
}