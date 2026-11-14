package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Time;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

public class ApartmentBot extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: frontLeft
         */
        public DcMotor fl;

        /**
         * Control 3: frontRight
         */
        public DcMotor fr;

        /**
         * Control 2: backLeft
         */
        public DcMotor bl;

        /**
         * Control 1: backRight
         */
        public DcMotor br;


        /**
         * Expansion 0: intake
         */
        public DcMotor intake;

        /**
         * Expansion 3: output
         */
        public Motor output;


        /**
         * Control 5: preventer
         */
        public ServoEx preventer;


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
     * Intake Actuator
     */
    public Actuator intake;

    /**
     * Output Actuator
     */
    public Actuator output;

    /**
     * Preventer Switch
     */
    public Switch preventer;

    public double kP = 1;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0.98;

    public double outputPower = 0.725;
    public Measure<Time> outputChargeTime = Seconds.of(3);

    public final Hardware hw = new Hardware();

    /**
     * SequentialTaskGroup that launches the collected pollen.
     */
    public SequentialTaskGroup launch;

    /**
     * ParallelTaskGroup that ejects all collected field elements out the intake.
     */
    public ParallelTaskGroup eject;

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("frontLeft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.fr = getHardware("frontRight", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.bl = getHardware("backLeft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.br = getHardware("backRight", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });

        hw.intake = getHardware("intake", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.REVERSE);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.output = getHardware("output", Motor.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            PIDFController pidf = new PIDFController(kP, kI, kD, kF);
            d.setRunUsingEncoderController(1, 2100, pidf); // theoretically has an insane max tps but it aint reaching allat

            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
                pidf.setCoefficients(kP, kI, kD, kF);

                o.telemetry.addData("currentVelocity", d.getVelocity());
                o.telemetry.addData("targetVelocity", pidf.getSetpoint());
            }));
        });

        hw.preventer = getHardware("preventer", ServoEx.class, (d) -> {
            d.scaleRange(0, 1);
            d.setEndToEndTime(Seconds.of(0.8));
        });

        hw.imu = getHardware("imu", IMUEx.class, (d) ->
                d.lazyInitialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                ))));

        DriveModel driveModel = new DriveModel.Builder()
                .setInPerTick(1)
                .setLateralInPerTick(1)
                .setTrackWidthTicks(1)
                .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
                .setMaxWheelVel(InchesPerSecond.of(1))
                .setKv(1)
                .setKs(1)
                .setKa(1)
                .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
                .setAxialGain(3.5)
                .setLateralGain(3.5)
                .setHeadingGain(2)
                .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.fl, hw.bl, hw.br, hw.fr, hw.imu, hardwareMap.voltageSensor)
                .withName("drive");

        intake = new Actuator(hw.intake)
                .withName("intakeSub");

        output = new Actuator(hw.output)
                .withName("outputSub");

        preventer = new Switch(hw.preventer)
                .withOpenBound(0)
                .withClosedBound(1)
                .withName("preventerSub");

        launch = new SequentialTaskGroup(
                preventer.tasks.close(),
                output.tasks.runFor(outputChargeTime, outputPower),
                output.tasks.control(() -> outputPower).until(preventer.tasks.open()),
                output.tasks.runFor(Seconds.of(1.5), outputPower).with(intake.tasks.runFor(Seconds.of(1.5), 1)),
                preventer.tasks.close()
        );

        eject = new ParallelTaskGroup(
                intake.tasks.runFor(Seconds.of(2), -1)
        );
    }
}
