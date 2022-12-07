package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.pipelines.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

/**
 * Intermediate task for using the AprilTagDetectionPipeline to detect a Signal position during an initLoop.
 * @author Lucas Bubner, FTC 15215; Nov 2022
 */
public class GetAprilTagTask extends Task implements TaskImpl {

    private final CameraOp cam;
    private AprilTagDetectionPipeline at;
    private int noDetections = 0;

    // Decimation thresholds, calibrate as needed
    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float DECIMATION_HIGH_METERS_THRESHOLD = 1.0f;
    final int DECIMATION_LOW_THRESHOLD = 4;

    // We also use an instance variable to store the result
    private volatile ParkingPosition position = null;
    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    /**
     * Get the saved position of where to park.
     * @return An enum of either LEFT, CENTER, or RIGHT determining where to park
     */
    public ParkingPosition getPosition() {
        return position;
    }

    public GetAprilTagTask(BunyipsOpMode opMode, CameraOp cam) {
        super(opMode);
        this.cam = cam;
    }

    @Override
    public void init() {
        super.init();

        // Ensure camera is in the correct mode
        if (cam.getMode() != CameraOp.CamMode.OPENCV)
            cam.swapModes();

        // Tag size in metres
        double tagsize = 0.166;

        // Lens intrinsics calibrations, units in pixels
        // TODO: This configuration is for the C920 webcam at 800x448. This must be calibrated.
        double fx = 578.272;
        double fy = 578.272;
        double cx = 402.145;
        double cy = 221.506;

        at = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        cam.setPipeline(at);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || position != null;
    }

    @Override
    public void run() {
        // Caution! ParkingPosition will be null if the camera does not pick up anything in it's task runtime.
        // Be sure to check if ParkingPosition is null before setting up your specific tasks, to handle a fallback value.
        if (isFinished()) return;

        ArrayList<AprilTagDetection> detections = at.getDetectionsUpdate();
        // Check if there are new frames
        if (detections != null) {
            // If there are, check if we see any tags
            if (detections.size() > 0) {
                // If we do, set parking position based on this information and end the task
                // Will compare detected tags to the APRILTAG_ID array.
                if (detections.get(0).pose.z < DECIMATION_HIGH_METERS_THRESHOLD)
                    at.setDecimation(DECIMATION_HIGH);

                for (AprilTagDetection detection : detections) {
                    switch (detection.id) {
                        // LEFT POSITION APRILTAG
                        case 17:
                            position = ParkingPosition.LEFT;
                            return;
                        // CENTER POSITION APRILTAG
                        case 13:
                            position = ParkingPosition.CENTER;
                            return;
                        // RIGHT POSITION APRILTAG
                        case 7:
                            position = ParkingPosition.RIGHT;
                            return;
                        default:
                            // Something strange happened and we managed to read a different tag!
                            // In this case, just ignore it and continue the task.
                            break;
                    }
                }
            } else {
                // Otherwise, count these frames since our last detection
                // and change to low decimation if it's been long enough
                noDetections++;
                if (noDetections >= DECIMATION_LOW_THRESHOLD)
                    at.setDecimation(DECIMATION_LOW);
            }
        }
    }
}
