package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;

public class CameraDetectionTask extends BaseTask implements Task {

    private final CameraOp cam;

    public CameraDetectionTask(BunyipsOpMode opMode, double time, CameraOp cam) {
        super(opMode, time);
        this.cam = cam;
    }

    @Override
    public void run() {
        // Check the signal and determine whether it can calculate which one it is (1, 2, 3)
        // If the time restraint is reached, then return nothing which defaults to null
        String signal = cam.determineSignal();
        while (signal == null) {
            cam.tick();
            signal = cam.determineSignal();
        }

        // Write it to the CameraOp variables, returning null if it still hasn't seen a signal
        cam.seeingSignal = signal;
    }
}
