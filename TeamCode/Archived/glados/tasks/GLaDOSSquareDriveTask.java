package org.firstinspires.ftc.teamcode.glados.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RelativePose2d;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSPOVDriveCore;

/**
 * Utilise a modified time control to drive GLaDOS based on Field Squares.
 *
 * @author Lucas Bubner, 2023
 * @deprecated Migration to RoadRunner
 */
@Deprecated
public class GLaDOSSquareDriveTask extends Task {
    /**
     * Seconds to travel one square along X or Y axis at 1.0 power.
     */
    protected final double SECONDS_TO_TRAVEL_ONE_SQUARE_AT_FULL_POWER = 1.0;
    protected final double SECONDS_TO_TRAVEL_ONE_SQUARE_AT_FULL_POWER_DIAGONALLY = 1.2;
    private final GLaDOSPOVDriveCore drive;
    private final int squares;
    private final RelativeVector direction;

    public GLaDOSSquareDriveTask(@NonNull BunyipsOpMode opMode, double time, GLaDOSPOVDriveCore drive, int squares, double power, RelativeVector direction) {
        super(opMode, time);
        this.drive = drive;
        this.squares = squares;
        this.direction = direction;
        if (direction == RelativeVector.ANTICLOCKWISE || direction == RelativeVector.CLOCKWISE) {
            throw new IllegalArgumentException("GSDT: Cannot drive in a square in a rotational direction");
        }
        direction.getVector().normalise(power);
    }

    @Override
    public boolean isTaskFinished() {
        return direction.getVector().isDiagonal() ? getDeltaTime() >= squares * SECONDS_TO_TRAVEL_ONE_SQUARE_AT_FULL_POWER_DIAGONALLY : getDeltaTime() >= squares * SECONDS_TO_TRAVEL_ONE_SQUARE_AT_FULL_POWER;
    }

    @Override
    public void init() {
        // noop
    }

    @Override
    public void run() {
        drive.setVector(direction);
        drive.update();
    }

    @Override
    public void onFinish() {
        drive.stop();
    }
}
