package org.murraybridgebunyips.brainbot.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.brainbot.components.BrainbotConfig;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.WhitePixel;

@TeleOp(name = "Vision Test (Brainbot)")
public class BrainbotVision extends BunyipsOpMode {
    private final BrainbotConfig config = new BrainbotConfig();
    private Vision vision;

    @Override
    protected void onInit() {
        config.init(this);
        vision = new Vision(this, config.camera);
        WhitePixel whitePixel = new WhitePixel();
        vision.init(whitePixel);
        vision.start(whitePixel);
    }

    @Override
    protected void activeLoop() {
        addTelemetry(String.valueOf(vision.getAllData()));
    }
}
