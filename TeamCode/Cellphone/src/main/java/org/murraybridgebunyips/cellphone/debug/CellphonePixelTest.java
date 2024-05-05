package org.murraybridgebunyips.cellphone.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.MultiColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.Pixels;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;

/**
 * Test all 2023-2024 CENTERSTAGE pixel detections.
 */
@TeleOp(name = "Pixel Vision Test (Cellphone)")
public class CellphonePixelTest extends BunyipsOpMode {
    private final CellphoneConfig config = new CellphoneConfig();
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
