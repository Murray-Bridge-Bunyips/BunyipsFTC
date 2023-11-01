package org.firstinspires.ftc.teamcode.common;


import android.util.Size;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.common.cameras.CameraType;
import org.firstinspires.ftc.teamcode.common.vision.TeamProp;
import org.firstinspires.ftc.teamcode.common.vision.data.AprilTagData;
import org.firstinspires.ftc.teamcode.common.vision.data.TfodData;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Component wrapper to support the v8.2+ SDK's included libraries for Camera operation.
 * Allows TFOD and AprilTag processors to be used in OpModes. Optionally is expansible to add
 * custom processors through the enum system.
 *
 * @author Lucas Bubner, 2023
 */
// TODO: Implement new Vision components instead of hardcoded processing methods
public class Vision extends BunyipsComponent {
    // Arrays to store the data from the processors
    private final List<AprilTagData> aprilTagData = new ArrayList<>();
    private final List<TfodData> tfodData = new ArrayList<>();
    private final WebcamName webcam;
    private final CameraType cameraInfo;
    private TfodProcessor tfod;
    private AprilTagProcessor aprilTag;
    private TeamProp teamProp;
    private VisionPortal visionPortal;

    public Vision(@NonNull BunyipsOpMode opMode, WebcamName webcam, CameraType cameraType) {
        super(opMode);
        this.webcam = webcam;
        cameraInfo = cameraType;
    }

    /**
     * Builds the VisionPortal after the VisionPortal has been constructed.
     *
     * @param builder Processor-rich builder pattern for the VisionPortal
     * @return VisionPortalImpl
     */
    private VisionPortal constructVisionPortal(VisionPortal.Builder builder) {
        return builder
                .setCamera(webcam)
                .setCameraResolution(new Size(1280, 720))
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                // Set any additional VisionPortal settings here
                .build();
    }

    /**
     * Initialises the Vision class with the specified processors.
     * This method should only be called once per OpMode.
     * Processors will be STOPPED by default, you must call start() after initialising.
     *
     * @param processors TFOD and/or AprilTag
     */
    public void init(Processors... processors) {
        if (processors.length == 0)
            throw new IllegalArgumentException("Must initialise at least one integrated processor!");
        List<VisionProcessor> initialisedProcessors = new ArrayList<>();

        for (Processors processor : processors) {
            switch (processor) {
                case TFOD:
                    tfod = new TfodProcessor.Builder()
                            // Specify custom TFOD settings here
                            // By default this will load the current season assets
                            .build();
                    initialisedProcessors.add(tfod);
                    break;
                case APRILTAG:
                    aprilTag = new AprilTagProcessor.Builder()
                            .setLensIntrinsics(cameraInfo.getFx(), cameraInfo.getFy(), cameraInfo.getCx(), cameraInfo.getCy())
                            // Specify custom AprilTag settings here
                            .build();
                    initialisedProcessors.add(aprilTag);
                    break;
                case TEAM_PROP:
                    teamProp = new TeamProp();
                    initialisedProcessors.add(teamProp);
                    break;
            }
        }

        // Initialise the VisionPortal with our newly created processors
        VisionPortal.Builder builder = new VisionPortal.Builder();
        for (VisionProcessor processor : initialisedProcessors) {
            builder.addProcessor(processor);
        }

        visionPortal = constructVisionPortal(builder);

        // Disable the vision processors by default. The OpMode must call start() to enable them.
        for (VisionProcessor processor : initialisedProcessors) {
            visionPortal.setProcessorEnabled(processor, false);
        }

        // Disable live view by default
        visionPortal.stopLiveView();
    }

    /**
     * Start desired processors. This method must be called before trying to extract data from
     * the cameras, and must be already initialised with the init() method.
     *
     * @param processors TFOD and/or AprilTag
     */
    public void start(Processors... processors) {
        // Resume the stream if it was previously stopped or is not running
        if (visionPortal.getCameraState() == VisionPortal.CameraState.CAMERA_DEVICE_READY ||
                visionPortal.getCameraState() == VisionPortal.CameraState.STOPPING_STREAM) {
            // Note if the camera state is STOPPING_STREAM, it will block the thread until the
            // stream is resumed. This is a documented operation in the SDK.
            visionPortal.resumeStreaming();
        }
        for (Processors processor : processors) {
            switch (processor) {
                case TFOD:
                    if (tfod == null) {
                        throw new IllegalStateException("TFOD processor is not initialised!");
                    }
                    visionPortal.setProcessorEnabled(tfod, true);
                    break;
                case APRILTAG:
                    if (aprilTag == null) {
                        throw new IllegalStateException("AprilTag processor is not initialised!");
                    }
                    visionPortal.setProcessorEnabled(aprilTag, true);
                    break;
                case TEAM_PROP:
                    if (teamProp == null) {
                        throw new IllegalStateException("TeamProp processor is not initialised!");
                    }
                    visionPortal.setProcessorEnabled(teamProp, true);
                    break;
            }
        }
    }

