package org.firstinspires.ftc.teamcode.common;


import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom common class to operate all Vuforia, TensorFlow, and OpenCV operations through a single attached Webcam
 * Allows hot-swapping between two modes, STANDARD (VF + TFOD) and OPENCV, for respective operation.
 * @author Lucas Bubner - FTC 15215 Captain; Oct-Nov 2022 - Murray Bridge Bunyips
 */

public class CameraOp extends BunyipsComponent {

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private List<Recognition> updatedRecognitions;
    private final List<VuforiaTrackable> allTrackables = new ArrayList<>();
    private OpenGLMatrix lastLocation = null;
    private VuforiaTrackables targets;
    private final WebcamName webcam;
    private final int monitorID;
    protected OpenCvCamera OCVcam;

    // Running variable which stores the last seen TFOD element (used for cross-task application)
    public volatile String seeingTfod = null;

    // Enum to indicate whether the camera should run in OpenCV mode or TFOD + Vuforia mode
    private CamMode mode;
    public enum CamMode {
        OPENCV,
        STANDARD
    }

    /**
     * Get the current mode that the camera is currently initialised in
     * @return CamMode enum value currently selected by this instance
     */
    public CamMode getMode() {
        return mode;
    }

    public boolean targetVisible = false;
    public boolean vuforiaEnabled = false;
    public boolean tfodEnabled = false;

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

    /**
     * CameraOperation custom common class for USB-connected webcams (TFOD Objects + Vuforia Field Pos or OpenCV mode)
     * @param opmode Pass abstract opmode class for telemetry
     * @param webcam hardwareMap.get(WebcamName.class, "NAME_OF_CAMERA")
     * @param monitorID hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
     */
    public CameraOp(BunyipsOpMode opmode, WebcamName webcam, int monitorID, CamMode mode) {
        super(opmode);
        this.webcam = webcam;
        this.monitorID = monitorID;
        this.mode = mode;

        // Make sure we're actually initialising a camera
        assert webcam != null;

        // Initialise the camera based on the mode selected
        switch (mode) {
            case STANDARD:
                stdinit();
                break;
            case OPENCV:
                OpenCVinit();
        }
    }

    private void stdinit() {
        // Vuforia localizer engine initialisation, Camera Stream will be Vuforia's
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(monitorID);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcam;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Init Vuforia targets using 2022-2023 POWERPLAY Season Trackables
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
        final float CAMERA_FORWARD_DISPLACEMENT  = 0.0f;   // Enter the forward distance from the center of the robot to the camera lens (mm)
        final float CAMERA_VERTICAL_DISPLACEMENT = 0.0f;   // Enter vertical height from the ground to the camera (mm)
        final float CAMERA_LEFT_DISPLACEMENT     = 0.0f;   // Enter the left distance from the center of the robot to the camera lens (mm)

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        // Let the wall target listeners know where the camera is on the robot for offset
        for (VuforiaTrackable trackable : allTrackables) {
            assert parameters.cameraName != null;
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }

        // TensorFlow object detection initialisation
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters();
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }

