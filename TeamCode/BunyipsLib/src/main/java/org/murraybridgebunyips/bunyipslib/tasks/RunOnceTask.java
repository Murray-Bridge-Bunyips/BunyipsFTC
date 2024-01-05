package org.murraybridgebunyips.bunyipslib.tasks;

/**
 * A task that runs once and then immediately completes.
 */
public abstract class RunOnceTask extends Task {
    protected RunOnceTask() {
        // Time will be ignored as this task will only run once
        super(1);
    }

    @Override
    public final void init() {
    }

    @Override
    public final void run() {
        runOnce();
        setTaskFinished(true);
    }

    @Override
    public final boolean isTaskFinished() {
        return getTaskFinished();
    }

    @Override
    public final void onFinish() {
    }

    public abstract void runOnce();
}
