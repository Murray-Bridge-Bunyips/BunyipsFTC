package org.firstinspires.ftc.teamcode.glados.components;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.RobotConfig;

/**
 * FTC 15215 CENTERSTAGE 2023-2024 robot configuration
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSConfigCore extends RobotConfig {

    public WebcamName webcam;
    // Expansion 0: Front Left "fl"
    public DcMotorEx fl;
    // Expansion 1: Front Right "fr"
    public DcMotorEx fr;
    // Expansion 2: Back Right "br"
    public DcMotorEx br;
    // Expansion 3: Back Left "bl"
    public DcMotorEx bl;

    @Override
    protected void init() {
        webcam = (WebcamName) getHardware("webcam", WebcamName.class);
        fl = (DcMotorEx) getHardware("fl", DcMotorEx.class);
        fr = (DcMotorEx) getHardware("fr", DcMotorEx.class);
        br = (DcMotorEx) getHardware("br", DcMotorEx.class);
        bl = (DcMotorEx) getHardware("bl", DcMotorEx.class);
    }
}
