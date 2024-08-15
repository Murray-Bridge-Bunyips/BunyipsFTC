package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Motor;
import org.murraybridgebunyips.bunyipslib.external.PIDFFController;
import org.murraybridgebunyips.bunyipslib.external.ff.ElevatorFeedforward;
import org.murraybridgebunyips.bunyipslib.external.pid.PIDController;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Experimental class to test new Motor class and position.
 */
//@TeleOp
//@Config
@SuppressWarnings("MissingJavadoc")
public class GLaDOSArmControllerTuning extends BunyipsOpMode {
    public static double kP;
    public static double kI;
    public static double kD;
    public static double kS;
    public static double kG;
    public static double kV;
    public static double kA;

    public static int SET_POINT_TICKS;

    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private PIDFFController controller;
    private Motor motor;

    @Override
    protected void onInit() {
        config.init();
        motor = new Motor(config.arm);
        controller = new PIDFFController(new PIDController(kP, kI, kD), new ElevatorFeedforward(kS, kG, kV, kA), motor.getEncoder());
        motor.setRunToPositionController(controller);
        motor.resetEncoder();
    }

    @Override
    protected void activeLoop() {
        controller.setCoefficients(kP, kI, kD, kS, kG, kV, kA);
        motor.setTargetPosition(SET_POINT_TICKS);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(1);
        telemetry.addDashboard("setpoint", SET_POINT_TICKS);
        telemetry.addDashboard("current", motor.getCurrentPosition());
    }
}
