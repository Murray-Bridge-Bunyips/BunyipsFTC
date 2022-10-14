package org.firstinspires.ftc.teamcode.proto;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;

public class ProtoTeleOp extends BunyipsOpMode {

    private ProtoConfig config;
    private ProtoDrive drive;
    private CameraOp cam;

    @Override
    protected void onInit() {
        config = ProtoConfig.newConfig(hardwareMap, telemetry);
        try {
            cam = new CameraOp(this, config.webcam, config.tfodMonitorViewId);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Camera Operation.");
        }
        try {
            drive = new ProtoDrive(this, config.bl, config.br, config.fl, config.fr, true);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Drive System.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        double x = gamepad1.right_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.left_stick_x;
    }
}
