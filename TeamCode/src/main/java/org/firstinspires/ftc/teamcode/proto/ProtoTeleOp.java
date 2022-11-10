package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.pipelines.TriColourSleeve;
import org.firstinspires.ftc.teamcode.proto.config.ProtoArm;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;
import org.openftc.easyopencv.OpenCvPipeline;

@TeleOp(name = "<PROTO> TeleOp testing")
public class ProtoTeleOp extends BunyipsOpMode {

    private ProtoConfig config;
//    private ProtoDrive drive;
    private CameraOp cam;
    private IMUOp imu;
//    private ProtoArm arm;

    @Override
    protected void onInit() {
        config = ProtoConfig.newConfig(hardwareMap, telemetry);
        try {
            cam = new CameraOp(this, config.webcam, config.monitorID);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Camera Operation.");
        }
//        try {
//            drive = new ProtoDrive(this, config.bl, config.br, config.fl, config.fr);
//        } catch (Exception e) {
//            telemetry.addLine("Failed to initialise Drive System.");
//        }
//        try {
//            arm = new ProtoArm(this, config.claw, config.arm);
//        } catch (Exception e) {
//            telemetry.addLine("Failed to initialise Arm System.");
//        }
//        try {
//            imu = new IMUOp(this, config.imu);
//        } catch (Exception e) {
//            telemetry.addLine("Failed to initalise IMU Operation.");
//        }

        // Using TFOD and Vuforia for debugging purposes, will likely
        // not use this in actual TeleOp due to resource consumption
        cam.startVuforia();
        cam.startTFOD();
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        // Set changing variables and gather raw data
//        double x = gamepad1.left_stick_x;
//        double y = gamepad1.left_stick_y;
//        double r = gamepad1.right_stick_x;
//        double y2 = gamepad2.left_stick_y;
        
        // Using for debug telemetry during testing phases
        cam.tick();
        OpenGLMatrix VuforiaMatrix = cam.getTargetRawMatrix();
        String tfodDetection = cam.determineTFOD();

//          imu.tick();
//          telemetry.addLine(String.format("Heading: %.2f, Roll: %.2f, Pitch: %.2f",
//                                          imu.getHeading(), imu.getRoll(), imu.getPitch()));

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
//        arm.clawRun(y2);
//        drive.update();
//        arm.update();
    }
}
