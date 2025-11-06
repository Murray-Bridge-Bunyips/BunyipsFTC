package org.firstinspires.ftc.teamcode;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.IMUEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.localization.PinpointLocalizer;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.DriveModel;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MecanumGains;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.parameters.MotionProfile;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.BlinkinLights;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;

@Config
public class Jonas extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: bl <sub>yaoi motor</sub>
         */
        public DcMotor backLeft;

        /**
         * Control 1: br
         */
        public DcMotor backRight;

        /**
         * Control 2: fr
         */
        public DcMotor frontRight;

        /**
         * Control 3: fl
         */
        public DcMotor frontLeft;

        /**
         * Control 0: pinpoint
         */
        public GoBildaPinpointDriver pinpoint;


        /**
         * Expansion 0: output
         */
        public Motor output;

        /**
         * Expansion 1: intake
         */
        public DcMotor intake;


        /**
         * Expansion 5: preventer
         */
        public ServoEx preventer;


        /**
         * Expansion 0: lights
         */
        public RevBlinkinLedDriver lights;
    }

    /**
     * 4-Wheels MecanumDrive
     */
    public MecanumDrive drive;

    /**
     * Output Actuator
     */
    public Actuator output;

    /**
     * Intake Actuator
     */
    public Actuator intake;

    /**
     * Preventer Switch
     */
    public Switch preventer;

    /**
     * Lights BlinkinLights
     */
    public BlinkinLights lights;

    public final Hardware hw = new Hardware();

    @Override
    protected void onRuntime() {
        hw.frontLeft = getHardware("fl", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);
        });
        hw.backLeft = getHardware("bl", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);
        });

        hw.frontRight = getHardware("fr", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        hw.backRight = getHardware("br", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));

        hw.pinpoint = getHardware("pinpoint", GoBildaPinpointDriver.class);

        hw.output = getHardware("output", Motor.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            PIDFController pidf = new PIDFController(1, 0.0, 0.0, 3.5);
            d.setRunUsingEncoderController(1, 1900, pidf);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
//                pidf.setPIDF(1, 0.0, 0.0, 3.5);
//                o.telemetry.addData("currentVelocity", d.getVelocity());
//                o.telemetry.addData("targetVelocity", pidf.getSetpoint());
//            }));
        });
        hw.intake = getHardware("intake", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));

        hw.preventer = getHardware("preventer", ServoEx.class, (d) -> {
            d.setEndToEndTime(Seconds.of(0.4));
            d.scaleRange(0, 0.4);
        });

        hw.lights = getHardware("lights", RevBlinkinLedDriver.class);

        // roadrunner template
        DriveModel driveModel = new DriveModel.Builder()
//            .setInPerTick()
//            .setLateralInPerTick()
//            .setTrackWidthTicks()
        .build();
        MotionProfile motionProfile = new MotionProfile.Builder()
//            .setKv()
//            .setKs()
//            .setKa()
        .build();
        MecanumGains mecanumGains = new MecanumGains.Builder()
//            .setAxialGain()
//            .setLateralGain()
//            .setHeadingGain()
            .build();
        PinpointLocalizer.Params localiserParams = new PinpointLocalizer.Params.Builder()
            .setInitialParDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .setInitialPerpDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .build();

        drive = new MecanumDrive(driveModel, motionProfile, mecanumGains, hw.frontLeft, hw.backLeft, hw.backRight, hw.frontRight, IMUEx.none(), hardwareMap.voltageSensor)
            .withLocalizer(new PinpointLocalizer(driveModel, localiserParams, hw.pinpoint))
            .withName("Drive");

        output = new Actuator(hw.output)
            .withName("Output");

        intake = new Actuator(hw.intake)
            .withName("Intake");

        preventer = new Switch(hw.preventer)
            .withName("Preventer");

        lights = new BlinkinLights(hw.lights, RevBlinkinLedDriver.BlinkinPattern.SINELON_RAINBOW_PALETTE)
            .withName("Lights");
    }
}