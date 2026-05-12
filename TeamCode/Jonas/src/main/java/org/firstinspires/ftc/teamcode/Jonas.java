package org.firstinspires.ftc.teamcode;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.MecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;

@Config
public class Jonas extends RobotConfig {
    public static class Hardware {
        //TODO: TOOD: redo javadocs to be correct with new configuration
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

        //TODO: Update config to reflect the below as the javadoc is actually correct
        /**
         * Expansion 0: preventer
         */
        public ServoEx preventer;

        //TODO: Remove lights from config
    }

    //TODO: Reorder these to reflect new config order
    /**
     * 4-Wheels MecanumDrive
     */
    public SimpleMecanumDrive drive;

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

    public final Hardware hw = new Hardware();

    public SequentialTaskGroup launch;

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
            d.setDirection(DcMotor.Direction.REVERSE);

            //TODO: Tune pid
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

        drive = new SimpleMecanumDrive(hw.frontLeft, hw.backLeft, hw.backRight, hw.frontRight)
            .withName("Drive");

        output = new Actuator(hw.output)
            .withName("Output");

        intake = new Actuator(hw.intake)
            .withName("Intake");

        preventer = new Switch(hw.preventer)
            .withName("Preventer");

        launch = new SequentialTaskGroup(
                new ParallelTaskGroup(
                        //TODO: Tune these numbers
                        output.tasks.runFor(Seconds.of(3.4), 0.375),
                        intake.tasks.runFor(Seconds.of(1), 1).after(preventer.tasks.open().after(2.4, Seconds))
                ),
                preventer.tasks.close()
        );
    }
}