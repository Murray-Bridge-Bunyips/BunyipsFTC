package org.firstinspires.ftc.teamcode.lisa;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.lisa.components.LisaConfig;
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive;
import org.firstinspires.ftc.teamcode.lisa.tasks.LisaPrecisionDriveTask;

import java.util.ArrayDeque;

@Autonomous(name = "<LISA> Autonomous Testing")
public class LisaAutonomous extends BunyipsOpMode {

    private LisaConfig config;
    private LisaDrive drive = null;
    private final ArrayDeque<TaskImpl> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = LisaConfig.newConfig(hardwareMap, telemetry);

        try {
            drive = new LisaDrive(this,
                    config.left, config.right);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise drive.");
        }
        drive.setToBrake();
        tasks.add(new MessageTask(this, 3, "Activating drive tasks in three seconds."));
        // tasks.add(new LisaIMUTask(this, 4, drive, 0.2, config.imu, 90));
        tasks.add(new LisaPrecisionDriveTask(this, drive, config.imu, 100, 100000, 0.25, 3, 0.1));
        // tasks.add(new LisaEncoderDriveTask(this, 5, drive, 10, 10, 0.3, 0.3));
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        TaskImpl currentTask = tasks.peekFirst();
        if (currentTask == null) {
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()) {
            tasks.removeFirst();
        }
        if (tasks.isEmpty()) {
            drive.setPower(0, 0);
            drive.update();
        }
    }
}