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

    public int lineDistance = 200;

    public int fixDistance = 200;
    public int breakDistance = -200;

    public final Hardware hw = new Hardware();

    public double kP = 0.0;
    public double kI = 0.0;
    public double kD = 0.0;

    @Override
    protected void onRuntime() {
        hw.line = getHardware("tableString", Motor.class, (d) -> {
            //TODO: Make positive direction the direction that repairs the table
            //TODO: Tune PID
            //TODO: Tune how far the motor needs to rotate when breaking/fixing
            //TODO: Tune minimum and maximum (if needed)
            d.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            PIDController pid = new PIDController(kP, kI, kD);
            d.setRunToPositionController(pid);

            BunyipsOpMode.ifRunning(o -> o.onActiveLoop(() -> {
                pid.setCoefficients(kP, kI, kD, 0.0);
                Motor.debug(d, "line", Motor.Scope.POSITION, Motor.Scope.TARGET);
            }));
        });

        lineActuator = new HoldableActuator(hw.line)
//                .withUpperLimit()
//                .withLowerLimit()
                .withName("Fishing Line");
    }
}
