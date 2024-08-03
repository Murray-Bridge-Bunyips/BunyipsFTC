package org.murraybridgebunyips.common.centerstage;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.murraybridgebunyips.bunyipslib.vision.ColourTuner;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.common.centerstage.vision.BlueTeamProp;
import org.murraybridgebunyips.common.centerstage.vision.RedTeamProp;

/**
 * Colour tuner for red+blue team props
 */
@TeleOp(name = "[Debug] Tune for R+B Team Props", group = "z")
public class TuneProps extends ColourTuner {
    /**
     * The camera from HardwareMap to use for vision processing.
     *
     * @return the camera to use
     */
    @NonNull
    @Override
    protected CameraName setCamera() {
        return hardwareMap.get(CameraName.class,"webcam");
    }

    /**
     * The processors to tune in this OpMode.
     *
     * @return the processors to tune, will be able to switch between them during runtime
     */
    @NonNull
    @Override
    protected ColourThreshold[] setThresholdsToTune() {
        return new ColourThreshold[] {
                new RedTeamProp(),
                new BlueTeamProp()
        };
    }
}
