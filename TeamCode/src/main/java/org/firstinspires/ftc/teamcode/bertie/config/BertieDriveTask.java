package org.firstinspires.ftc.teamcode.bertie.config;

import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class BertieDriveTask extends BaseTask implements Task {

    private final BertieBunyipDrive drive;
    private final double x;
    private final double y;
    private final double r;

    public BertieDriveTask(BunyipsOpMode opMode, double time, BertieBunyipDrive drive, double x, double y, double r) {
        super(opMode, time);
        this.drive = drive;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void run() {
        drive.setSpeedXYR(x,y,r);
        drive.update();
        if (isFinished()) {
            drive.setSpeedXYR(0,0,0);
            drive.update();
            return;
        }

    }

}
