package org.firstinspires.ftc.teamcode.wheatley.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Cartesian;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

/**
 * Time based drive task for Wheatley
 * The more I program the more Wheatley and GlaDOS's code looks the same
 * Like this is identical to GlaDOS's counterpart file
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyTimeDriveTask extends Task {
    private final MecanumDrive drive;
    private final double x;
    private final double y;
    private final double r;


    public WheatleyTimeDriveTask(@NonNull BunyipsOpMode opMode, double time, MecanumDrive drive, double x, double y, double r) {
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
        // Time control only
        return false;
    }

    @Override
    public void onFinish() {
        drive.stop();
    }
}
