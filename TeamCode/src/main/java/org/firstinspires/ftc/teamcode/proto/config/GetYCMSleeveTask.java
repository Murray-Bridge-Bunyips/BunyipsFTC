package org.firstinspires.ftc.teamcode.proto.config;


import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class GetYCMSleeveTask extends BaseTask implements Task {

    private final CameraOp cam;

    public GetYCMSleeveTask(BunyipsOpMode opMode, double time, CameraOp cam) {
        super(opMode, time);
        this.cam = cam;
    }

    @Override
    public void init() {
        super.init();
        // Make sure we're running in OpenCV mode as this task is designed for OpenCV
        if (cam.getMode() != CameraOp.CamMode.OPENCV)
            cam.swapModes();
    }

    @Override
    public void run() {
        if (isFinished()) {
            // If we run out of time, save the middle value for a 1/3 chance of being right
            opMode.globalStorage.updateItem("PARKING_POSITION", "CENTER");
        }
    }
}
