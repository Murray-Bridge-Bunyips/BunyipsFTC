package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Text;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

@TeleOp(name = "GLaDOS: Extrusion Motor Runner", group = "GLaDOS")
public class GLaDOSExtensionRunner extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
    }

    @Override
    protected void activeLoop() {
        if (config.sa != null) {
            config.sa.setPower(gamepad1.left_stick_y);
            addTelemetry(Text.format("Extrusion Motor Position: %s", config.sa.getCurrentPosition()));
            addTelemetry(Text.format("Extrusion Motor Power: %s", config.sa.getPower()));
        }
    }
}
