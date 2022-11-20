package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.proto.config.ProtoArm;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;
import org.firstinspires.ftc.teamcode.common.tasks.GetQRSleeveTask;

import java.util.ArrayDeque;

@Autonomous(name="<PROTO> Autonomous Testing")
public class ProtoAutonomous extends BunyipsOpMode {

    private ProtoConfig config;
    private CameraOp cam = null;
    private ProtoDrive drive;
    private ProtoArm arm;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = ProtoConfig.newConfig(hardwareMap, telemetry);
        try {
            cam = new CameraOp(this, config.webcam, config.monitorID, CameraOp.CamMode.OPENCV);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Camera Operation.");
        }
        try {
            drive = new ProtoDrive(this, config.bl, config.br, config.fl, config.fr);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Drive System.");
        }
        try {
            arm = new ProtoArm(this, config.claw1, config.claw2, config.arm1, config.arm2, config.limit);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Arm System.");
        }


        // 1. Detect signal position (with OpenCV) and save it to a variable in OpMode GlobalStorage
        // Run this as soon as we hit init and armed the tasks, as we are permitted to do so.
        Task initTask = new GetQRSleeveTask(this, 5, cam);
        initTask.run();
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        Task currentTask = tasks.peekFirst();
        if (currentTask == null) {
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()) {
            tasks.removeFirst();
        }
        if (tasks.isEmpty()) {
            drive.deinit();
        }
    }
}
