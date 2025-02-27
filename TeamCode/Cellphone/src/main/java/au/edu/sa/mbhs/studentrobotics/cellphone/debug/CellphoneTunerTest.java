package au.edu.sa.mbhs.studentrobotics.cellphone.debug;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.RoadRunnerDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.roadrunner.tuning.RoadRunnerTuningOpMode;
import au.edu.sa.mbhs.studentrobotics.cellphone.Cellphone;

/**
 * Taste the Ocean
 * Now Taste The Drive
 */
@TeleOp
public class CellphoneTunerTest extends RoadRunnerTuningOpMode {
    @NonNull
    @Override
    protected RoadRunnerDrive getDrive() {
        return Cellphone.instance.notAMecanumDrive;
    }
//    @NonNull
//    @Override
//    protected CameraName setCamera() {
//        return ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.BACK);
//    }
//
//    @NonNull
//    @Override
//    protected ColourThreshold[] setThresholdsToTune() {
//        return new ColourThreshold[]{
//                new WhitePixel(),
//                new PurplePixel()
//        };
//    }
}
