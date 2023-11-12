package org.firstinspires.ftc.teamcode.common.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public abstract class RunOnceTask extends Task {
    public RunOnceTask(@NonNull BunyipsOpMode opMode) {
        // Time will be ignored as this task will only run once
        super(opMode, 1);
    }

    @Override
    final public void init() {
    }

    @Override
    final public void run() {
        runOnce();
        setTaskFinished(true);
    }

    @Override
    final public boolean isTaskFinished() {
        return getTaskFinished();
    }

    @Override
    final public void onFinish() {
    }

    public abstract void runOnce();
}
