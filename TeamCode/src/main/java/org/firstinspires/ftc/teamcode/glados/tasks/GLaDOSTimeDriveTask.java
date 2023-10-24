package org.firstinspires.ftc.teamcode.glados.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSPOVDriveCore;

public class GLaDOSTimeDriveTask extends Task {
    private final GLaDOSPOVDriveCore drive;
    private final double x;
    private final double y;
    private final double r;

    public GLaDOSTimeDriveTask(@NonNull BunyipsOpMode opMode, double time, GLaDOSPOVDriveCore drive, double x, double y, double r) {
        super(opMode, time);
        this.drive = drive;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void run() {
        drive.setSpeedXYR(x, y, r);
        drive.update();
    }

    @Override
    public void onFinish() {
        drive.stop();
    }
}
