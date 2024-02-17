package org.murraybridgebunyips.brainbot.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.brainbot.components.BrainbotConfig;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.AprilTag;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiYCbCrThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.Pixels;

@TeleOp(name = "Vision Test (Brainbot)")
public class BrainbotVision extends BunyipsOpMode {
    private final BrainbotConfig config = new BrainbotConfig();
    private Vision visionB;
//    private Vision visionF;

    @Override
    protected void onInit() {
        config.init(this);
        visionB = new Vision(config.cameraB);
        MultiYCbCrThreshold all = new MultiYCbCrThreshold(Pixels.createProcessors());
        AprilTag at = new AprilTag();
        visionB.init(all, at);
        visionB.start(all, at);
        visionB.startPreview();
    }

    @Override
    protected void activeLoop() {
        addTelemetry(visionB.getAllData());
    }
}
