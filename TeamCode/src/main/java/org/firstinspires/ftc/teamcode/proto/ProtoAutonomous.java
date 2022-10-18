package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.TFODDetectionTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.proto.config.ProtoConfig;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;
import org.firstinspires.ftc.teamcode.proto.config.ProtoSignalParkTask;

import java.util.ArrayDeque;

@Autonomous(name="<PROTO> Autonomous Testing")
public class ProtoAutonomous extends BunyipsOpMode {

    private ProtoConfig config;
    private CameraOp cam = null;
    private ProtoDrive drive;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

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


        // Add tasks here
        tasks.add(new TFODDetectionTask(this, 5, cam));
        // tasks.add(new ProtoSignalParkTask(this, 7));
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