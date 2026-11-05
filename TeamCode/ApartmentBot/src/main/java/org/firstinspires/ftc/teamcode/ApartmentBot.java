package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.InchesPerSecond;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

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
         * Expansion 1: output
         */
        public Motor output;


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

    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0;

    public final Hardware hw = new Hardware();

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("frontLeft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.fr = getHardware("frontRight", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.bl = getHardware("backLeft", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.br = getHardware("backRight", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });

        hw.intake = getHardware("intake", DcMotorEx.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });
        hw.output = getHardware("output", Motor.class, (d) -> {
            d.setDirection(DcMotor.Direction.FORWARD);
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            PIDFController pidf = new PIDFController(kP, kI, kD, kF);
            d.setRunToPositionController(pidf);

            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
                pidf.setCoefficients(kP, kI, kD, kF);

                Motor.debug(d, "output", Motor.Scope.POSITION, Motor.Scope.TARGET);
            }));
        });

        hw.imu = getHardware("imu", IMUEx.class, d ->
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
    }
}
