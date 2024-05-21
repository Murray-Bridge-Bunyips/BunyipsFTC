package org.murraybridgebunyips.wheatley.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * TeleOp used to calibrate PID
 *
 * @author Lachlan Paul, 2024
 */
@Config
@TeleOp(name = "WheatleyPIDDebug")
public class WheatleyPIDDebug extends BunyipsOpMode {
    public static double kP;
    public static double kI;
    public static double kD;

    private HoldableActuator holdableActuator;
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInit() {
        config.init();
        holdableActuator = new HoldableActuator(config.clawRotator);
        PIDFCoefficients coefficients = config.linearActuator.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
        kP = coefficients.p;
        kI = coefficients.i;
        kD = coefficients.d;
    }

    @Override
    protected void activeLoop() {
        holdableActuator.setPower(gamepad1.left_stick_y);
        config.linearActuator.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(kP, kI, kD, 0, MotorControlAlgorithm.LegacyPID));
        holdableActuator.update();
    }
}
