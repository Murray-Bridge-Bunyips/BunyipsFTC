package org.firstinspires.ftc.teamcode.common.tasks

import android.annotation.SuppressLint
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.CameraOp.CamMode
import org.firstinspires.ftc.teamcode.common.pipelines.AprilTagDetectionPipeline

/**
 * Intermediate task for using the AprilTagDetectionPipeline to detect a Signal position during an initLoop.
 * @author Lucas Bubner, FTC 15215; Nov 2022
 */
class GetAprilTagTask(opMode: BunyipsOpMode, private val cam: CameraOp) : Task(opMode), TaskImpl {
    private var at: AprilTagDetectionPipeline? = null
    private var noDetections = 0

    // Decimation thresholds, calibrate as needed
    val DECIMATION_HIGH = 3f
    val DECIMATION_LOW = 2f
    val DECIMATION_HIGH_METERS_THRESHOLD = 1.0f
    val DECIMATION_LOW_THRESHOLD = 4

    /**
     * Get the saved position of where to park.
     * @return An enum of either LEFT, CENTER, or RIGHT determining where to park
     */
    @Volatile
    var position: ParkingPosition? = null
        private set

    enum class ParkingPosition {
        LEFT, CENTER, RIGHT
    }

    override fun init() {
        super.init()

        // Ensure camera is in the correct mode
        if (cam.mode != CamMode.OPENCV) cam.swapModes()

        // Tag size in metres
        val tagsize = 0.166

        // Lens intrinsics calibrations, units in pixels
        // This is calibrated for the Logitech C920 camera, FTC season 2022-2023
        val fx = 578.272
        val fy = 578.272
        val cx = 402.145
        val cy = 221.506
        at = AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy)
        cam.setPipeline(at)
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || position != null
    }

    @SuppressLint("DefaultLocale")
    override fun run() {
        // Caution! ParkingPosition will be null if the camera does not pick up anything in it's task runtime.
        // Be sure to check if ParkingPosition is null before setting up your specific tasks, to handle a fallback value.
        if (isFinished()) return
        val detections = at?.detectionsUpdate
        // Check if there are new frames
        if (detections != null) {
            // If there are, check if we see any tags
            if (detections.size > 0) {
                // If we do, set parking position based on this information and end the task
                // Will compare detected tags to the APRILTAG_ID array.
                if (detections[0].pose.z < DECIMATION_HIGH_METERS_THRESHOLD) at?.setDecimation(
                    DECIMATION_HIGH
                )
                for (detection in detections) {
                    when (detection.id) {
                        17 -> {
                            position = ParkingPosition.LEFT
                            return
                        }

                        13 -> {
                            position = ParkingPosition.CENTER
                            return
                        }

                        7 -> {
                            position = ParkingPosition.RIGHT
                            return
                        }

                        else -> {}
                    }
                }
            } else {
                // Otherwise, count these frames since our last detection
                // and change to low decimation if it's been long enough
                noDetections++
                if (noDetections >= DECIMATION_LOW_THRESHOLD) at?.setDecimation(DECIMATION_LOW)
            }
        }
    }
}