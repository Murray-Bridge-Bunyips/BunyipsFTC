package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDFController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.ServoEx;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Switch;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleTankDrive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Collections;

public class DinkyBot extends RobotConfig {
    public static class Hardware {
        /**
         * Control 1: leftDrive
         */
        public DcMotor leftDrive;

        /**
         * Control 2: rightDrive
         */
        public DcMotor rightDrive;

        /**
         * Control 3: flywheel
         */
        public DcMotor flywheel;


        /**
         * Control 5: pusher
         */
        public Servo pusher;
    }

    /**
     * 2-Wheels SimpleTankDrive
     */
    public SimpleTankDrive drive;

    /**
     * Flywheel Actuator
     */
    public Actuator flywheel;

    /**
     * Pusher Switch
     */
    public Switch pusher;

    public final Hardware hw = new Hardware();

    public double kP = 1;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0.87;

    @Override
    protected void onRuntime() {
        //TODO: fix directions on everything

        hw.leftDrive = getHardware("leftDrive", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);
        });
        hw.rightDrive = getHardware("rightDrive", DcMotorEx.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);
        });

        //TODO: Tune PID if we have time
        hw.flywheel = getHardware("flywheel", Motor.class, (d) -> {
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            d.setDirection(DcMotor.Direction.REVERSE);

            PIDFController pidf = new PIDFController(kP, kI, kD, kF);
            d.setRunUsingEncoderController(1, 1800 /* theoretrically 2380 but it only approaches 1800 */, pidf);
            d.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        });

        //TODO: Tune these
        hw.pusher = getHardware("pusher", ServoEx.class, (d) -> {
            d.setEndToEndTime(Seconds.of(0.8));
            d.scaleRange(0, 1);
        });

        drive = new SimpleTankDrive(Collections.singletonList(hw.leftDrive), Collections.singletonList(hw.rightDrive))
                .withName("Drive");

        flywheel = new Actuator(hw.flywheel)
                .withName("Flywheel");

        pusher = new Switch(hw.pusher)
                .withName("Pusher");
    }
}
