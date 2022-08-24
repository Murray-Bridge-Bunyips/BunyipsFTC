package org.firstinspires.ftc.teamcode.lisa;

import org.firstinspires.ftc.teamcode.common.BaseTask;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Task;

public class LisaEncoderDriveTask extends BaseTask implements Task {

    private final LisaDrive drive;
    private final double leftDistance;
    private final double rightDistance;
    private final double leftPower;
    private final double rightPower;

    public LisaEncoderDriveTask(BunyipsOpMode opMode, double time, LisaDrive drive, double leftCM, double rightCM, double leftPower, double rightPower) {
        super(opMode, time);
        this.drive = drive;

        final double TICKS_PER_REVOLUTION = 0; // TODO
        final double WHEEL_DIAMETER_CM = 0; // TODO

        this.leftDistance = (leftCM * 10) * TICKS_PER_REVOLUTION / ((WHEEL_DIAMETER_CM / 10) * Math.PI);
        this.rightDistance = (rightCM * 10) * TICKS_PER_REVOLUTION / ((WHEEL_DIAMETER_CM / 10) * Math.PI);
        this.leftPower = leftPower;
        this.rightPower = rightPower;
    }

    @Override
    public void init() {
        super.init();
        drive.setEncoder(true);
        drive.setTargetPosition(leftDistance, rightDistance);
        drive.setPower(leftPower, rightPower);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || drive.targetPositionReached();
    }

    @Override
    public void run() {
        if (isFinished()) {
            drive.setPower(0, 0);
            drive.update();
            drive.setEncoder(false);
            return;
        }
        drive.update();
    }
}