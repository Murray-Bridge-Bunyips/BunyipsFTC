package org.firstinspires.ftc.teamcode.proto.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;

public class ProtoRotationTask extends BaseTask implements Task {

    private final IMUOp imu;
    private final ProtoDrive drive;
    private final double angle, speed;

    public ProtoRotationTask(BunyipsOpMode opMode, double time, IMUOp imu, ProtoDrive drive, double angle, double speed) {
        super(opMode, time);
        this.imu = imu;
        this.drive = drive;
        this.angle = angle;
        this.speed = speed;
    }

    // Enum to find out which way we need to be turning
    Direction direction;
    private enum Direction {
        LEFT,
        RIGHT
    }

    @Override
    public void init() {
        super.init();
        imu.tick();
        double currentAngle = imu.getHeading();

        // Find out which way we need to turn based on the information provided
        if (currentAngle < angle && angle <= 180) {
            // Faster to turn right to get to the target. If the desired angle is 180 degrees,
            // will also turn right (as it is equal, just mere preference)
            direction = Direction.RIGHT;
        } else {
            // Faster to turn left to get to the target
            direction = Direction.LEFT;
        }
    }

    @Override
    public void run() {
        switch (direction) {
            case LEFT:
                drive.setSpeedXYR(0, 0, -speed);
                break;
            case RIGHT:
                drive.setSpeedXYR(0, 0, speed);
                break;
        }
        imu.tick();
        drive.update();
    }
}
