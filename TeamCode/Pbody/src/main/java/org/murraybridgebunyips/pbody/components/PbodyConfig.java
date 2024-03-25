package org.murraybridgebunyips.pbody.components;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.RobotConfig;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.DriveConstants;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumCoefficients;

public class PbodyConfig extends RobotConfig {
    public DcMotorEx fl;
    public DcMotorEx fr;
    public DcMotorEx bl;
    public DcMotorEx br;

    public Servo ls;
    public Servo rs;

    public DcMotorEx arm;

    public Servo pl;

    public IMU imu;

    public DriveConstants driveConstants;
    public MecanumCoefficients mecanumCoefficients;


    @Override
    protected void onRuntime() {
        // TODO: encoders
        fl = getHardware("fl", DcMotorEx.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        fr = getHardware("fr", DcMotorEx.class);
        bl = getHardware("bl", DcMotorEx.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        br = getHardware("br", DcMotorEx.class);

        ls = getHardware("left_servo", Servo.class);
        rs = getHardware("right_servo", Servo.class);

        arm = getHardware("arm", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));

        pl = getHardware("drone_trigger", Servo.class);
        imu = getHardware("imu", IMU.class, (d) -> {
            boolean init = d.initialize(new IMU.Parameters(
                    new RevHubOrientationOnRobot(
                            // TODO: this is not configured properly
                            RevHubOrientationOnRobot.LogoFacingDirection.UP,
                            RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                    )
            ));
            if (!init) Dbg.error("imu failed init");
        });

        driveConstants = new DriveConstants.Builder()
                .setTicksPerRev(288)
                .setMaxRPM(125)
                .setRunUsingEncoder(false)
                .setWheelRadius(Inches.fromMM(75) / 2.0)
                .setGearRatio(1.0)
                .setTrackWidth(16)
                // ((max_rpm / 60) * gear_ratio * wheel_radius * 2 * math.pi) * 0.85
                .setMaxVel(16.426880878)
                .setMaxAccel(16.426880878)
                .setMaxAngVel(Math.PI / 2)
                .setMaxAngAccel(Math.PI / 2)
                .setKV(0.01395)
                .setKStatic(0.06311)
                .setKA(0.0015)
                .build();
        mecanumCoefficients = new MecanumCoefficients.Builder()
                .setTranslationalPID(new PIDCoefficients(8, 0, 0))
                .setHeadingPID(new PIDCoefficients(9, 0, 0))
                .build();
    }
}
