package org.firstinspires.ftc.teamcode.common;

public abstract class BaseTask implements Task {

    /**
     * the number of nanoseconds in a second
     */
    public static final double NANOS_IN_SECONDS = 1000000000.0;
    protected double time;
    protected volatile boolean isFinished = false;
    protected double startTime = 0;
    protected BunyipsOpMode opMode;


    public BaseTask(BunyipsOpMode opMode, double time) {

        this.time = time;
        this.opMode = opMode;

    }

    @Override
    public void init() {

    }

    @Override
    public boolean isFinished() {
        if (isFinished) {
            return isFinished;
        }

        if (startTime == 0) {
            init();
            startTime = getCurrentTime();
        }
        if (getCurrentTime() > (startTime + time)) {
            isFinished = true;
        }
        return isFinished;
    }


    private double getCurrentTime() {
        return System.nanoTime() / NANOS_IN_SECONDS;
    }

}
