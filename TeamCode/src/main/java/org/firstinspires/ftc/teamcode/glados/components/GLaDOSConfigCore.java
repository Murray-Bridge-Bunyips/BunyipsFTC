package org.firstinspires.ftc.teamcode.glados.components;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.PivotMotor;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.MecanumCoefficients;
import org.firstinspires.ftc.teamcode.common.roadrunner.drive.localizers.TwoWheelTrackingLocalizerCoefficients;
import org.firstinspires.ftc.teamcode.common.roadrunner.util.Encoder;

/**
 * FTC 15215 CENTERSTAGE 2023-2024 robot configuration
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSConfigCore extends RobotConfig {

    protected static final double CORE_HEX_TICKS_PER_REVOLUTION = 288;
    protected static final double SR_GEAR_RATIO = (30.0 / 90.0) * (60.0 / 125.0);
    // USB: Webcam "webcam"
    public WebcamName webcam;
    // Expansion 0: Front Left "fl"
    public DcMotorEx fl;
    // Expansion 1: Front Right "fr"
    public DcMotorEx fr;
    // Expansion 2: Back Right "br"
    public DcMotorEx br;
    // Expansion 3: Back Left "bl"
    public DcMotorEx bl;
    // Control 0: Suspender Actuator "sa"
    public DcMotorEx sa;
    // Control 1: Suspender Rotation "sr", 30T->90T->60T->125T
    public PivotMotor sr;
    // Control Servo 0: Alignment Servo "al"
    public Servo al;
    // Control Servo 1: Left Servo "ls"
    public Servo ls;
    // Control Servo 2: Right Servo "rs"
    public Servo rs;
    // Control Servo 3: Plane Launcher "pl"
    public Servo pl;
    // Internally mounted on I2C C0 "imu"
    public IMU imu;


    // Control 2: parallelEncoder
    public Encoder parallelEncoder;

    // Control 3: perpendicularEncoder
    public Encoder perpendicularEncoder;

    public DriveConstants driveConstants;
    public TwoWheelTrackingLocalizerCoefficients localizerCoefficients;
    public MecanumCoefficients mecanumCoefficients;

    @Override
    protected void init() {
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        fl = (DcMotorEx) getHardware("fl", DcMotorEx.class);
        fr = (DcMotorEx) getHardware("fr", DcMotorEx.class);
        br = (DcMotorEx) getHardware("br", DcMotorEx.class);
        bl = (DcMotorEx) getHardware("bl", DcMotorEx.class);
        DcMotorEx SRmotor = (DcMotorEx) getHardware("sr", DcMotorEx.class);
        if (SRmotor != null) {
            sr = new PivotMotor(SRmotor, CORE_HEX_TICKS_PER_REVOLUTION, SR_GEAR_RATIO);
            sr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        sa = (DcMotorEx) getHardware("sa", DcMotorEx.class);
        al = (Servo) getHardware("al", Servo.class);
        ls = (Servo) getHardware("ls", Servo.class);
        rs = (Servo) getHardware("rs", Servo.class);
        pl = (Servo) getHardware("pl", Servo.class);
        imu = (IMU) getHardware("imu", IMU.class);

        if (fr != null)
            fr.setDirection(DcMotorSimple.Direction.FORWARD);

        if (fl != null)
            fl.setDirection(DcMotorSimple.Direction.REVERSE);

        if (br != null)
            br.setDirection(DcMotorSimple.Direction.REVERSE);

        if (bl != null)
            bl.setDirection(DcMotorSimple.Direction.REVERSE);

        if (sa != null) {
            sa.setDirection(DcMotorSimple.Direction.FORWARD);
            sa.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        if (imu == null) {
            throw new IllegalStateException("IMU is null!?");
        }

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                        )
                )
        );

        // TODO: Tune
        driveConstants = new DriveConstants.Builder()
                .setTicksPerRev(537.6)
                .setMaxRPM(312.5)
                .setRunUsingEncoder(false)
                .setWheelRadius(1.4763)
                .setGearRatio((1.0 / 5.0) * (1.0 / 4.0))
                .setTrackWidth(15.5)
                // ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.85
                .setMaxVel(41.065033847087705)
                .setMaxAccel(41.065033847087705)
                .setMaxAngVel(Math.toRadians(130.71406249999998))
                .setMaxAngAccel(Math.toRadians(130.71406249999998))
                .build();

        localizerCoefficients = new TwoWheelTrackingLocalizerCoefficients.Builder()
                .setTicksPerRev(1200)
                .setGearRatio(1)
                .setWheelRadius(Inches.fromMM(50) / 2)
                .setParallelX(-12)
                .setParallelY(-4.5)
                .setPerpendicularX(-15)
                .setPerpendicularY(0)
                .build();

        mecanumCoefficients = new MecanumCoefficients.Builder()
                .build();

        DcMotorEx pe = (DcMotorEx) getHardware("parallelEncoder", DcMotorEx.class);
        if (pe != null) {
            parallelEncoder = new Encoder(pe);
        }

        DcMotorEx ppe = (DcMotorEx) getHardware("perpendicularEncoder", DcMotorEx.class);
        if (ppe != null) {
            perpendicularEncoder = new Encoder(ppe);
            perpendicularEncoder.setDirection(Encoder.Direction.REVERSE);
        }
    }
}
