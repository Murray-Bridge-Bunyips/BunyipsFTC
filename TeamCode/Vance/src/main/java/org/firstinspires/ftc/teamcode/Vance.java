package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Degrees;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.CompositeController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.ff.ArmFeedforward;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.ThreeWheelLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.accumulators.PeriodicIMUAccumulator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.EncoderTicks;

/**
 * FTC 22407 INTO THE DEEP 2024-2025 robot configuration and subsystems
 *
 * @author Lachlan Paul, 2024
 */
@Config
public class Vance extends RobotConfig {
    /**
     * Shoulder kP
     */
    public static double sh_kP = 0.0333;
    /**
     * Shoulder Cos
     */
    public static double sh_Cos = 0.4;
    /**
     * Shoulder TPS
     */
    public static double sh_TPS = 400;
    /**
     * Shoulder reduction
     */
    public static double sh_Reduc = 5.14285714286;

    // todo: toon deez
    /**
     * Elbow kP
     */
    public static double el_kP = 0.03;
    /**
     * Elbow Cos
     */
    public static double el_Cos = 0.65;
    /**
     * Elbow TPS
     */
    public static double el_TPS = 100; // todo
    /**
     * Elbow reduction
     */
    public static double el_Reduc = 8.5;

    /**
     * Positions for TeleOp's arm
     */
    public int[] shoulderPositions = {
            0, 100, 200  // todo
    };
    public int[] elbowPositions = {
            0, 100, 200 // todo
    };

    /**
     * Vance hardware
     */
    public final Hardware hw = new Hardware();
    /**
     * Mecanum drive
     */
    public MecanumDrive drive;

    /**
     * The shoulder of our arm
     */
    public HoldableActuator shoulder;
    /**
     * The elbow of our arm
     */
    public HoldableActuator elbow;
    /**
     * Intake
     */
    public Switch intake;

    @Override
    protected void onRuntime() {
        // giulio messed with the robot so i have to change all the directions
        hw.fl = getHardware("fl", DcMotorEx.class, (d) -> {
//            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.bl = getHardware("bl", DcMotorEx.class, (d) -> {
//            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.fr = getHardware("fr", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.br = getHardware("br", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.imu = getHardware("imu", IMUEx.class, (d) ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                ))));

        hw.dwleft = getHardware("br", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        hw.dwright = getHardware("fl", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.FORWARD));
        hw.dwx = getHardware("bl", RawEncoder.class, (d) -> d.setDirection(DcMotorSimple.Direction.REVERSE));

        hw.intake = getHardware("in", CRServo.class);
        hw.shoulder = getHardware("sh", Motor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
            EncoderTicks.Generator angleGen = EncoderTicks.createGenerator(d, sh_Reduc);
            PIDController pid = new PController(sh_kP);
            ArmFeedforward ff = new ArmFeedforward(0.0, sh_Cos, 0.0, 0.0, angleGen::getAngle, angleGen::getAngularVelocity, angleGen::getAngularAcceleration);
            CompositeController c = pid.compose(ff, Double::sum);
            d.setRunToPositionController(c);
            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
                c.setCoefficients(sh_kP, 0.0, 0.0, 0.0, 0.0, sh_Cos, 0.0, 0.0);
                o.telemetry.addData("Shoulder Angle", angleGen.getAngle().in(Degrees));
            }));
        });
        hw.elbow = getHardware("el", Motor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
            EncoderTicks.Generator angleGen = EncoderTicks.createGenerator(d, el_Reduc);
            PIDController pid = new PController(el_kP);
            ArmFeedforward ff = new ArmFeedforward(0.0, el_Cos, 0.0, 0.0, angleGen::getAngle, angleGen::getAngularVelocity, angleGen::getAngularAcceleration);
            CompositeController c = pid.compose(ff, Double::sum);
            d.setRunToPositionController(c);
            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
                c.setCoefficients(el_kP, 0.0, 0.0, 0.0, 0.0, el_Cos, 0.0, 0.0);
                o.telemetry.addData("Elbow Angle", angleGen.getAngle().in(Degrees));
            }));
        });

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(122.5 / 61697.0)
                .setLateralInPerTick(0.001498916323279902)
                .setTrackWidthTicks(7670.3069265030135)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setKv(0.00035)
                .setKs(1)
                .setKa(0.00007)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(2)
                .setLateralGain(2)
                .setHeadingGain(4)
                .build();
        ThreeWheelLocalizer.Params localiserParams = new ThreeWheelLocalizer.Params.Builder()
                .setPar0YTicks(-1274.4310945248199)
                .setPar1YTicks(1355.6339929262751)
                .setPerpXTicks(-3361.673151430961)
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu, hardwareMap.voltageSensor)
                .withLocalizer(new ThreeWheelLocalizer(driveModel, localiserParams, hw.dwleft, hw.dwright, hw.dwx))
                .withAccumulator(new PeriodicIMUAccumulator(hw.imu.get(), Seconds.of(5)))
                .withName("Drive");
        shoulder = new HoldableActuator(hw.shoulder)
//                .withUserSetpointControl((dt) -> dt * sh_TPS)  // todo
                .withTolerance(10, true)
                .withUpperLimit(900)
                .withHomingPower(0.7)
                .withName("Shoulder");
        elbow = new HoldableActuator(hw.elbow)
                .withUserSetpointControl((dt) -> dt * el_TPS)
//                .withPowerClamps(-0.5, 0.5)
                .withTolerance(10, true)
                .withTolerance(7, true)
                .withName("Elbow");
    }

    public static class Hardware {
        /**
         * Internally mounted on I2C C0 "imu"
         */
        public IMUEx imu;

        /**
         * Control 2: fr
         */
        public DcMotorEx /*Are you*/ fr /*Or jk*/;

        /**
         * Control 1: fl
         */
        public DcMotorEx fl;

        /**
         * Control 0: bl
         */
        public DcMotorEx bl;

        /**
         * Control 3: br
         */
        public DcMotorEx br;

        /**
         * Control 3: br
         */
        public RawEncoder dwleft;

        /**
         * Control 2: fr
         */
        public RawEncoder dwright;

        /**
         * Control 1: fl
         */
        public RawEncoder dwx;

        /**
         * Expansion 2: sh
         */
        public Motor shoulder;

        /**
         * Expansion 1: el
         */
        public Motor elbow;

        /**
         * Control Servo 2: in
         */
        public CRServo intake;
    }
}
