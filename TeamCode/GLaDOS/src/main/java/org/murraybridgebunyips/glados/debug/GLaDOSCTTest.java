package org.murraybridgebunyips.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.murraybridgebunyips.bunyipslib.vision.ContourTuner;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.PurplePixel;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.WhitePixel;

/**
 * ContourTuner testing
 */
@TeleOp
public class GLaDOSCTTest extends ContourTuner {
    @Override
    protected CameraName setCamera() {
        return (WebcamName) hardwareMap.get("webcam");
    }

    @Override
    protected ColourThreshold[] setThresholdsToTune() {
        return new ColourThreshold[]{
                new WhitePixel(),
                new PurplePixel()
        };
    }
}
