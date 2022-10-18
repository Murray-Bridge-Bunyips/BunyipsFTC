package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.proto.config.ProtoArmControl;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;

@TeleOp(name = "<PROTO> TeleOp testing")
public class ProtoTeleOp extends BunyipsOpMode {

    private ProtoConfig config;
    private ProtoDrive drive;
    private CameraOp cam;
    private ProtoArmControl arm;

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
        try {
            arm = new ProtoArmControl(this, config.claw, config.arm);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Arm System.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        double x = gamepad1.right_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.left_stick_x;
        boolean up_pressed = gamepad2.dpad_up;
        boolean down_pressed = gamepad2.dpad_down;
        boolean drop_pressed = gamepad2.left_bumper;
        if (up_pressed) {
            arm.runClaw(1);
        } else if (down_pressed) {
            arm.runClaw(-1);
        } else {
            arm.stopClaw();
        }
    }
}
