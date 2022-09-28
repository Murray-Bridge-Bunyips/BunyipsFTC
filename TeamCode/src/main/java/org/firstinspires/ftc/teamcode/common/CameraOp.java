package org.firstinspires.ftc.teamcode.common;


import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/*
    Lucas Bubner
    2022 - Murray Bridge Bunyips FTC 15215
 */

public class CameraOp extends BunyipsComponent {

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    List<Recognition> updatedRecognitions;

    // TFOD MODEL FOR 2022-2023 POWERPLAY SEASON
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    // private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/CustomTeamModel.tflite";
    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };

    private static final String VUFORIA_KEY =
            "AUAUEO7/////AAABmaBhSSJLMEMkmztY3FQ8jc8fX/wM6mSSQMqcLVW4LjbkWOU5wMH4tLQR7u90fyd93G/7JgfGU5nn2fHF41Q+oaUFe4zI58cr7KsONh689X8o8nr6+7BPN9gMrz08bOzj4+4JwxJ1m84iTPqCpImzYMHr60dtlKBSHN53sRL476JHa+HxZZB4kVq0BhpHlDo7WSGUb6wb5qdgGS3GGx62kiZVCfuWkGY0CZY+pdenCmkNXG2w0/gaeKC5gNw+8G4oGPmAKYiVtCkVJOvjKFncom2h82seL9QA9k96YKns4pQcJn5jdkCbbKNPULv3sqvuvWsjfFOpvzJ0Wh36MrcXlRCetR5oNWctERDjujSjf1o1";

    public CameraOp(BunyipsOpMode opmode, CameraName webcam, int tfodMonitorViewId) {
        super(opmode);
        // Vuforia localizer engine initialisation
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcam;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // TensorFlow object detection initialisation
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);

        // Activate TFOD
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }
    }

    @SuppressLint("DefaultLocale")
    public String determineSignal() {
        // TFOD updated recognitions will return null if the data is the same as the last call
        if (updatedRecognitions == null) { return null; }

        // Debug telemetry
        getOpMode().telemetry.addLine(String.format("Objects found: %d", updatedRecognitions.size()));
        for (Recognition recognition : updatedRecognitions) {
            double col = (recognition.getLeft() + recognition.getRight()) / 2;
            double row = (recognition.getTop()  + recognition.getBottom()) / 2;
            double width  = Math.abs(recognition.getRight() - recognition.getLeft());
            double height = Math.abs(recognition.getTop()  - recognition.getBottom());
 
            getOpMode().telemetry.addLine(String.format("Image", "%1$s (%2$.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 ));
            getOpMode().telemetry.addLine(String.format("- Position (Row/Col)","%1$.0f / %2$.0f", row, col));
            getOpMode().telemetry.addLine(String.format("- Size (Width/Height)","%1$.0f / %2$.0f", width, height));

            // If the computer is more than 75% sure that the signal is what it thinks it is, then return it.
            // This will prevent an instant locking of the signal, and allow the engine a bit of time to think.
            // Combined with a task, this can be time constrained in the event this method keeps returning null
            if (recognition.getConfidence() > 0.75) {
                return recognition.getLabel();
            }
        }
        return null;
    }

//    public void determinePosition() {
//        // TODO: Use Vuforia Field Navigation class to inherit methods used for determining position on field
//    }

    public void tick() {
        // Update the TensorFlow recognitions by the webcam
        updatedRecognitions = tfod.getUpdatedRecognitions();

        // TODO: Vuforia field positioning update
    }
}
