package org.firstinspires.ftc.teamcode.lisa;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.MessageTask;
import org.firstinspires.ftc.teamcode.common.Task;

import java.util.ArrayDeque;

@Autonomous(name="<LISA> Autonomous Testing")
public class LisaAuto extends BunyipsOpMode {

    private LisaConfiguration config;
    private LisaDrive drive = null;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = LisaConfiguration.newConfig(hardwareMap, telemetry);

        try {
            drive = new LisaDrive(this,
                        config.left, config.right, config.fws, config.dws);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise drive.");
        }

        tasks.add(new MessageTask(this, 3, "Activating drive tasks in three seconds."));
        tasks.add(new LisaIMUTask(this, 4, drive, 0.2, true, config.imu, 90));
        // tasks.add(new LisaEncoderDriveTask(this, 5, drive, 10, 10, 0.3, 0.3));
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
            drive.setPower(0, 0);
            drive.update();
        }
    }
}