package org.firstinspires.ftc.team15215.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team15215.glados.components.GLaDOSConfigCore;
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode;

/**
 * Dynamic component tester for GLaDOS robot FTC 15215
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLaDOS: Component Tester", group = "GLaDOS")
public class GLaDOSComponentTester extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private boolean inc;
    private boolean dec;
    private int currentComponent;

    @Override
    protected void onInit() {
        config.init(this);
    }

    @Override
    protected void activeLoop() {
        switch (currentComponent) {
            case 0:
                config.leftPixel.setPosition(gamepad1.left_stick_y);
                break;
            case 1:
                config.rightPixel.setPosition(gamepad1.left_stick_y);
                break;
            case 2:
                config.pixelAlignment.setPosition(gamepad1.left_stick_y);
                break;
            case 3:
                config.pixelMotion.setPower(-gamepad1.left_stick_y);
                break;
            case 4:
                config.suspenderActuator.setPower(-gamepad1.left_stick_y);
                break;
            case 5:
                config.suspenderHook.setPosition(gamepad1.left_stick_y);
                break;
        }

        if (gamepad1.dpad_right && !inc) {
            currentComponent++;
            if (currentComponent > 5) currentComponent = 0;
        } else if (gamepad1.dpad_left && !dec) {
            currentComponent--;
            if (currentComponent < 0) currentComponent = 5;
        }

        inc = gamepad1.dpad_right;
        dec = gamepad1.dpad_left;
        addTelemetry("%", config.suspenderHook.getPosition());
        addTelemetry("Use gamepad1.left_stick_y to control component. Use dpad_left and dpad_right to switch components.");
        addTelemetry("Currently controlling: %", currentComponent == 0 ? "Left Pixel" : currentComponent == 1 ? "Right Pixel" : currentComponent == 2 ? "Pixel Alignment" : currentComponent == 3 ? "Pixel Motion" : currentComponent == 4 ? "Suspender Actuator" : "Suspender Hook");
    }
}
