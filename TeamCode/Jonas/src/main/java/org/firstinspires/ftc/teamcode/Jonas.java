package org.firstinspires.ftc.teamcode;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.SequentialTaskGroup;

@Config
public class Jonas extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: fl
         */
        public DcMotor frontLeft;

        /**
         * Control 1: br
         */
        public DcMotor backRight;

        /**
         * Control 2: bl <sub>yaoi motor</sub>
         */
        public DcMotor backLeft;

        /**
         * Control 3: fr
         */
        public DcMotor frontRight;


        /**
         * Expansion 0: intake
         */
        public DcMotor intake;

        /**
         * Expansion 1: output
         */
        public Motor output;


        /**
         * Expansion 0: preventer
         */
        public ServoEx preventer;
    }

    /**
     * 4-Wheels SimpleMecanumDrive
     */
    public SimpleMecanumDrive drive;

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

    public final Hardware hw = new Hardware();

    public SequentialTaskGroup launch;
    public SequentialTaskGroup launchStaggered;

    public double kP = 1;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0.87;

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

        hw.intake = getHardware("intake", DcMotorEx.class, (d) -> d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE));
        hw.output = getHardware("output", Motor.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);

            PIDFController pidf = new PIDFController(kP, kI, kD, kF);
            d.setRunUsingEncoderController(1, 1800 /* theoretrically 2380 but it only approaches 1800 */, pidf);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        });

        hw.preventer = getHardware("preventer", ServoEx.class, (d) -> {
            d.setEndToEndTime(Seconds.of(0.8));
            d.scaleRange(0, 1);
        });

        drive = new SimpleMecanumDrive(hw.frontLeft, hw.backLeft, hw.backRight, hw.frontRight)
            .withName("Drive");

        intake = new Actuator(hw.intake)
                .withName("Intake");

        output = new Actuator(hw.output)
            .withName("Output");
// im so back
        preventer = new Switch(hw.preventer)
            .withName("Preventer");

        launch = new SequentialTaskGroup(
                new SequentialTaskGroup(
                    output.tasks.control(() -> 1).until(preventer.tasks.open()),
                    new ParallelTaskGroup(
                            output.tasks.runFor(Seconds.of(2), 1),
                            intake.tasks.runFor(Seconds.of(2), 1)
                    )
                ).after(
                        new ParallelTaskGroup(
                                output.tasks.runFor(Seconds.of(1.4), 1),
                                intake.tasks.runFor(Seconds.of(1.4), 1)
                        )
                ),
                preventer.tasks.close()
        );

        launchStaggered = new SequentialTaskGroup(
                new SequentialTaskGroup(
                        output.tasks.control(() -> 1).until(preventer.tasks.open()),
                        new ParallelTaskGroup(
                                output.tasks.runFor(Seconds.of(2), 1),
                                new SequentialTaskGroup(
                                        intake.tasks.runFor(Seconds.of(0.5), 1),
                                        intake.tasks.runFor(Seconds.of(0.5), 0),
                                        intake.tasks.runFor(Seconds.of(1), 1)
                                )
                        )
                ).after(
                        new ParallelTaskGroup(
                                output.tasks.runFor(Seconds.of(1.4), 1),
                                intake.tasks.runFor(Seconds.of(1.4), 1)
                        )
                ),
                preventer.tasks.close()
        );
    }
}