    /**
     * Stop desired processors (Level 2).
     * <p>
     * This method should be called when hardware resources no longer
     * need to be allocated to operating the cameras, and should have the option to be re-enabled
     * with start().
     * <p>
     * Note: The VisionPortal is automatically closed at the end of the OpMode's run time, calling
     * stop() or terminate() is not required at the end of an OpMode.
     * <p>
     * Passing no arguments will pause the Camera Stream (Level 3). Pausing
     * the camera stream will automatically disable any running processors. Note this may
     * take some very small time to resume the stream if start() is called again. If you don't plan
     * on using the camera stream again, it is recommended to call terminate() instead.
     *
     * @param processors TFOD and/or AprilTag
     */
    public void stop(Processors... processors) {
        // Disable processors without pausing the stream
        for (Processors processor : processors) {
            switch (processor) {
                case TFOD:
                    if (tfod == null) {
                        throw new IllegalStateException("TFOD processor is not initialised!");
                    }
                    visionPortal.setProcessorEnabled(tfod, false);
                    break;
                case APRILTAG:
                    if (aprilTag == null) {
                        throw new IllegalStateException("AprilTag processor is not initialised!");
                    }
                    visionPortal.setProcessorEnabled(aprilTag, false);
                    break;
                case TEAM_PROP:
                    if (teamProp == null) {
                        throw new IllegalStateException("TeamProp processor is not initialised!");
                    }
                    visionPortal.setProcessorEnabled(teamProp, false);
                    break;
            }
        }
    }

    /**
     * Stop all processors and pause the camera stream (Level 3).
     */
    public void stop() {
        // Pause the processor, this will also auto-close any VisionProcessors
        visionPortal.stopStreaming();
    }

    /**
     * Terminate all VisionPortal resources (Level 4).
     * <p>
     * Use this method when you are completely done with the VisionPortal and want to free up
     * all available resources. This method will automatically disable all processors and close
     * the VisionPortal, and cannot be undone without calling init() again.
     * <p>
     * It is strongly discouraged to reinitialise the VisionPortal in the same OpMode, as this
     * takes significant time and may cause the OpMode to hang or become unresponsive. Instead,
     * use the start() and stop() methods to enable/disable the VisionPortal.
     */
    public void terminate() {
        visionPortal.close();
    }

    /**
     * Get the current status of the camera attached to the VisionPortal.
     */
    public VisionPortal.CameraState getStatus() {
        return visionPortal.getCameraState();
    }

    /**
     * Get the current Frames Per Second of the VisionPortal.
     */
    public double getFps() {
        return visionPortal.getFps();
    }

    /**
     * Start or stop the live camera view (Level 1).
     * When initialised, live view is disabled by default.
     */
    public void setLiveView(boolean enabled) {
        if (enabled) {
            visionPortal.resumeLiveView();
        } else {
            visionPortal.stopLiveView();
        }
    }

    /**
     * Tick the camera stream and extract data from the processors.
     * This data is stored in the instance and can be accessed with the getters.
     */
    public void tick() {
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            // Camera must be initialised and streaming before we can extract data
            return;
        }

