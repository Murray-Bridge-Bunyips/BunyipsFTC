package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.pipelines.QRPark;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class GetQRSleeveTask extends BaseTask implements Task {

    private final CameraOp cam;
    private QRPark qr;

    public GetQRSleeveTask(BunyipsOpMode opMode, double time, CameraOp cam) {
        super(opMode, time);
        this.cam = cam;
    }

    @Override
    public void init() {
        super.init();
        // Make sure camera is in OpenCV mode
        if (cam.getMode() != CameraOp.CamMode.OPENCV)
            cam.swapModes();

        // Setup the pipeline for operation
        qr = new QRPark();
        cam.setPipeline(qr);
    }

    @Override
    public void run() {
        if (isFinished()) {
            // If we run out of time, then select the second value as a guess
            // It isn't calculated at all, but there's a 1/3 chance we're right...
            opMode.globalStorage.updateItem("PARKING_POSITION", "CENTER");
            return;
        }

        // Try to get the position of the sleeve using QR codes
        // The string to parking position conversion is done by the pipeline
        QRPark.ParkingPosition result = qr.getPosition();
        if (result != null) {
            opMode.globalStorage.updateItem("PARKING_POSITION", String.valueOf(result));
            isFinished = true;
            return;
        }
    }
}
