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
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom common class to operate Vuforia, OpenCV, and TensorFlow through a single attached Webcam
 * @author Lucas Bubner - FTC 15215 Captain; Oct 2022 - Murray Bridge Bunyips
 */

public class CameraOp extends BunyipsComponent {

    private final VuforiaLocalizer vuforia;
    private final TFObjectDetector tfod;
    private List<Recognition> updatedRecognitions;
    private final List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
    private OpenGLMatrix lastLocation = null;
    private final VuforiaTrackables targets;
    private final CameraName webcam;
    private final int tfodMonitorViewId;

    // Use seeingTfod public String for saving a TFOD value for usage in other tasks
    public String seeingTfod = null;

    // Best guess is used when time limits are reached, then is saved to seeingTfod
    public String bestguess = null;

    // Allow other classes to access the OpenCV configuration powered by Vuforia passthrough
    public final OpenCvCamera vuforiaPassthroughCam;

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
     * CameraOperation custom common class for USB-connected webcams (TFOD Objects + Vuforia Field Pos + OpenCV async features)
     * @param opmode Pass abstract opmode class for telemetry
     * @param webcam hardwareMap.get(WebcamName.class, "NAME_OF_CAMERA")
     * @param tfodMonitorViewId hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
     */
    public CameraOp(BunyipsOpMode opmode, CameraName webcam, int tfodMonitorViewId) {
        super(opmode);
        this.webcam = webcam;
        this.tfodMonitorViewId = tfodMonitorViewId;

        // OpenCV viewport configs
        int[] viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(tfodMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);

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
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);

        // Initialise OpenCV, and as this will be running on an async thread,
        // this should not impact performance of the OpMode
        vuforiaPassthroughCam = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters, viewportContainerIds[1]);
        vuforiaPassthroughCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                vuforiaPassthroughCam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                vuforiaPassthroughCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                vuforiaPassthroughCam.startStreaming(0,0, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                getOpMode().telemetry.addLine("Unable to initialise OpenCV functions. Error code: " + errorCode);
            }
        });
    }

    /**
     * Set the OpenCV pipeline of the camera to provide information based on arguments provided
     */
    public void setOpenCVPipeline(OpenCvPipeline pipeline) {
        vuforiaPassthroughCam.setPipeline(pipeline);
    }

    /**
     * TensorFlow detection return function to determine a label. TFOD must be activated.
     * @return Returns the String of the detected TFOD label if the confidence is above 75%, otherwise returns null
     */
    @SuppressLint("DefaultLocale")
    public String determineTFOD() {
        // TFOD updated recognitions will return null if the data is the same as the last call
        if (updatedRecognitions == null || tfod == null) { return null; }

        for (Recognition recognition : updatedRecognitions) {
            double col = (recognition.getLeft() + recognition.getRight()) / 2;
            double row = (recognition.getTop()  + recognition.getBottom()) / 2;
            double width  = Math.abs(recognition.getRight() - recognition.getLeft());
            double height = Math.abs(recognition.getTop()  - recognition.getBottom());

            // Debugging telemetry
            getOpMode().telemetry.addLine(String.format("Image: %1$s (%2$.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 ));
            getOpMode().telemetry.addLine(String.format("- Position (Row/Col): %1$.0f / %2$.0f", row, col));
            getOpMode().telemetry.addLine(String.format("- Size (Width/Height): %1$.0f / %2$.0f", width, height));

            // If the computer is more than 90% sure that the signal is what it thinks it is, then return it.
            // This will prevent an instant locking of the signal, and allow the engine a bit of time to think.
            // Combined with a task, this can be time constrained in the event this method keeps returning null
            bestguess = recognition.getLabel();
            if (recognition.getConfidence() > 0.90) {
                // Will not automatically save label to public seeingTfod variable and is left for the task/opmode to do
                // Uncomment the next line if auto saving to cam.seeingTfod is desired
                // this.seeingTfod = recognition.getLabel();
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
        if (targetVisible) {

            /*
             * This method shouldn't be called unless a raw matrix is required or for debugging.
             * Debugging telemetry from this method should be used from the calling OpMode + get methods (see above)
             * However, if needed for testing, call getTargetRawMatrix to a junk variable and
             * uncomment the lines below to get all interpreted data readings for debugging.
             */

//            VectorF translation = lastLocation.getTranslation();
//            getOpMode().telemetry.addLine(String.format("Pos (mm): {X, Y, Z} = %.1f, %.1f, %.1f",
//                    translation.get(0), translation.get(1), translation.get(2)));

//            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
//            getOpMode().telemetry.addLine(String.format("Rot (deg): {Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle));

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
     public double getX() {
        VectorF translation = this.getTargetTranslation();
        return translation.get(0);
     }

    /**
     * Get positional Y coordinate from Vuforia
     * @return mm of interpreted position Y data
     */
     public double getY() {
        VectorF translation = this.getTargetTranslation();
        return translation.get(1);
     }

    /**
     * Get positional Z coordiate from Vuforia
     * @return mm of interpreted position Z data
     */
     public double getZ() {
        VectorF translation = this.getTargetTranslation();
        return translation.get(2);
     }

    /**
     * Get X (roll) orientation from Vuforia
     * @return X orientation in degrees
     */
     public double getRoll() {
        Orientation orientation = this.getOrientationTranslation();
        return orientation.firstAngle;
     }

    /**
     * Get Y (pitch) orientation from Vuforia
     * @return Y orientation in degrees
     */
     public double getPitch() {
        Orientation orientation = this.getOrientationTranslation();
        return orientation.secondAngle;
     }

    /**
     * Get Z (heading) orientation from Vuforia
     * @return Z orientation in degrees
     */
     public double getHeading() {
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
     * Update and tick TFOD detections and/or Vuforia field positions. OpenCV is controlled automatically.
     * Ensure to call this method in an active loop
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
