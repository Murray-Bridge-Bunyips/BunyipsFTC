package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.finder.ScoutFinder;

import java.util.Collections;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
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
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, Constants.IMU_USB_DIRECTION))
        ));

        DriveModel dm = new DriveModel.Builder()
                .build();
        MotionProfile mp = new MotionProfile.Builder()
                .build();
        TankGains tg = new TankGains.Builder()
                .build();
        drive = new TankDrive(dm, mp, tg, Collections.singletonList(left), Collections.singletonList(right), imu, hardwareMap.voltageSensor);
    }

    /**
     * Constants that can vary per minibot construction. Ensure to build with the correct configuration.
     * The {@link ScoutFinder} OpMode can assist in this.
     */
    @Config
    public static class Constants {
        public static DcMotorSimple.Direction LEFT_WHEEL_DIRECTION = DcMotorSimple.Direction.FORWARD;
        public static DcMotorSimple.Direction RIGHT_WHEEL_DIRECTION = DcMotorSimple.Direction.REVERSE;
        public static RevHubOrientationOnRobot.UsbFacingDirection IMU_USB_DIRECTION = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
    }
}
