package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Text;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

@TeleOp(name = "GLaDOS: Pivot Motor Debug", group = "GLaDOS")
@Disabled
public class GLaDOSPivotRunner extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
    }

    @Override
    protected void activeLoop() {
        if (config.sr != null) {
            config.sr.setPower(gamepad1.left_stick_y);
            addTelemetry(Text.format("Pivot Motor Position: %s", config.sr.getCurrentPosition()));
            addTelemetry(Text.format("Pivot Motor Power: %s", config.sr.getPower()));
        }
    }
}
