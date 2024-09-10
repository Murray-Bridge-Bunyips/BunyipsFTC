package org.murraybridgebunyips.vance;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.DegreesPerSecond;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.InchesPerSecond;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Millimeters;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Second;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.RobotConfig;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.DriveConstants;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumCoefficients;

/**
 * Vance's config file
 *
 * @author Lachlan Paul, 2024
 */
public class VanceConfig extends RobotConfig {
    /**
     * Internally mounted on I2C C0 "imu"
     */
    public IMU imu;

    /**
     * Control 0: fr
     */
    public DcMotorEx /*Are you*/ fr /*Or jk*/;

    /**
     * Control 1: fl
     */
    public DcMotorEx fl;

    /**
     * Control 2: bl
     */
    public DcMotorEx bl;

    /**
     * Control 3: br
     */
    public DcMotorEx br;

    /**
     * Control Servo ?: Blinkin Driver
     */
    public RevBlinkinLedDriver lights;

    /**
     * RoadRunner drive constants
     */
    public DriveConstants driveConstants;
    /**
     * RoadRunner Mecanum coefficients
     */
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void onRuntime() {
        // Motor directions configured to work with current config
        fl = getHardware("fl", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        bl = getHardware("bl", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        fr = getHardware("fr", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        br = getHardware("br", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        imu = getHardware("imu", IMU.class, (d) -> {
            boolean res = d.initialize(
                    new IMU.Parameters(
                            new RevHubOrientationOnRobot(
                                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                            )
                    )
            );
            if (!res) {
                Dbg.error("IMU failed to initialise!");
            }
        });


        // Fancy lights
        lights = getHardware("lights", RevBlinkinLedDriver.class);
        driveConstants = new DriveConstants.Builder()
                .setTicksPerRev(28)
                .setMaxRPM(6000)
                .setRunUsingEncoder(false)
                .setWheelRadius(Millimeters.of(75).divide(2))
                .setGearRatio(1.0 / 13.1)
                .setTrackWidth(Inches.of(20.5))
                // ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
                .setMaxVel(InchesPerSecond.of(41.065033847087705))
                .setMaxAccel(InchesPerSecond.per(Second).of(41.065033847087705))
                .setMaxAngVel(DegreesPerSecond.of(175))
                .setMaxAngAccel(DegreesPerSecond.per(Second).of(175))
                .setKV(0.0145)
                .setKStatic(0.06422)
                .setKA(0.001)
                .build();

        mecanumCoefficients = new MecanumCoefficients.Builder()
                .setLateralMultiplier(60.0 / 54.07)
                .setTranslationalPID(new PIDCoefficients(8, 0, 0))
                .setHeadingPID(new PIDCoefficients(10, 0, 0))
                .build();
    }
}
