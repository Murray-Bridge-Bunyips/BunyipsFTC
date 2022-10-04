package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;

public class TFODDetectionTask extends BaseTask implements Task {

    private final CameraOp cam;
    private String seeingtfod = null;

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
        // If the time restraint is reached, then return the second
        while (seeingtfod == null) {
            // If we hit the time constraint, return the latest result; and if that doesn't exist, return the second
            if (isFinished()) {
                cam.stopTFOD();
                cam.seeingTfod = cam.bestguess != null ? cam.bestguess : CameraOp.LABELS[1];
                return;
            }
            cam.tick();
            seeingtfod = cam.determineTFOD();
        }

        // Write it to the CameraOp variables, and return task as finished
        cam.seeingTfod = seeingtfod;
        isFinished = true;
        cam.stopTFOD();
    }
}
