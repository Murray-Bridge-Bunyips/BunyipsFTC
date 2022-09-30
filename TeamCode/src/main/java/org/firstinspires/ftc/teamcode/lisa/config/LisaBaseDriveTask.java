package org.firstinspires.ftc.teamcode.lisa.config;

import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class LisaBaseDriveTask extends BaseTask implements Task {

    private final LisaDrive drive;
    private final double leftSpeed;
    private final double rightSpeed;

    public LisaBaseDriveTask(BunyipsOpMode opMode, double time, LisaDrive drive, double leftSpeed, double rightSpeed) {
        super(opMode, time);
        this.drive = drive;
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void run() {
        drive.setPower(-leftSpeed, -rightSpeed);
        drive.update();
        if (isFinished()) {
            drive.setPower(0, 0);
            drive.update();
            return;
        }

    }

}
