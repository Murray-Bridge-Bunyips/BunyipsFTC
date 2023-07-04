package org.firstinspires.ftc.teamcode.bertie.tasks;

import org.firstinspires.ftc.teamcode.bertie.components.BertieDrive;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
public class BertieTimeDriveTask extends Task implements TaskImpl {

    private final BertieDrive drive;
    private final double x;
    private final double y;
    private final double r;

    public BertieTimeDriveTask(BunyipsOpMode opMode, double time, BertieDrive drive, double x, double y, double r) {
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