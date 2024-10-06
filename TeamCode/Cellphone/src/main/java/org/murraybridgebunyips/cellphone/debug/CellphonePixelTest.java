package org.murraybridgebunyips.cellphone.debug;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ColorSpace;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.vision.Vision;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourLocator;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.WhitePixel;
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
//        VisionPortal p = VisionPortal.easyCreateWithDefaults(
//                config.cameraB,
//                new ColorBlobLocatorProcessor.Builder()
//                        .setRoi(ImageRegion.entireFrame())
//                        .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
//                        .setTargetColorRange(new ColorRange(ColorSpace.YCrCb, WhitePixel.LOWER_YCRCB, WhitePixel.UPPER_YCRCB))
//                        .build()
//                new PredominantColorProcessor.Builder()
//                        .setRoi(ImageRegion.asUnityCenterCoordinates(-0.2, 0.2, 0.2, -0.2))
//                        .setSwatches(PredominantColorProcessor.Swatch.RED)
//                        .build()
//        );
        visionB = new Vision(config.cameraB);
//        MultiColourThreshold proc = new MultiColourThreshold(Pixels.createProcessors());
        ColourLocator proc = new ColourLocator(
                b -> b
                        .setRoi(ImageRegion.entireFrame())
                        .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                        .setTargetColorRange(new ColorRange(ColorSpace.YCrCb, WhitePixel.LOWER_YCRCB, WhitePixel.UPPER_YCRCB))
        );
//        ColourSensor proc = new ColourSensor(ImageRegion.asUnityCenterCoordinates(-0.2, 0.2, 0.2, -0.2), PredominantColorProcessor.Swatch.RED);
        visionB.init(proc);
        visionB.start(proc);
        visionB.startPreview();
//        Task testTask = new AlignToContourTask(Seconds.of(3), visionB, () -> proc.getData().stream().map(ColourBlob::toContourData).collect(Collectors.toList()), new PController(0));
    }

    @Override
    protected void activeLoop() {
        telemetry.add(visionB.getAllData());
    }
}
