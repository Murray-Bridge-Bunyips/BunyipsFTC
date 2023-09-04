package org.firstinspires.ftc.teamcode.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfig;

/**
 * TeleOp for GLaDOS robot FTC 15215
 * Functionality is currently unknown...
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLADOS: TeleOp", group = "GlADOS")
public class GLaDOSTeleOp extends BunyipsOpMode {
    private GLaDOSConfig config = new GLaDOSConfig();
    private Vision vision;

    @Override
    protected void onInit() {
        config = (GLaDOSConfig) RobotConfig.newConfig(this, config, hardwareMap);
        vision = new Vision(this, config.webcam);
        // Start AprilTag vision processing, likely will be an aspect of CENTERSTAGE
        vision.init(Vision.Processors.APRILTAG);
        vision.start(Vision.Processors.APRILTAG);
    }

    @Override
    protected void activeLoop() {
        vision.tick();
        addTelemetry(vision.getAprilTagData().toString());
    }
}
