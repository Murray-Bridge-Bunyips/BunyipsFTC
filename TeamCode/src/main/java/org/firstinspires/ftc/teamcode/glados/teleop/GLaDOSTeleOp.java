package org.firstinspires.ftc.teamcode.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfig;

/**
 * TeleOp for GLaDOS robot FTC 15215
 * Functionality is currently unknown...
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLADOS: TeleOp", group = "GLADOS")
public class GLaDOSTeleOp extends BunyipsOpMode {
    private GLaDOSConfig config = new GLaDOSConfig();

    @Override
    protected void onInit() {
        config = (GLaDOSConfig) RobotConfig.newConfig(this, config, hardwareMap);
    }

    @Override
    protected void activeLoop() {

    }
}
