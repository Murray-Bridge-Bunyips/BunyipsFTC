package org.firstinspires.ftc.teamcode.common.tasks;

public interface Task {

    void init();

    /**
     * Define code to run while the task is not finished.
     */
    void run();

    boolean isFinished();

}
