package org.murraybridgebunyips.vance;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.DegreesPerSecond;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.InchesPerSecond;
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
 * FTC 22407 INTO THE DEEP 2024-2025 robot configuration
 *
 * @author Lachlan Paul, 2024
 */
public class Vance extends RobotConfig {
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
            d.setDirection(DcMotorSimple.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        br = getHardware("br", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
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
                .setTicksPerRev(537.6)
                .setMaxRPM(312)
                .setRunUsingEncoder(false)
                .setWheelRadius(Inches.of(2))
                .setGearRatio(1.0)
                .setTrackWidth(Inches.of(16.89))
                .setMaxVel(InchesPerSecond.of(53)) // ~1.34 m/s
                .setMaxAccel(InchesPerSecond.per(Second).of(53))
                .setMaxAngVel(DegreesPerSecond.of(180))
                .setMaxAngAccel(DegreesPerSecond.per(Second).of(180))
                .setKV(0.015)
                .setKStatic(0.06422)
                .setKA(0.001)
                .build();

        mecanumCoefficients = new MecanumCoefficients.Builder()
                .setLateralMultiplier(1.0)
                .setTranslationalPID(new PIDCoefficients(8, 0, 0))
                .setHeadingPID(new PIDCoefficients(10, 0, 0))
                .build();
    }
}
