package org.firstinspires.ftc.teamcode.jerry.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Deadwheel;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;

public class JerryPrecisionDriveTask extends Task implements TaskImpl {

    private final JerryDrive drive;
    private final IMUOp imu;
    private final Deadwheel xe, ye;
    private final double x, y, speed;

    public JerryPrecisionDriveTask(BunyipsOpMode opMode, double time, JerryDrive drive, IMUOp imu, Deadwheel xe, Deadwheel ye, double speed, double x, double y) {
        super(opMode, time);
        this.drive = drive;
        this.imu = imu;
        this.speed = speed;
        this.xe = xe;
        this.ye = ye;
        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {
        super.init();
        imu.startCapture();
        xe.enableTracking();
        ye.enableTracking();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || (xe.targetReached(x) && ye.targetReached(y));
    }

    @Override
    public void run() {
        if (isFinished()) {
            drive.deinit();
            imu.resetCapture();
            xe.disableTracking();
            ye.disableTracking();
            return;
        }

        double xspeed = xe.getTravelledMM() < x ? speed : -speed;
        double yspeed = ye.getTravelledMM() < y ? speed : -speed;

        if (!xe.targetReached(x)) {
            drive.setSpeedXYR(xspeed, 0, imu.getRPrecisionSpeed(0, 3));
        } else if (!ye.targetReached(y)) {
            drive.setSpeedXYR(0, yspeed, imu.getRPrecisionSpeed(0, 3));
        }

        drive.update();
        imu.tick();
    }
}
