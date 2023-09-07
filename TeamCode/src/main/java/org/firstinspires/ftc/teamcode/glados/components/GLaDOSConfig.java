package org.firstinspires.ftc.teamcode.glados.components;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * FTC 15215 CENTERSTAGE 2023-2024 robot configuration
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSConfig extends RobotConfig {

    public WebcamName webcam;

    @Override
    protected void init() {
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
    }
}
