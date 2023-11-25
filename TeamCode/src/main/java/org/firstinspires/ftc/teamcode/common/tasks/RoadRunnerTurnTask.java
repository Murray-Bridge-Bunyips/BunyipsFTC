package org.firstinspires.ftc.teamcode.common.tasks;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;

/**
 * Task for running RoadRunner trajectories using the BunyipsOpMode Task system
 *
 * @author Lucas Bubner, 2023
 */
public class RoadRunnerTurnTask extends Task {
    private final MecanumDrive drive;

    private final double angle;

    public RoadRunnerTurnTask(@NonNull BunyipsOpMode opMode, double time, MecanumDrive drive, double angle) {
        super(opMode, time);
        this.drive = drive;
        this.angle = angle;
    }

    @Override
    public void init() {
        drive.turnAsync(Math.toRadians(angle));
    }

    @Override
    public void run() {
        drive.update();
    }

    @Override
    public void onFinish() {
        drive.setDrivePower(new Pose2d(0, 0, 0));
    }

    @Override
    public boolean isTaskFinished() {
        return !drive.isBusy();
    }
}
