package org.murraybridgebunyips.wheatley.debug;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.wheatley.components.WheatleyConfig;

/**
 * TeleOp used to calibrate PID
 *
 * @author Lachlan Paul, 2024
 */
@Config
@TeleOp(name = "WheatleyPIDDebug")
public class WheatleyPIDDebug extends CommandBasedBunyipsOpMode {
    public static double kP;
    public static double kI;
    public static double kD;

    private HoldableActuator clawRotator;
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInitialise() {
        config.init();
        clawRotator = new HoldableActuator(config.clawRotator);
        PIDFCoefficients coefficients = config.clawRotator.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
        kP = coefficients.p;
        kI = coefficients.i;
        kD = coefficients.d;

        addSubsystems(clawRotator);
    }

    @Override
    protected void assignCommands() {
        clawRotator.setDefaultTask(clawRotator.controlTask(() -> -gamepad2.rsy));
    }

    @Override
    protected void periodic(){
        // holdableActuator.setPower(gamepad1.left_stick_y);
        config.clawRotator.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(kP, kI, kD, 0, MotorControlAlgorithm.LegacyPID));
        clawRotator.update();
    }
}
