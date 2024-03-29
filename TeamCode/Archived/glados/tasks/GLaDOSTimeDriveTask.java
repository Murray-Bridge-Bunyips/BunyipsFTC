package org.firstinspires.ftc.teamcode.glados.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Cartesian;
import org.firstinspires.ftc.teamcode.common.Controller;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

/**
 * Utilise time control to drive GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
public class GLaDOSTimeDriveTask extends Task {
    private final MecanumDrive drive;
    private final double x;
    private final double y;
    private final double r;

    public GLaDOSTimeDriveTask(@NonNull BunyipsOpMode opMode, double time, MecanumDrive drive, double x, double y, double r) {
        super(opMode, time);
        this.drive = drive;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void init() {
        // noop
    }

    @Override
    public void run() {
        drive.setWeightedDrivePower(Cartesian.toPose(x, y, r));
        drive.update();
    }

    @Override
    public boolean isTaskFinished() {
        return false;
    }

    @Override
    public void onFinish() {
        drive.stop();
    }
}
