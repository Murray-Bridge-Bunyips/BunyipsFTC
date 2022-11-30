package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

/**
 * Abstract implementation class to use custom ArrayDeque tasks in an
 * activeLoop, simulating multiple different looping operations in a linear sense.
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

    // Overloading base Task to allow any tasks that may not want to use a time restriction,
    // such as an init-loop task or some other always-active task. This will perform the same
    // as a task also defined with a time of 0 seconds.
    public Task(BunyipsOpMode opMode) {
        this.opMode = opMode;
        this.time = 0;
    }

    /**
     * Define code to run once, upon when the task is started.
     */
    @Override
    public void init() {}

    /**
     * Override to this method to add custom criteria if a task should be considered finished.
     * Recommended to override with a super call even if not using a time restriction.
     * @return bool expression indicating whether the task is finished or not
     */
    @Override
    public boolean isFinished() {
        if (isFinished) {
            return isFinished;
        }

        if (startTime == 0) {
            init();
            startTime = getCurrentTime();
        }

        if ((getCurrentTime() > (startTime + time) && time != 0) || opMode.isStopRequested()) {
            isFinished = true;
        }
        return isFinished;
    }

    private double getCurrentTime() {
        return System.nanoTime() / NANOS_IN_SECONDS;
    }

}
