package org.murraybridgebunyips.bunyipslib.tasks;

/**
 * A task that runs once and then immediately completes.
 */
public abstract class RunOnceTask extends Task {
    @Override
    public final void init() {
        runOnce();
    }

    @Override
    public final void run() {
    }

    @Override
    public final boolean isTaskFinished() {
        return true;
    }

    @Override
    public final void onFinish() {
    }

    public abstract void runOnce();
}
