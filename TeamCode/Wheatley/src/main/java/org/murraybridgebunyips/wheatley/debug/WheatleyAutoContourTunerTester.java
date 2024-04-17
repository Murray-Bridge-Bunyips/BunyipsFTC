package org.murraybridgebunyips.wheatley.debug;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.murraybridgebunyips.bunyipslib.vision.AutoContourTuner;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.WhitePixel;
import org.opencv.core.Rect;

/**
 * A simple op mode to test the auto contour tuner.
 */
@TeleOp(name = "Auto Contour Tester")
public class WheatleyAutoContourTunerTester extends AutoContourTuner {
    @Override
    protected ColourThreshold setThresholdToTune() {
        return new WhitePixel();
    }

    @Override
    protected CameraName setCamera() {
        return hardwareMap.get(CameraName.class, "webcam");
    }

    @Nullable
    @Override
    protected Rect setCustomFittingRect() {
        return null;
    }
}
