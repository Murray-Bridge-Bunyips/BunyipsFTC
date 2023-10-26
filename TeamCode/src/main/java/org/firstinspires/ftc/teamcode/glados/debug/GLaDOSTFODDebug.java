package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.TfodData;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

import java.util.List;

/**
 * Vision debugger for TFOD detections, GLaDOS bot.
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLADOS: TFOD Debug", group = "GLADOS")
public class GLaDOSTFODDebug extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private Vision vision;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        vision = new Vision(this, config.webcam, new C920());
        // Start AprilTag vision processing
        vision.init(Vision.Processors.TFOD);
        vision.start(Vision.Processors.TFOD);
    }

    @Override
    protected void activeLoop() {
        vision.tick();
        List<TfodData> data = vision.getTfodData();
        for (int i = 0; i < data.size(); i++) {
            addTelemetry(String.valueOf(data.get(i)));
            addTelemetry(String.valueOf(data.get(i).getVerticalTranslation()));
            // TODO: Finish this
        }
    }
}
