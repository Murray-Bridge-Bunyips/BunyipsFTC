package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.proto.config.ProtoArm;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "<PROTO> TeleOp testing")
public class ProtoTeleOp extends BunyipsOpMode {

    private ProtoConfig config;
//    private ProtoDrive drive;
    private CameraOp cam;
    private ProtoArm arm;

    @Override
    protected void onInit() {
        config = ProtoConfig.newConfig(hardwareMap, telemetry);
        try {
            cam = new CameraOp(this, config.webcam, config.tfodMonitorViewId);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Camera Operation.");
        }
//        try {
//            drive = new ProtoDrive(this, config.bl, config.br, config.fl, config.fr, true);
//        } catch (Exception e) {
//            telemetry.addLine("Failed to initialise Drive System.");
//        }
        try {
            arm = new ProtoArm(this, config.claw);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Arm System.");
        }

        // Using TFOD and Vuforia for debugging purposes, will likely
        // not use this in actual TeleOp due to resource consumption
        cam.startVuforia();
        cam.startTFOD();
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        // Set changing variables and gather raw data
//        double x = gamepad1.right_stick_x;
//        double y = gamepad1.left_stick_y;
//        double r = gamepad1.left_stick_x;
        double y2 = gamepad2.left_stick_y;
        
        // Using for debug telemetry during testing phases
        OpenGLMatrix VuforiaMatrix = cam.getTargetRawMatrix();
        String tfodDetection = cam.determineTFOD();

//        boolean up_pressed = gamepad2.dpad_up;
//        boolean down_pressed = gamepad2.dpad_down;
//        boolean drop_pressed = gamepad2.left_bumper;
        
        // Set speeds of motors and interpret any data
//        drive.setSpeedXYR(x, y, r);
//        arm.setLiftPower(0.25);

//        if (up_pressed && !gamepad2.dpad_up) {
//            arm.liftUp();
//        } else if (down_pressed && !gamepad2.dpad_down) {
//            arm.liftDown();
//        } else if (drop_pressed && !gamepad2.left_bumper) {
//            arm.liftReset();
//        }

        // Update live movements of all motors
        arm.clawRun(Range.clip(y2, -1, 1));
//        drive.update();
//        arm.update();
    }
}
