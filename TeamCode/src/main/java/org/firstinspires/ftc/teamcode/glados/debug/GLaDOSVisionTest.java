package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.common.vision.AprilTag;
import org.firstinspires.ftc.teamcode.common.vision.TFOD;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

/**
 * Test TFOD/AT detections.
 */
@TeleOp(name="GLaDOS: New Vision Test", group="GLaDOS")
public class GLaDOSVisionTest extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private Vision vision;
    private AprilTag at;
    private TFOD tf;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        vision = new Vision(this, config.webcam);
        at = new AprilTag(new C920());
        tf = new TFOD();
        vision.init(at, tf);
        vision.start(at, tf);
    }

    @Override
    protected void activeLoop() {
        vision.updateAll();
        addTelemetry(String.valueOf(vision.getAllData()));
    }
}
