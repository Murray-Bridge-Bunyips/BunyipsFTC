package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.pipelines.QRPark;
import org.firstinspires.ftc.teamcode.common.pipelines.TriColourSleeve;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;

@TeleOp(name = "<PROTO> Cam Testing")
public class ProtoCamTesting extends BunyipsOpMode {

    private CameraOp cam;
    private QRPark qr;

    @Override
    protected void onInit() {
        ProtoConfig config = ProtoConfig.newConfig(hardwareMap, telemetry);
        try {
            cam = new CameraOp(this, config.webcam, config.monitorID, CameraOp.CamMode.OPENCV);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Camera Operation.");
        }
        qr = new QRPark();
        cam.setPipeline(qr);
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        telemetry.addData("QR Parking Position", String.valueOf(qr.getPosition()));
    }
}
