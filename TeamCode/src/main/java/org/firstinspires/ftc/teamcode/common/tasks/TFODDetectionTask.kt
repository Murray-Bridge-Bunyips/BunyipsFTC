package org.firstinspires.ftc.teamcode.common.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp

/**
 * Task to detect the TensorFlow element defined in CameraOp.
 */
class TFODDetectionTask(opMode: BunyipsOpMode, time: Double, private val cam: CameraOp) :
    Task(opMode, time), TaskImpl {
    override fun init() {
        super.init()
        cam.startTFOD()
    }

    override fun run() {
        // Check the TFOD task and determine whether it can calculate which one based on the labels
        // If the time restraint is reached, then return default value selected here.
        while (cam.determineTFOD() == null) {
            // If we hit the time constraint, stop TFOD detections and select a default.
            if (isFinished()) {
                opMode.addTelemetry("No TFOD label found. Defaulting to 1 (LEFT).")
                cam.seeingTfod = CameraOp.LABELS[0]
                cam.stopTFOD()
                return
            }
            cam.tick()
        }

        // CameraOp should automatically save the string of the TFOD element to its own instance
        // so we can call finished and end the task as there is nothing more to do.
        taskFinished = true
        cam.stopTFOD()
    }
}