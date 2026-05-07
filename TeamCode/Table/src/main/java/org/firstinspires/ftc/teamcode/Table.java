package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.control.pid.PIDController;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;

public class Table extends RobotConfig {
    public static class Hardware {
        /**
         * Control 3: tableString
         */
        public Motor line;
    }

    /**
     * Line Actuator
     */
    public HoldableActuator lineActuator;

    public int lineDistance = 1700;

    public final Hardware hw = new Hardware();

    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0;

    @Override
    protected void onRuntime() {
        hw.line = getHardware("tableString", Motor.class, (d) -> {
                    d.setDirection(DcMotor.Direction.REVERSE);
                    d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                    PIDController pid = new PIDController(kP, kI, kD);
                    d.setRunToPositionController(pid);

                    BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> pid.setCoefficients(kP, kI, kD, 0.0)));
        });

        lineActuator = new HoldableActuator(hw.line)
//                .withUpperLimit()
//                .withLowerLimit()
                .withName("Fishing Line");
    }
}
