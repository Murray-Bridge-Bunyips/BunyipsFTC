package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;

public class TFODDetectionTask extends Task implements TaskImpl {

    private final CameraOp cam;

    public TFODDetectionTask(BunyipsOpMode opMode, double time, CameraOp cam) {
        super(opMode, time);
        this.cam = cam;
    }

    @Override
    public void init() {
        super.init();
        cam.startTFOD();
    }

    @Override
    public void run() {
        // Check the TFOD task and determine whether it can calculate which one based on the labels
        // If the time restraint is reached, then return default value selected here.
        while (cam.determineTFOD() == null) {
            // If we hit the time constraint, stop TFOD detections and select a default.
            if (isFinished()) {
                opMode.telemetry.addLine("No TFOD label found. Defaulting to 1 (LEFT).");
                cam.seeingTfod = CameraOp.LABELS[0];
                cam.stopTFOD();
                return;
            }
            cam.tick();
        }

        // CameraOp should automatically save the string of the TFOD element to its own instance
        // so we can call finished and end the task as there is nothing more to do.
        isFinished = true;
        cam.stopTFOD();
    }
}
