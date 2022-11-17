package org.firstinspires.ftc.teamcode.proto.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.proto.config.ProtoDrive;
import org.firstinspires.ftc.teamcode.common.Deadwheel;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

// Advanced drive task which will use the deadwheel encoders to X Y position on field
// For this robot, we don't actually need a precision IMU drive, and as such we don't need to
// implement one (although IMUOp has the methods available to make this work)
public class ProtoDeadwheelDriveTask extends BaseTask implements Task {

    private final ProtoDrive drive;
    private final Deadwheel x, y;
    private final double px_mm, py_mm, xspeed, yspeed;

    public ProtoDeadwheelDriveTask(BunyipsOpMode opMode, double time, ProtoDrive drive, Deadwheel x, Deadwheel y, double px_mm, double py_mm, double xspeed, double yspeed) {
        super(opMode, time);
        this.drive = drive;
        this.x = x;
        this.y = y;

        // Subtract 6 centimetres from the target distance to account for momentum
        this.px_mm = px_mm - 60;
        this.py_mm = py_mm - 60;

        this.xspeed = xspeed;
        this.yspeed = yspeed;
    }

    @Override
    public void run() {
        // Run x before y, moving until the goal is reached
        // Only start the encoder that needs to be tracked, to prevent false readings

        x.enableTracking();
        while (x.getTravelledMM() <= px_mm && !isFinished()) {
            drive.setSpeedXYR(xspeed, 0, 0);
            drive.update();
        }
        x.disableTracking();

        y.enableTracking();
        while (y.getTravelledMM() <= py_mm && !isFinished()) {
            drive.setSpeedXYR(0, yspeed, 0);
            drive.update();
        }
        y.disableTracking();

        if (isFinished()) {
            drive.deinit();
            return;
        }
    }
}