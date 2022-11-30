package org.firstinspires.ftc.teamcode.jerry.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;

public class JerryIMUDriveTask extends Task implements TaskImpl {

    private final JerryDrive drive;
    private final IMUOp imu;
    private final double x, y ,r;

    public JerryIMUDriveTask(BunyipsOpMode opMode, double time, JerryDrive drive, IMUOp imu, double x, double y, double r) {
        super(opMode, time);
        this.drive = drive;
        this.imu = imu;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void init() {
        super.init();
        imu.startCapture();
    }

    @Override
    public void run() {
        if (isFinished()) {
            drive.deinit();
            imu.resetCapture();
            return;
        }
        drive.setSpeedXYR(x, y, imu.getRPrecisionSpeed(r, 3));
        drive.update();
        imu.tick();
    }
}
