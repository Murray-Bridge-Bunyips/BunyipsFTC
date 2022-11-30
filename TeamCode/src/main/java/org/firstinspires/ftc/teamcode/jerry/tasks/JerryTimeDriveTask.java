package org.firstinspires.ftc.teamcode.jerry.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;

// Base drive task which will run XYR speed for a given time
// Only used for tests, do not use in actual OpMode as field positioning data is lost
public class JerryTimeDriveTask extends Task implements TaskImpl {

    private final JerryDrive drive;
    private final double x, y, r;

    public JerryTimeDriveTask(BunyipsOpMode opMode, double time, JerryDrive drive, double x, double y, double r) {
        super(opMode, time);
        this.drive = drive;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void run() {
        drive.setSpeedXYR(x, y, r);
        drive.update();

        if (isFinished()) {
            drive.deinit();
            return;
        }
    }

}
