package org.firstinspires.ftc.teamcode;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Algae extends RobotConfig {
    public static class Hardware {
        /**
         * Control 0: configName
         */
        public DcMotor motor;


        /**
         * Expansion 0: configName
         */
        public DcMotor expMotor;


        /**
         * Expansion 0: configName
         */
        public Servo expServo;
    }

    /**
     * 4-Wheels MecanumDrive
     */
    public SimpleMecanumDrive drive;

    public final Hardware hw = new Hardware();

    @Override
    protected void onRuntime() {

    }
}
