package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;

/**
 * Main robot configuration file.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Your Name, Year // TODO: Set this to your name and the current year!
 */
public class Damon extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public SimpleMecanumDrive drive;
    // TODO: Add more subsystems here according to your robot's needs
    // .....................................................

    @Override
    protected void onRuntime() {
        hw.fl = getHardware("fl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });


        hw.bl = getHardware("bl", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });


        hw.br = getHardware("br", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.REVERSE);
        });


        hw.fr = getHardware("fr", DcMotor.class, (d) -> {
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        drive = new SimpleMecanumDrive(hw.fl, hw.bl, hw.br, hw.fr)
                .withName("Drive");
    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {

        /**
         * Expansion 0: fl
         */
        public DcMotor fl;
        /**
         * Expansion 1: fr
         */
        public DcMotor fr;

        /**
         * Expansion 2: br
         */
        public DcMotor br;

        /**
         * Expansion 3: bl
         */
        public DcMotor bl;



    }
}
