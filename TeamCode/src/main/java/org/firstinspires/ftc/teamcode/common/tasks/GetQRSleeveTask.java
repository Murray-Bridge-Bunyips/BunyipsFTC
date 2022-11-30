package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.pipelines.QRParkPipeline;
import org.firstinspires.ftc.teamcode.jerry.config.JerryArm;

public class GetQRSleeveTask extends Task implements TaskImpl {

    private final CameraOp cam;
    private final JerryArm arm;
    private QRParkPipeline qr;

    private volatile ParkingPosition position;
    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    public ParkingPosition getPosition() {
        return position;
    }

    public GetQRSleeveTask(BunyipsOpMode opMode, double time, CameraOp cam, JerryArm arm) {
        super(opMode, time);
        this.cam = cam;
        this.arm = arm;
    }

    @Override
    public void init() {
        super.init();
        // Make sure camera is in OpenCV mode
        if (cam.getMode() != CameraOp.CamMode.OPENCV)
            cam.swapModes();

        // Setup the pipeline for operation
        qr = new QRParkPipeline();
        cam.setPipeline(qr);
        arm.liftSetPower(0.4);
        arm.liftSetPosition(2);
    }

    @Override
    public void run() {
        // Null case needs to be done by the opMode, do this by checking the result if it is null,
        // and select a custom result based on the default (null) result
        if (isFinished()) return;
        arm.update();

        // Try to get the position of the sleeve using QR codes
        // The string to parking position conversion is done by the pipeline
        ParkingPosition result = qr.getPosition();
        opMode.telemetry.addLine("Waiting for QR Code detection... Currently seeing: " + String.valueOf(result));
        opMode.telemetry.update();
        if (result != null) {
            isFinished = true;
            return;
        }
    }
}
