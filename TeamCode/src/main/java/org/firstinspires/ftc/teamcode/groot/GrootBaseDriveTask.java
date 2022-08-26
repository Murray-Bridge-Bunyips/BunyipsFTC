package org.firstinspires.ftc.teamcode.groot;

public class GrootBaseDriveTask extends BaseTask implements Task {

    private final GrootDrive drive;
    private final double leftSpeed;
    private final double rightSpeed;

    public GrootBaseDriveTask(BunyipsOpMode opMode, double time, GrootDrive drive, double leftSpeed, double rightSpeed) {
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
        drive.setSpeed(leftSpeed, rightSpeed);
        drive.update();
        if (isFinished()) {
            drive.setSpeed(0, 0);
            drive.update();
            return;
        }
    }

}