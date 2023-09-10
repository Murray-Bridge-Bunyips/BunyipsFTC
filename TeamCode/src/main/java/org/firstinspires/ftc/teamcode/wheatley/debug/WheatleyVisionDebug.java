package org.firstinspires.ftc.teamcode.wheatley.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

/**
 * Debug for Wheatley's Vision
 *
 * @author Lachlan Paul, 2023
 */

@TeleOp(name = "WHEATLEY: Vision Debug", group = "WHEATLEY")
public class WheatleyVisionDebug extends BunyipsOpMode {

    private WheatleyConfig config = new WheatleyConfig();
    private Vision vision;

    @Override
    protected void onInit() {

        config = (WheatleyConfig) RobotConfig.newConfig(this, config, hardwareMap);

        vision = new Vision(this, config.webCam);
        vision.init(Vision.Processors.APRILTAG);
        vision.start(Vision.Processors.APRILTAG);
    }

    @Override
    protected void activeLoop() {

        // Gives a different message based on whether or not the camera is connected
        // This is to ensure there are no connection issues
        if (config.affirm(config.webCam)) {
            addTelemetry("Camera is connected");
        } else {
            addTelemetry("Camera is NOT connected");
        }
    }
}

