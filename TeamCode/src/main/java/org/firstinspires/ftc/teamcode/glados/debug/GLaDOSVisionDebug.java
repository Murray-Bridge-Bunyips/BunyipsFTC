package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfig;

/**
 * Vision debugger for AprilTag detections, GLaDOS bot.
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLADOS: AprilTag Vision Debug", group = "GLADOS")
public class GLaDOSVisionDebug extends BunyipsOpMode {
    private GLaDOSConfig config = new GLaDOSConfig();
    private Vision vision;

    @Override
    protected void onInit() {
        config = (GLaDOSConfig) RobotConfig.newConfig(this, config, hardwareMap);
        vision = new Vision(this, config.webcam);
        // Start AprilTag vision processing
        vision.init(Vision.Processors.APRILTAG);
        vision.start(Vision.Processors.APRILTAG);
    }

    @Override
    protected void activeLoop() {
        vision.tick();
        addTelemetry(vision.getAprilTagData().toString());
    }
}
