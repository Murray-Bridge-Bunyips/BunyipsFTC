package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Centimeters;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Inches;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.tuning.MotorDirection;

import java.util.Collections;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.TankLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.TankGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.TankDrive;

/**
 * Generic minibot configuration with two Core Hex motors and upwards logo Control Hub orientation.
 * <p>
 * Left wheel on "l".
 * Right wheel on "r".
 * IMU on "imu".
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

    @Override
    protected void onRuntime() {
        left = getHardware("l", DcMotor.class, d -> d.setDirection(Constants.LEFT_WHEEL_DIRECTION));
        right = getHardware("r", DcMotor.class, d -> d.setDirection(Constants.RIGHT_WHEEL_DIRECTION));
        imu = getHardware("imu", IMU.class, d -> d.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        // Assumes the hub is mounted with the logo facing upwards for +Z axis.
                        // https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html
                        // Note that the TankDrive instance uses encoders to determine rotation, if this is inaccurate
                        // set the accumulator on the TankDrive to a CustomAccumulator using IMU readings for heading.
                        // The IMU is used exclusively during tuning as a control.
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        // USB direction on an upwards facing logo does not impact the Z axis, we don't use the other axes
                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                ))
        ));

        DriveModel dm = new DriveModel.Builder()
                .setInPerTick(Inches.convertFrom(333, Centimeters)/2269)
                .setTrackWidthTicks(211.84157714243102)
                .build();
        MotionProfile mp = new MotionProfile.Builder()
                //TODO: robot thinks its turning more than it think it should
                .setKv(0.018178431094225414)
                .setKv(1.2328893171060802)
                .build();
        TankGains tg = new TankGains.Builder()
                .build();
        drive = new TankDrive(dm, mp, tg, Collections.singletonList(left), Collections.singletonList(right), imu, hardwareMap.voltageSensor);

        TankLocalizer localizer = (TankLocalizer) drive.getLocalizer();
        localizer.leftEncs.get(0).setDirection(Constants.LEFT_WHEEL_DIRECTION);
        localizer.rightEncs.get(0).setDirection(Constants.RIGHT_WHEEL_DIRECTION);
    }

    /**
     * Constants that can vary per minibot construction. Ensure to build with the correct configuration.
     * The {@link MotorDirection} OpMode can assist in this.
     */
    @Config
    public static class Constants {
        public static DcMotorSimple.Direction LEFT_WHEEL_DIRECTION = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction RIGHT_WHEEL_DIRECTION = DcMotorSimple.Direction.FORWARD;
    }
}
