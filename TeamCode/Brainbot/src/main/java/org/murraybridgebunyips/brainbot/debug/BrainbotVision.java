package org.murraybridgebunyips.brainbot.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.murraybridgebunyips.brainbot.components.BrainbotConfig;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Dbg;
import org.murraybridgebunyips.bunyipslib.cameras.C920;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.bunyipslib.vision.processors.TeamProp;
import org.murraybridgebunyips.bunyipslib.vision.processors.WhitePixel;

@TeleOp(name = "Vision Test (Brainbot)")
public class BrainbotVision extends BunyipsOpMode {
    private final BrainbotConfig config = new BrainbotConfig();
    private Vision vision;

    @Override
    protected void onInit() {
        config.init(this);
        vision = new Vision(config.camera);
        WhitePixel wp = new WhitePixel();
        TeamProp tp = new TeamProp(255, 0, 0);
        vision.init(wp, tp);
        vision.start(wp, tp);
        vision.startDashboardSender();
    }

    @Override
    protected void onStart() {
        vision.setDashboardProcessor("whitepixel");
    }

    @Override
    protected void activeLoop() {
        addTelemetry(String.valueOf(vision.getAllData()));
    }
}
