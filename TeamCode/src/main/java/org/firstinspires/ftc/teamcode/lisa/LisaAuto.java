package org.firstinspires.ftc.teamcode.lisa;

@Autonomous(name="<LISA> 24M Straight Drive")
public class LisaAuto extends BunyipsOpMode {

    final private LisaConfiguration config;
    final private LisaDrive drive = null;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = LisaConfiguration.newConfig(hardwareMap, telemetry);

        try {
            drive = new LisaDrive(this,
                        config.left, config.right);
        } catch (Exception e) {
            telemetry.addLine("Failed to initalise motors.");
        }

        tasks.add(new MessageTask(this, 3, "Activating drive in three seconds."));
        tasks.add(new LisaBaseDriveTask(this, 2, drive, 0.1, 0.1));
        // tasks.add(new LisaIMUTask(this, 4, drive, 0.2, true, config.imu, 90));
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