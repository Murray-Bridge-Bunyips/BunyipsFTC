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
public class lilbro5000 extends RobotConfig {
    public final Hardware hw = new Hardware();

    // ... SUBSYSTEMS AND OTHER PUBLIC DECLARATIONS HERE ...
    public SimpleMecanumDrive drive;
    // TODO: Add more subsystems here according to your robot's needs
    // .....................................................

    @Override
    protected void onRuntime() {
        // TODO: Change or confirm the hardware name "fl" is your Front Left Mecanum Motor
        hw.fl = getHardware("fl", DcMotor.class, (d) -> {
            // TODO: Set the direction of the front left motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Change or confirm the hardware name "bl" is your Back Left Mecanum Motor
        hw.bl = getHardware("bl", DcMotor.class, (d) -> {
            // TODO: Set the direction of the back left motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Change or confirm the hardware name "br" is your Back Right Mecanum Motor
        hw.br = getHardware("br", DcMotor.class, (d) -> {
            // TODO: Set the direction of the back right motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        // TODO: Change or confirm the hardware name "fr" is your Front Right Mecanum Motor
        hw.fr = getHardware("fr", DcMotor.class, (d) -> {
            // TODO: Set the direction of the front right motor here
            d.setDirection(DcMotorSimple.Direction.FORWARD);
        });

        drive = new SimpleMecanumDrive(hw.fl, hw.bl, hw.br, hw.fr)
                .withName("Drive");
    }

    /**
     * Contains all hardware objects used for the robot.
     */
    public static class Hardware {
        // TODO: Rewrite the comments above each hardware declaration to match your port and name configuration (e.g <Control/Expansion> <port>: <name in DS>)
        //   It is strongly recommended to do this as hardware mappings may erase themselves unexpectedly.
        /**
         * Control 0: fl
         */
        public DcMotor fl;
        /**
         * Control 1: bl
         */
        public DcMotor bl;
        /**
         * Control 2: br
         */
        public DcMotor br;
        /**
         * Control 3: fr
         */
        public DcMotor fr;
        // TODO: Add more hardware devices here according to your robot's configuration
    }
}

