package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.FieldTilesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.tuning.MotorDirection;

import java.util.Collections;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Distance;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.TankLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.accumulators.PeriodicIMUAccumulator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.TankGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.TankDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.vision.Vision;

/**
 * Generic minibot configuration with two Core Hex motors and upwards logo Control Hub orientation.
 * <p>
 * Left wheel on "l".
 * Right wheel on "r".
 * IMU on "imu".
 * Optional webcam on "camera".
 *
 * @author Lucas Bubner, 2025
 */
@RobotConfig.AutoInit
public class Scout extends RobotConfig {
    public static Scout instance = new Scout();

    public DcMotor left;
    public DcMotor right;
    public IMU imu;

    public TankDrive drive;
    public Vision optionalVision;

    @Override
    protected void onRuntime() {
        left = getHardware("l", DcMotor.class, d -> d.setDirection(Constants.LEFT_WHEEL_DIRECTION));
        right = getHardware("r", DcMotor.class, d -> d.setDirection(Constants.RIGHT_WHEEL_DIRECTION));
        // Optional webcam device, may be null
        CameraName optionalCamera = hardwareMap.tryGet(CameraName.class, "webcam");
        if (optionalCamera != null)
            optionalVision = new Vision(optionalCamera);
        imu = getHardware("imu", IMU.class, d -> {
            d.initialize(new IMU.Parameters(
                    new RevHubOrientationOnRobot(
                            // Assumes the hub is mounted with the logo facing upwards for +Z axis.
                            // https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html
                            // Note that the TankDrive instance uses encoders to determine rotation, with periodic
                            // readings from the IMU to ensure accuracy
                            RevHubOrientationOnRobot.LogoFacingDirection.UP,
                            // USB direction on an upwards facing logo does not impact the Z axis, we don't use the other axes
                            RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                    ))
            );
            // Scout does not hold pose information from the previous OpMode
            d.resetYaw();
        });
        // See above comment, we don't store previous information and use a relative system
        Storage.memory().lastKnownPosition = Geometry.zeroPose();

        DriveModel dm = new DriveModel.Builder()
                .setDistPerTick(Constants.DISTANCE_PUSHED, Constants.TICKS_REPORTED)
                .setTrackWidthTicks(Constants.TRACK_WIDTH_TICKS)//pepsi min
                .build();//zero taste max sugar
        MotionProfile mp = new MotionProfile.Builder()
                .setMaxWheelVel(FieldTilesPerSecond.of(1))
                .setKs(Constants.KS)
                .setKv(Constants.KV)
                .setKa(Constants.KA)
                .build();
        TankGains tg = new TankGains.Builder()
                .build();
        drive = new TankDrive(dm, mp, tg, Collections.singletonList(left), Collections.singletonList(right), imu, hardwareMap.voltageSensor)
                .withAccumulator(new PeriodicIMUAccumulator(imu, Seconds.one()))
                .withName("Drive"); // burger xd!1

        TankLocalizer localizer = (TankLocalizer) drive.getLocalizer();//giulio
        localizer.leftEncs.get(0).setDirection(Constants.LEFT_WHEEL_DIRECTION);
        localizer.rightEncs.get(0).setDirection(Constants.RIGHT_WHEEL_DIRECTION);
    }

    /**
     * Constants that can vary per minibot construction. Ensure to build with the correct configuration.
     * The {@link MotorDirection} OpMode can assist in motor directions.
     */
    @Config
    public static class Constants {
        public static DcMotorSimple.Direction LEFT_WHEEL_DIRECTION = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction RIGHT_WHEEL_DIRECTION = DcMotorSimple.Direction.FORWARD;

        public static Measure<Distance> DISTANCE_PUSHED = Centimeters.of(200);
        public static double TICKS_REPORTED = 1984;

        public static double TRACK_WIDTH_TICKS = 211.84157714243102;

        public static double KS = 1.2328893171060802;
        public static double KV = 0.018178431094225414;
        public static double KA = 0.001;
    }
}
