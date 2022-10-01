package org.firstinspires.ftc.teamcode.common;


import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Lucas Bubner - FTC 15215 Captain
 * Oct 2022 - Murray Bridge Bunyips
 */

public class CameraOp extends BunyipsComponent {

    private final VuforiaLocalizer vuforia;
    private final TFObjectDetector tfod;
    private List<Recognition> updatedRecognitions;
    private final List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
    private OpenGLMatrix lastLocation = null;
    private final VuforiaTrackables targets;
    private boolean targetVisible = false;

    public String seeingTfod = null;

    public volatile boolean vuforiaEnabled = false;
    public volatile boolean tfodEnabled = false;

    // USING 2022-2023 POWERPLAY SEASON TFOD ASSETS
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    // private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/CustomTeamModel.tflite";

    public static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };

    // Vuforia key: BUNYIPSFTC belonging to lkbubner@proton.me
    private static final String VUFORIA_KEY =
            "AUAUEO7/////AAABmaBhSSJLMEMkmztY3FQ8jc8fX/wM6mSSQMqcLVW4LjbkWOU5wMH4tLQR7u90fyd93G/7JgfGU5nn2fHF41Q+oaUFe4zI58cr7KsONh689X8o8nr6+7BPN9gMrz08bOzj4+4JwxJ1m84iTPqCpImzYMHr60dtlKBSHN53sRL476JHa+HxZZB4kVq0BhpHlDo7WSGUb6wb5qdgGS3GGx62kiZVCfuWkGY0CZY+pdenCmkNXG2w0/gaeKC5gNw+8G4oGPmAKYiVtCkVJOvjKFncom2h82seL9QA9k96YKns4pQcJn5jdkCbbKNPULv3sqvuvWsjfFOpvzJ0Wh36MrcXlRCetR5oNWctERDjujSjf1o1";

    // Vuforia constants and conversions
    private static final float mmPerInch      = 25.4f;
    private static final float mmTargetHeight = 6 * mmPerInch;
    private static final float halfField      = 72 * mmPerInch;
    private static final float halfTile       = 12 * mmPerInch;
    private static final float oneAndHalfTile = 36 * mmPerInch;

    public CameraOp(BunyipsOpMode opmode, CameraName webcam, int tfodMonitorViewId) {
        super(opmode);
        // Vuforia localizer engine initialisation
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcam;
        parameters.useExtendedTracking = false;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // USING 2022-2023 POWERPLAY SEASON VUFORIA TRACKABLES
        targets = this.vuforia.loadTrackablesFromAsset("PowerPlay");
        allTrackables.addAll(targets);

        /*
         * Transformation matrices indicating where each target is on the field. These are required
         * for localisation.
         *
         * "Transformation matrix." Wikipedia, Wikimedia Foundation, 28 Sept. 2022,
         * en.wikipedia.org/wiki/Transformation_matrix. Accessed 1 Oct. 2022.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         */
        identifyTarget(0, "Red Audience Wall",   -halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0,  90);
        identifyTarget(1, "Red Rear Wall",        halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0, -90);
        identifyTarget(2, "Blue Audience Wall",  -halfField,   oneAndHalfTile, mmTargetHeight, 90, 0,  90);
        identifyTarget(3, "Blue Rear Wall",       halfField,   oneAndHalfTile, mmTargetHeight, 90, 0, -90);

        // Create a transformation matrix to let the computer know where the camera is relative to the robot
        final float CAMERA_FORWARD_DISPLACEMENT  = 0.0f * mmPerInch;   // Enter the forward distance from the center of the robot to the camera lens
        final float CAMERA_VERTICAL_DISPLACEMENT = 6.0f * mmPerInch;   // Enter vertical height from the ground to the camera
        final float CAMERA_LEFT_DISPLACEMENT     = 0.0f * mmPerInch;   // Enter the left distance from the center of the robot to the camera lens

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        // Let the wall target listeners know where the camera is on the robot for offset
        for (VuforiaTrackable trackable : allTrackables) {
            assert parameters.cameraName != null;
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }

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
    }

    @SuppressLint("DefaultLocale")
    public String determineTFOD() {
        // TFOD updated recognitions will return null if the data is the same as the last call
        if (updatedRecognitions == null || tfod == null) { return null; }

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

    public OpenGLMatrix targetRawMatrix() {
        if (targetVisible) {
            // Express position (translation) of robot in millimetres.
            VectorF translation = lastLocation.getTranslation();
            getOpMode().telemetry.addLine(String.format("Pos (mm)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0), translation.get(1), translation.get(2)));

            // Express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            getOpMode().telemetry.addLine(String.format("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle));

            /*
             * METHODS TO CALL WITH VUFORIA CAMERA OP
             *  OpenGLMatrix VuforiaMatrix = cam.targetRawMatrix();
             *  VectorF translation = VuforiaMatrix.getTranslation();
             *  Orientation rotation = Orientation.getOrientation(VuforiaMatrix, EXTRINSIC, XYZ, DEGREES);
             *
             * TRANSLATED DATA CALLS
             *  translation.get(0): Position X (mm)
             *  translation.get(1): Position Y (mm)
             *  translation.get(2): Position Z (mm)
             *
             *  rotation.firstAngle: Roll (X) (degs)
             *  rotation.secondAngle: Pitch (Y) (degs)
             *  rotation.thirdAngle: Heading (Z) (degs)
             */

            // Return the raw matrix for the OpMode data interpretation
            return lastLocation;
        }
        else {
            getOpMode().telemetry.addLine("Vuforia Visible Target: none");
        }
        return null;
    }

    /***
     * Identify a target by naming it, and setting its position and orientation on the field
     * @param targetIndex
     * @param targetName
     * @param dx, dy, dz  Target offsets in x,y,z axes
     * @param rx, ry, rz  Target rotations in x,y,z axes
     */
    private void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }

    public void startTFOD() {
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
            tfodEnabled = true;
        }
    }

    public void startVuforia() {
        if (vuforia != null) {
            targets.activate();
            vuforiaEnabled = true;
        }
    }

    public void stopVuforia() {
        targets.deactivate();
        vuforiaEnabled = false;
    }

    public void stopTFOD() {
        tfod.deactivate();
        tfodEnabled = false;
    }

    public void tick() {
        // Update the TensorFlow and Vuforia recognitions by the webcam if they're enabled
        if (tfodEnabled) {
            updatedRecognitions = tfod.getUpdatedRecognitions();
        }

        if (vuforiaEnabled) {
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                    targetVisible = true;

                    // getUpdatedRobotLocation() will return null if no new information is available since
                    // the last time that call was made, or if the trackable is not currently visible.
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    break;
                } else {
                    targetVisible = false;
                }
            }
        }
    }
}
