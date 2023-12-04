package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Test extension motor tracking and control using direct input
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "Extrusion Motor Runner")
public class GLaDOSExtensionRunner extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInit() {
        config.init(this);
    }

    @Override
    protected void activeLoop() {
        if (config.suspenderActuator != null) {
            config.suspenderActuator.setPower(gamepad1.left_stick_y);
            addTelemetry("Extrusion Motor Position: %", config.suspenderActuator.getCurrentPosition());
            addTelemetry("Extrusion Motor Power: %", config.suspenderActuator.getPower());
        }
    }
}
