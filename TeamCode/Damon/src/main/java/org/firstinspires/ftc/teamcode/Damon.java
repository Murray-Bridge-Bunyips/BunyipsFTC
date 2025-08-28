package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.DegreesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.DegreesPerSecondPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecondPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;

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

        hw.perp = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        hw.par = getHardware("fr", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(60.0 / 66157)
                .setLateralInPerTick(0.0008086379593504867)
                .setTrackWidthTicks(14340.444741047844)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(35))
                .setMaxAngVel(DegreesPerSecond.of(180))
                .setKv(0.0002)
                .setKs(0.9034678945603911)
                .setKa(0.00001000)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(2.5)
                .setLateralGain(3)
                .setHeadingGain(4)
                .setPoseHolding(true)
                .build();
        TwoWheelLocalizer.Params localizerPrams = new TwoWheelLocalizer.Params.Builder()
                .setParYTicks(-2535.679102548541)
                .setPerpXTicks(5415.298663365377)
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
