package org.firstinspires.ftc.teamcode.proto.config;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;
import org.firstinspires.ftc.teamcode.common.Deadwheel;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class ProtoDeadwheelDriveTask extends BaseTask implements Task {

    private final ProtoDrive drive;
    private final Deadwheel x, y;
    private final double px_mm, py_mm, xspeed, yspeed;

    public ProtoDeadwheelDriveTask(BunyipsOpMode opMode, double time, ProtoDrive drive, Deadwheel x, Deadwheel y, double px_mm, double py_mm, double xspeed, double yspeed) {
        super(opMode, time);
        this.drive = drive;
        this.x = x;
        this.y = y;
        this.px_mm = px_mm;
        this.py_mm = py_mm;
        this.xspeed = xspeed;
        this.yspeed = yspeed;
    }

    @Override
    public void init() {
        super.init();
        x.enableTracking();
        y.enableTracking();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || (x.targetReached(px_mm) && y.targetReached(py_mm));
    }

    @Override
    public void run() {
        while (x.getTravelledMM() <= px_mm && !isFinished()) {
            drive.setSpeedXYR(xspeed, 0, 0);
            drive.update();
        }
        while (y.getTravelledMM() <= py_mm && !isFinished()) {
            drive.setSpeedXYR(0, yspeed, 0);
            drive.update();
        }
        if (isFinished()) {
            x.disableTracking();
            y.disableTracking();
            drive.deinit();
        }
    }
}