        // For every processor, check if it is enabled and extract data if it is
        for (Processors processor : Processors.values()) {
            switch (processor) {
                case TFOD:
                    if (tfod != null && visionPortal.getProcessorEnabled(tfod)) {
                        interpretTfod();
                    }
                    break;
                case APRILTAG:
                    if (aprilTag != null && visionPortal.getProcessorEnabled(aprilTag)) {
                        interpretAprilTag();
                    }
                    break;
            }
        }
    }

    private void interpretAprilTag() {
        List<AprilTagDetection> detections = aprilTag.getFreshDetections();
        if (detections == null) {
            return;
        }
        aprilTagData.clear();
        for (AprilTagDetection detection : detections) {
            aprilTagData.add(new AprilTagData(
                    detection.id,
                    detection.hamming,
                    detection.decisionMargin,
                    detection.center,
                    detection.corners,
                    detection.metadata != null ? detection.metadata.name : null,
                    detection.metadata != null ? detection.metadata.tagsize : null,
                    detection.metadata != null ? detection.metadata.fieldPosition : null,
                    detection.metadata != null ? detection.metadata.fieldOrientation : null,
                    detection.metadata != null ? detection.metadata.distanceUnit : null,
                    detection.ftcPose != null ? detection.ftcPose.x : null,
                    detection.ftcPose != null ? detection.ftcPose.y : null,
                    detection.ftcPose != null ? detection.ftcPose.z : null,
                    detection.ftcPose != null ? detection.ftcPose.pitch : null,
                    detection.ftcPose != null ? detection.ftcPose.roll : null,
                    detection.ftcPose != null ? detection.ftcPose.yaw : null,
                    detection.ftcPose != null ? detection.ftcPose.range : null,
                    detection.ftcPose != null ? detection.ftcPose.bearing : null,
                    detection.ftcPose != null ? detection.ftcPose.elevation : null,
                    detection.rawPose,
                    detection.frameAcquisitionNanoTime
            ));
        }
    }

    private void interpretTfod() {
        List<Recognition> recognitions = tfod.getFreshRecognitions();
        if (recognitions == null) {
            return;
        }
        tfodData.clear();
        for (Recognition recognition : recognitions) {
            tfodData.add(new TfodData(
                    recognition.getLabel(),
                    recognition.getConfidence(),
                    recognition.getLeft(),
                    recognition.getTop(),
                    recognition.getRight(),
                    recognition.getBottom(),
                    recognition.getWidth(),
                    recognition.getHeight(),
                    recognition.getImageWidth(),
                    recognition.getImageHeight(),
                    recognition.estimateAngleToObject(AngleUnit.DEGREES),
                    recognition.estimateAngleToObject(AngleUnit.RADIANS)
            ));
        }
    }

    /**
     * Primary getter for all TFOD data.
     *
     * @return List of all TFOD objects and their data
     */
    public List<TfodData> getTfodData() {
        if (tfod == null) {
            throw new IllegalStateException("TFOD processor was never initialised with init()");
        }
        return tfodData;
    }

    /**
     * Primary getter for all AprilTag data.
     *
     * @return List of all AprilTag objects and their data
     */
    public List<AprilTagData> getAprilTagData() {
        if (aprilTag == null) {
            throw new IllegalStateException("AprilTag processor was never initialised with init()");
        }
        return aprilTagData;
    }

    /**
     * EXPERIMENTAL METHOD: USE WITH CAUTION
     * <p>
     * Add a custom VisionProcessor that is not defined by the enum system.
     * This method may is expensive to call as it will reconstruct the VisionPortal!
     * If using purely non-enum processors, you should not call init() prior to this method.
     * </p>
     * All onboard processors will be enabled by default, you must call management methods to disable them.
     * If possible, try to avoid this but instead append functionality to this file to have it
     * interop with the existing enum-based system. If you choose to not do this, you will have
     * to fully manage your own VisionProcessor as Vision will not have instance management over it.
     */
    public void experimentallyUseCustomProcessor(VisionProcessor processor) {
        if (visionPortal != null)
            visionPortal.close();
        if (processor instanceof TfodProcessor || processor instanceof AprilTagProcessor) {
            throw new IllegalArgumentException("Use init() method instead to initialise AT or TFOD!");
        }
        // Reconstruct the VisionPortal with the new processor
        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (tfod != null) {
            builder.addProcessor(tfod);
        }
        if (aprilTag != null) {
            builder.addProcessor(aprilTag);
        }
        builder.addProcessor(processor);
        visionPortal = constructVisionPortal(builder);
        visionPortal.stopLiveView();
    }

    /**
     * Start or stop a custom VisionProcessor that is not AprilTag or TFOD. (Level 2)
     */
    public void setCustomProcessorState(VisionProcessor processor, boolean state) {
        if (visionPortal == null) {
            throw new IllegalStateException("VisionPortal is not initialised!");
        }
        if (processor instanceof TfodProcessor || processor instanceof AprilTagProcessor) {
            throw new IllegalArgumentException("Use stop() or start() methods instead to control AT or TFOD!");
        }
        // Will throw an exception if the processor is not registered, handled by BYO
        visionPortal.setProcessorEnabled(processor, state);
    }

    public enum Processors {
        /**
         * Caution! Using TFOD and using OpModes with high load may cause a watchdog timeout.
         * Ensure to test for this, as system memory may deplete and cause unexpected behaviour.
         */
        TFOD,
        APRILTAG,
        // Custom processors
        TEAM_PROP
    }
}