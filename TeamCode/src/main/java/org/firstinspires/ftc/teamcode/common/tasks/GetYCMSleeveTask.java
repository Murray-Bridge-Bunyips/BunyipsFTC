package org.firstinspires.ftc.teamcode.common.tasks;


import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.pipelines.TriColourSleeve;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class GetYCMSleeveTask extends BaseTask implements Task {

    private final CameraOp cam;
    private TriColourSleeve tcs;

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
        tcs = new TriColourSleeve();
        cam.setPipeline(tcs);
    }

    @Override
    public void run() {
        if (isFinished()) {
            // If we run out of time, save the middle value for a 1/3 chance of being right
            opMode.globalStorage.updateItem("PARKING_POSITION", "CENTER");
            return;
        }
        TriColourSleeve.ParkingPosition result = tcs.getPosition();
        if (result != null) {
            opMode.globalStorage.updateItem("PARKING_POSITION", String.valueOf(result));
            isFinished = true;
            return;
        }
    }
}
