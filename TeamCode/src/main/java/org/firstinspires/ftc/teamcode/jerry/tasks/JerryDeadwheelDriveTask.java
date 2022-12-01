package org.firstinspires.ftc.teamcode.jerry.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive;
import org.firstinspires.ftc.teamcode.common.Deadwheel;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;

// Advanced drive task which will use the deadwheel encoders to X Y position on field
// For this robot, we don't actually need a precision IMU drive, and as such we don't need to
// implement one (although IMUOp has the methods available to make this work)
public class JerryDeadwheelDriveTask extends Task implements TaskImpl {

    private final JerryDrive drive;
    private final Deadwheel x, y;
    private final double px_mm, py_mm, xspeed, yspeed;

    public JerryDeadwheelDriveTask(BunyipsOpMode opMode, double time, JerryDrive drive, Deadwheel x, Deadwheel y, double px_mm, double py_mm, double xspeed, double yspeed) {
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
    public boolean isFinished() {
        return super.isFinished() || (x.targetReached(px_mm) && y.targetReached(py_mm));
    }

    @Override
    public void run() {
        // Run x before y, moving until the goal is reached
        // Only start the encoder that needs to be tracked, to prevent false readings

        x.enableTracking();
        while (!x.targetReached(px_mm) && !isFinished()) {
            drive.setSpeedXYR(xspeed, 0, 0);
            drive.update();
        }
        x.disableTracking();

        y.enableTracking();
        while (!y.targetReached(py_mm) && !isFinished()) {
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