    private void OpenCVinit() {

        /*
         * Instead of using Vuforia and OpenCV on the same camera, we instead init the camera
         * using OpenCV's own camera instance. It is highly unlikely one camera would need to use
         * both Vuforia/TF and OpenCV at the same time, as this would be very taxing on components.
         * Besides, a hardware camera should not have multiple instances of a camera object running
         * on it at the same time, so this provides a way to pick between the two.
         */

        OCVcam = OpenCvCameraFactory.getInstance().createWebcam(webcam, monitorID);
        OCVcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                OCVcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                getOpMode().telemetry.addLine("An error occurred in initalising OpenCV. Standard mode will be activated. Error code: " + errorCode);
                OCVcam = null;
                mode = CamMode.STANDARD;
                stdinit();
            }
        });
    }

    /**
     * Call this function to swap the modes of the camera without having to re-instantiate the class
     */
    public void swapModes() {
        switch (mode) {
            case STANDARD:
                stopVuforia();
                stopTFOD();
                tfod = null;
                vuforia = null;
                OpenCVinit();
                break;
            case OPENCV:
                OCVcam.stopStreaming();
                OCVcam.closeCameraDeviceAsync(() -> {});
                OCVcam = null;
                stdinit();
        }
    }

    /**
     * Change the OpenCV pipeline on the fly by calling this method
     * @param pipeline Supply an OpenCVPipeline to set the camera to
     */
    public void setPipeline(OpenCvPipeline pipeline) {
        if (OCVcam != null)
            OCVcam.setPipeline(pipeline);
    }

    /**
     * TensorFlow detection return function to determine a label. TFOD must be activated.
     * @return Returns the String of the detected TFOD label if the confidence is above 75%, otherwise returns null
     */
    @SuppressLint("DefaultLocale")
    public String determineTFOD() {
        // TFOD updated recognitions will return null if the data is the same as the last call
        if (updatedRecognitions == null || tfod == null) return null;

        for (Recognition recognition : updatedRecognitions) {

            // Debugging telemetry, uncomment if required
            double col = (recognition.getLeft() + recognition.getRight()) / 2;
            double row = (recognition.getTop()  + recognition.getBottom()) / 2;
            double width  = Math.abs(recognition.getRight() - recognition.getLeft());
            double height = Math.abs(recognition.getTop()  - recognition.getBottom());
            getOpMode().telemetry.addLine(String.format("Image: %1$s (%2$.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 ));
            getOpMode().telemetry.addLine(String.format("- Position (Row/Col): %1$.0f / %2$.0f", row, col));
            getOpMode().telemetry.addLine(String.format("- Size (Width/Height): %1$.0f / %2$.0f", width, height));

            // If the computer is more than 85% sure that the signal is what it thinks it is, then return it.
            // This will prevent an instant locking of the signal, and allow the engine a bit of time to think.
            // Combined with a task, this can be time constrained in the event this method keeps returning null
            if (recognition.getConfidence() > 0.85) {
                // Save the string of this result as well to the running variable camera instance
                this.seeingTfod = recognition.getLabel();

                // Return the result to the opmode to let it know we're done
                return recognition.getLabel();
            }
        }
        return null;
    }


    /**
     * Returns the raw OpenGLMatrix info from the Vuforia engine for OpMode interpretation. See this method's definition for more information. Vuforia must be activated.
     * @return OpenGLMatrix from the current identified target identified by the Vuforia engine.
     *          Returns null if no target is currently visible.
     * For the most part, this method shouldn't have to be called in an OpMode unless for debugging.
     */
    /*
     * Call these methods for fully pre-interpreted data
     *     cam.getX(); Position X (mm)
     *     cam.getY(); Position Y (mm)
     *     cam.getZ(); Position Z (mm)
     *     cam.getPitch(); Pitch (X) (degs)
     *     cam.getRoll(); Roll (Y) (degs)
     *     cam.getHeading(); Heading (Z) (degs)
     *
     * See: https://github.com/FIRST-Tech-Challenge/FtcRobotController/blob/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/FTC_FieldCoordinateSystemDefinition.pdf
     * for information regarding field positioning with these coordinates.
     */
    @SuppressLint("DefaultLocale")
    public OpenGLMatrix getTargetRawMatrix() {
        if (targetVisible && vuforia != null) {

            /*
             * This method shouldn't be called unless a raw matrix is required or for debugging.
             * Debugging telemetry from this method should be used from the calling OpMode + get methods (see above)
             * However, if needed for testing, call getTargetRawMatrix to a junk variable and
             * uncomment the lines below to get all interpreted data readings for debugging.
             */

            VectorF translation = lastLocation.getTranslation();
            getOpMode().telemetry.addLine(String.format("Pos (mm): {X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0), translation.get(1), translation.get(2)));

            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            getOpMode().telemetry.addLine(String.format("Rot (deg): {Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle));

            // Return the raw matrix detected if it is visible to the camera, otherwise return null
            return lastLocation;
        }
        return null;
    }

    /**
     * Offers raw position matrices for custom OpMode interpretation of data, if something needs to be done
     * outside of standard pre-interpreted data. Vuforia must be enabled.
     * For the most part, you will not need to call this method and instead use the getX,Y,Z methods
     * @return translated position vector from Vuforia, returns null if there are no datapoints
     */
    public VectorF getTargetTranslation() {
        OpenGLMatrix matrix = this.getTargetRawMatrix();
        return matrix.getTranslation();
    }

    /**
     * Offers raw orientation matrix for custom OpMode interpretation of Vuforia information. Vuforia must be enabled.
     * For the most part, you will not need to call this method and instead use the getRoll,Pitch,Heading methods
     * @return translated orientation matrix from Vuforia, returns null if there are no datapoints
     */
    public Orientation getOrientationTranslation() {
        OpenGLMatrix matrix = this.getTargetRawMatrix();
        return Orientation.getOrientation(matrix, EXTRINSIC, XYZ, DEGREES);
    }

    /**
     * Get positional X coordinate from Vuforia
     * @return mm of interpreted position X data
     */
    public double vuGetX() {
        VectorF translation = this.getTargetTranslation();
        return translation.get(0);
    }

    /**
     * Get positional Y coordinate from Vuforia
     * @return mm of interpreted position Y data
     */
    public double vuGetY() {
        VectorF translation = this.getTargetTranslation();
        return translation.get(1);
    }

    /**
     * Get positional Z coordiate from Vuforia
     * @return mm of interpreted position Z data
     */
    public double vuGetZ() {
        VectorF translation = this.getTargetTranslation();
        return translation.get(2);
    }

    /**
     * Get X (roll) orientation from Vuforia
     * @return X orientation in degrees
     */
    public double vuGetRoll() {
        Orientation orientation = this.getOrientationTranslation();
        return orientation.firstAngle;
    }

    /**
     * Get Y (pitch) orientation from Vuforia
     * @return Y orientation in degrees
     */
    public double vuGetPitch() {
        Orientation orientation = this.getOrientationTranslation();
        return orientation.secondAngle;
    }

    /**
     * Get Z (heading) orientation from Vuforia
     * @return Z orientation in degrees
     */
    public double vuGetHeading() {
        Orientation orientation = this.getOrientationTranslation();
        return orientation.thirdAngle;
    }

    /**
     * Identify a target by naming it, and setting its position and orientation on the field
     * @param dx, dy, dz  Target offsets in x,y,z axes
     * @param rx, ry, rz  Target rotations in x,y,z axes
     */
    private void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }

    /**
     * Start TensorFlow Object Detection and allow detections to be made
     */
    public void startTFOD() {
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
            tfodEnabled = true;
        }
    }

    /**
     * Start Vuforia engine and allow field positioning data to be made
     */
    public void startVuforia() {
        if (vuforia != null) {
            targets.activate();
            vuforiaEnabled = true;
        }
    }

    /**
     * Stop Vuforia engine and camera field positioning data
     */
    public void stopVuforia() {
        targets.deactivate();
        vuforiaEnabled = false;
    }

    /**
     * Stop TensorFlow Object Detection
     */
    public void stopTFOD() {
        tfod.deactivate();
        tfodEnabled = false;
    }

    /**
     * Call this method in STANDARD mode to update TFOD and Vuforia recognitions.
     * This is not needed in OPENCV mode.
     */
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