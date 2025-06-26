package org.firstinspires.ftc.teamcode;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Amps;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsLib;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.CompositeController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.ff.ArmFeedforward;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.ff.ElevatorFeedforward;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.MecanumLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.EncoderTicks;

/**
 * <font color=red>+4</font> <font color=white>Mult</font>
 * <br>
 * who am i? hmmmm, hm hm hm
 * im the joker, baby! hm hm hm
 * im the, f#%@ joker, i make cool jokes, i
 * AAAAAAAAAAAAAA, AAAAAAAAAAAAAAAA
 * THE PIANO
 * IT FELL ON ME
 * AAAAAAAAAAAAAAAAAAAA
 */
@Config
public class Joker extends RobotConfig {
    public static class Hardware {
        /**
         * Expansion 1: front_left
         */
        public DcMotor frontLeft;
        /**
         * Expansion 2: front_right
         */
        public DcMotor frontRight;
        /**
         * Expansion 0: back_left
         */
        public DcMotor backLeft;
        /**
         * Expansion 3: back_right
         */
        public DcMotor backRight;

        /**
         * Control Hub 0: intakeMotor
         */
        public Motor intakeMotor;
        /**
         * Control Hub 1: liftMotor
         */
        public Motor liftMotor;
        /**
         * Control Hub 2: hook
         */
        public DcMotor hook;
        /**
         * Control Hub 3: arm
         */
        public DcMotor ascentArm;

        /**
         * Control Hub 0: intakeAlign
         */
        public Servo intakeAlign;
        /**
         * Control Hub 1: outtakeGrip
         */
        public Servo outtakeGrip;
        /**
         * Control Hub 2: intakeGrip
         */
        public Servo intakeGrip;

        /**
         * Control Hub 0-1 (1 used): liftLimiter
         */
        public TouchSensor liftBotStop;
        /**
         * Control Hub 2-3 (3 used): intakeInStop
         */
        public TouchSensor intakeInStop;

        /**
         * Internally connected
         */
        public IMUEx imu;
    }

    /**
     * 4-Wheels MecanumDrive
     */
    public MecanumDrive drive;

    /**
     * Intake Arm HoldableActuator
     */
    public HoldableActuator intake;
    /**
     * Outtake Lift HoldableActuator
     */
    public HoldableActuator lift;
    /**
     * Ascent Arm HoldableActuator
     */
    public HoldableActuator ascentArm;

    /**
     * Outtake Grip Switch
     */
    public Switch outtakeGrip;
    /**
     * Intake Grip Switch
     */
    public Switch intakeGrip;
    /**
     * Intake Align Switch
     */
    public Switch intakeAlign;

    public static double liftkP = 0.005;
    public static double liftkI = 0.0;
    public static double liftkD = 0.0;
    public static double liftkG = 0.1;

    public final Hardware hw = new Hardware();

    //live mecanum wheel rolling on keyboard reaction:
    //Zzzzzzzzzzzzzzzzzzzzzzzzzzzssxccfvgbhnjk,l.....;///'/'

    @Override
    protected void onRuntime() {
        hw.frontLeft = getHardware("front_left", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        hw.frontRight = getHardware("front_right", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        hw.backLeft = getHardware("back_left", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));
        hw.backRight = getHardware("back_right", DcMotor.class, d -> d.setDirection(DcMotorSimple.Direction.REVERSE));

        hw.intakeMotor = getHardware("intakeMotor", Motor.class, d -> {
            //TODO: tune this once hardware changes are complete
            EncoderTicks.Generator angleGen = EncoderTicks.createGenerator(d, 0.333);
            PIDController pid = new PIDController(0.005, 0, 0.00001);
            ArmFeedforward ff = new ArmFeedforward(0, 0.1, 0, 0, angleGen::getAngle, angleGen::getAngularVelocity, angleGen::getAngularAcceleration);
            CompositeController c = new CompositeController(pid, ff, Double::sum);
            d.setRunToPositionController(c);
            //giulio was here he is also java and is way better then you at coding
        });
        hw.liftMotor = getHardware("liftMotor", Motor.class, d -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
            PIDController pid = new PIDController(liftkP, liftkI, liftkD);
            ElevatorFeedforward ff = new ElevatorFeedforward(0.0, liftkG, 0.0, 0.0, () -> 0, () -> 0);
            CompositeController c = pid.compose(ff, Double::sum);
            d.setRunToPositionController(c);
        });
        hw.hook = getHardware("hook", DcMotor.class);
        hw.ascentArm = getHardware("arm", Motor.class,
                d -> d.setRunToPositionController(new PIDController(0.01, 0, 0.00001)));

        hw.intakeAlign = getHardware("intakeAlign", Servo.class);
        hw.intakeGrip = getHardware("intakeGrip", Servo.class);
        hw.outtakeGrip = getHardware("outtakeGrip", Servo.class, d -> d.setDirection(Servo.Direction.REVERSE));

        hw.liftBotStop = getHardware("liftLimiter", TouchSensor.class);
        hw.intakeInStop = getHardware("intakeInStop", TouchSensor.class);

        hw.imu = getHardware("imu", IMUEx.class, d ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                ))));

        // roadrunner values for the robot without its ascent
        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick((141-(9*2))/6560.0)
                .setLateralInPerTick((141-(18-(1.25+1)))/4730)
                .setTrackWidthTicks(1549.125951361604)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(40))
                .setKv(0.004282941307554055)
                .setKs(1.2153614527317247)
                .setKa(0.00035)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(3.5)
                .setLateralGain(3.5)
                .setHeadingGain(2)
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.frontLeft, hw.backLeft, hw.backRight, hw.frontRight, hw.imu, hardwareMap.voltageSensor)
                .withName("drive");

        MecanumLocalizer localizer = (MecanumLocalizer) drive.getLocalizer();
        localizer.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        localizer.rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        intake = new HoldableActuator(hw.intakeMotor)
                .withBottomSwitch(hw.intakeInStop)
                .withUserSetpointControl((dt) -> 300 * dt)
                .withName("intake");

        lift = new HoldableActuator(hw.liftMotor)
                .withBottomSwitch(hw.liftBotStop)
                .withPowerClamps(-1, 1)
                .withUpperLimit(4200)
                .withOvercurrent(Amps.of(7.5), Seconds.of(1))
                .withUserSetpointControl((dt) -> 1600 * dt)
                .withTolerance(10)
                .withName("lift");
        if (BunyipsLib.getOpMode().getClass().isAnnotationPresent(Autonomous.class)) {
            lift.withTolerance(10);
        }

        // can be replaced w/ pid controller if hook motor gets an encoder (not really needed though)
        hw.hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ascentArm = new HoldableActuator(hw.ascentArm)
                .withName("ascent");
        
        outtakeGrip = new Switch(hw.outtakeGrip, 0, 0.6)
                .withName("outtake grip");

        intakeGrip = new Switch(hw.intakeGrip, 0, 1)
                .withName("intake grip");

        intakeAlign = new Switch(hw.intakeAlign, 0, 0.7)
                .withName("intake align");
    }
}