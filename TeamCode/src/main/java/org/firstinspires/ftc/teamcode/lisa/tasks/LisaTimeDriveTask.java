package org.firstinspires.ftc.teamcode.lisa.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive;

public class LisaTimeDriveTask extends Task implements TaskImpl {

    private final LisaDrive drive;
    private final double leftSpeed;
    private final double rightSpeed;

    public LisaTimeDriveTask(BunyipsOpMode opMode, double time, LisaDrive drive, double leftSpeed, double rightSpeed) {
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
