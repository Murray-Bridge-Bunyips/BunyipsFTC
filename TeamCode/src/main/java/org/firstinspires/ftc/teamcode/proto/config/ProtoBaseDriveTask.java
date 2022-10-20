package org.firstinspires.ftc.teamcode.proto.config;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class ProtoBaseDriveTask extends BaseTask implements Task {

    private final ProtoDrive drive;
    private final double x, y, r;

    public ProtoBaseDriveTask(BunyipsOpMode opMode, double time, ProtoDrive drive, double x, double y, double r) {
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
        drive.setSpeedXYR(x, y, r);
        drive.update();

        if (isFinished()) {
            drive.deinit();
            return;
        }
    }

}
