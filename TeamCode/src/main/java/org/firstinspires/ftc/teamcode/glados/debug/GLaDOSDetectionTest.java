package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Text;
import org.firstinspires.ftc.teamcode.common.Vision;
import org.firstinspires.ftc.teamcode.common.cameras.C920;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

@TeleOp(name = "GLaDOS: Detection Test", group = "GLADOS")
public class GLaDOSDetectionTest extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private Vision cam;
    private TeamProp proc;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        cam = new Vision(this, config.webcam, new C920());
        proc = new TeamProp();
//        cam.init(Vision.Processors.TEAM_PROP);
//        cam.start(Vision.Processors.TEAM_PROP);
        cam.experimentallyUseCustomProcessor(proc);

    }

    @Override
    protected void activeLoop() {
        addTelemetry(Text.format("Zone: %s", TeamProp.get_element_zone()));
    }
}
