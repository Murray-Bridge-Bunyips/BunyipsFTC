package org.murraybridgebunyips.cellphone.debug;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.murraybridgebunyips.bunyipslib.vision.ColourTuner;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.PurplePixel;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.WhitePixel;

/**
 * Taste the Ocean
 */
@TeleOp
public class CellphoneTunerTest extends ColourTuner {
    @NonNull
    @Override
    protected CameraName setCamera() {
        return ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.BACK);
    }

    @NonNull
    @Override
    protected ColourThreshold[] setThresholdsToTune() {
        return new ColourThreshold[] {
                new WhitePixel(),
                new PurplePixel()
        };
    }
}
