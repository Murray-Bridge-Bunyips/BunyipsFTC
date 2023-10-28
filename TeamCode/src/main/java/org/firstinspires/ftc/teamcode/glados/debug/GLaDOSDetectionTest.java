package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.OpenCVCam;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Text;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.common.vision.TeamPropProcessor;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@TeleOp(name = "GLaDOS: Detection Test", group = "GLADOS")
public class GLaDOSDetectionTest extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private Vision cam;
    private TeamPropProcessor proc;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        cam = new Vision(this, config.webcam, new C920());
        proc = new TeamPropProcessor();
        cam.init(Vision.Processors.TEAM_PROP);
        cam.start(Vision.Processors.TEAM_PROP);

    }

    @Override
    protected void activeLoop() {
        addTelemetry(Text.format("Zone: %s", TeamPropProcessor.get_element_zone()));
    }
}
