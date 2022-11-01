package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;

public class TFODDetectionTask extends BaseTask implements Task {

    private final CameraOp cam;
    private String results = null;

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
        // Check the signal and determine whether it can calculate which one it is (1, 2, 3)
        // If the time restraint is reached, then return default value listed in CameraOp
        while (results == null) {
            // If we hit the time constraint, stop TFOD and exit.
            // Automatically, CameraOp is designed to select the LEFT parking position if not modified
            if (isFinished()) {
                opMode.telemetry.addLine("No parking position found. Defaulting to LEFT.");
                cam.stopTFOD();
                return;
            }
            cam.tick();
            results = cam.determineTFOD();
        }

        // CameraOp should automatically save a parking position (and String) to the camera instance,
        // so we can report it to telemetry and finish task. We do not have to do anything further
        // in terms of saving this result.
        opMode.telemetry.addLine("Parking position found: " + cam.parkingPosition);
        isFinished = true;
        cam.stopTFOD();
    }
}
