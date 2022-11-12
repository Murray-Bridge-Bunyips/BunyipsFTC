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

@TeleOp(name = "<PROTO> POWERPLAY TeleOp")
public class ProtoTeleOp extends BunyipsOpMode {

    private ProtoDrive drive;
    private ProtoArm arm;

    @Override
    protected void onInit() {
        // Configure drive and arm subsystems
        ProtoConfig config = ProtoConfig.newConfig(hardwareMap, telemetry);
        try {
            drive = new ProtoDrive(this, config.bl, config.br, config.fl, config.fr);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Drive System.");
        }
        try {
            arm = new ProtoArm(this, config.claw, config.arm);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Arm System.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        // Set changing variables and gather raw data
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;
        double y2 = gamepad2.left_stick_y;

        boolean up_pressed = gamepad2.dpad_up;
        boolean down_pressed = gamepad2.dpad_down;
        boolean drop_pressed = gamepad2.left_bumper;
        
        // Set speeds of motors and interpret any data
        drive.setSpeedXYR(x, y, r);
        arm.liftSetPower(0.25);

        if (up_pressed && !gamepad2.dpad_up) {
            arm.liftUp();
        } else if (down_pressed && !gamepad2.dpad_down) {
            arm.liftDown();
        } else if (drop_pressed && !gamepad2.left_bumper) {
            arm.liftReset();
        }

        // Update live movements of all motors
        arm.clawRun(y2);
        drive.update();
        arm.update();
    }
}
