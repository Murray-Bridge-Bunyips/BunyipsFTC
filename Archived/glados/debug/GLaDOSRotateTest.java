package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

import java.util.Locale;

/**
 * Test arm rotation tracking and control
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "Rotator Motor Degrees Runner")
public class GLaDOSRotateTest extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    double target;

    @Override
    protected void onInit() {
        config.init(this);
        config.sr.reset();
        config.sr.track();
        config.sr.setTargetPosition(0);
        config.sr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        config.sr.setPower(1);
    }

    @Override
    protected void activeLoop() {
        target -= gamepad1.left_stick_y / 2;
        addTelemetry("Degrees: %", String.format(Locale.getDefault(), "%.2f", config.sr.getDegrees()));
        config.sr.setDegrees(target);
    }
}
