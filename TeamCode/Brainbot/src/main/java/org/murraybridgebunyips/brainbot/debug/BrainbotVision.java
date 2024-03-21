package org.murraybridgebunyips.brainbot.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.brainbot.components.BrainbotConfig;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiColourThreshold;
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
        MultiColourThreshold proc = new MultiColourThreshold(Pixels.createProcessors());
        visionB.init(proc);
        visionB.start(proc);
        visionB.startPreview();
    }

    @Override
    protected void activeLoop() {
        addTelemetry(visionB.getAllData());
    }
}
