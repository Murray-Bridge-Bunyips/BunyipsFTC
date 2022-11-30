package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Abstract implementation class to use custom ArrayDeque tasks in an activeLoop, simulating
 * multiple different looping operations in a linear sense.
 */
public abstract class Task implements TaskImpl {

    public static final double NANOS_IN_SECONDS = 1000000000.0;
    protected double time;
    protected volatile boolean isFinished = false;
    protected double startTime = 0;
    protected BunyipsOpMode opMode;

    public Task(BunyipsOpMode opMode, double time) {
        this.time = time;
        this.opMode = opMode;
    }

    @Override
    public void init() {}

    @Override
    public boolean isFinished() {
        if (isFinished) {
            return isFinished;
        }

        if (startTime == 0) {
            init();
            startTime = getCurrentTime();
        }
        if (getCurrentTime() > (startTime + time) || opMode.isStopRequested()) {
            isFinished = true;
        }
        return isFinished;
    }

    private double getCurrentTime() {
        return System.nanoTime() / NANOS_IN_SECONDS;
    }

}
