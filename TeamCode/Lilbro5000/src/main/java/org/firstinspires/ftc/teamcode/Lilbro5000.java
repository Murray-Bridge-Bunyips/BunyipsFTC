package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.Actuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Ziya, 2025
 */
public class Lilbro5000 extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public SimpleMecanumDrive drive;
    public Actuator intake;
    public Actuator outtake;
    public Actuator transfer;
    // .....................................................

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("front_left", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.bl = getHardware("back_left", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.br = getHardware("back_right", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.fr = getHardware("front_right", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });

        hw.intake = getHardware("intake", DcMotor.class);
        hw.outtake = getHardware("outtake", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });
        hw.transfer = getHardware("trigger", DcMotor.class);
        drive = new SimpleMecanumDrive(hw.fl, hw.bl, hw.br, hw.fr)
                .withName("Drive");
        intake = new Actuator(hw.intake).withName("Intake");
        outtake = new Actuator(hw.outtake).withName("Outtake");
        transfer = new Actuator(hw.transfer).withName("Transfer");
    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {
        /**
         * Expansion 0: back_left
         */
        public DcMotor bl;
        /**
         * Expansion 1: front_left
         */
        public DcMotor fl;
        /**
         * Expansion 2: front_right
         */
        public DcMotor fr;
        /**
         * Expansion 3: back_right
         */
        public DcMotor br;
        /**
         * Control 1: intake
         */
        public DcMotor intake;
        /**
         * Control 0: outtake
         */
        public DcMotor outtake;
        /**
         * Control 2: outtake
         */
        public DcMotor transfer;
    }
}

