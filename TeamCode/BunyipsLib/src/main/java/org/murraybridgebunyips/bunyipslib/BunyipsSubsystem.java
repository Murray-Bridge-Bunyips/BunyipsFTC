package org.murraybridgebunyips.bunyipslib;

import androidx.annotation.NonNull;

import org.murraybridgebunyips.bunyipslib.tasks.Task;

public abstract class BunyipsSubsystem extends BunyipsComponent {
    private Task currentTask;
    private Task defaultTask;

    protected BunyipsSubsystem(@NonNull BunyipsOpMode opMode) {
        super(opMode);
    }

    public void setDefaultTask(Task defaultTask) {
        this.defaultTask = defaultTask;
    }

    public Task getCurrentTask() {
        if (currentTask == null) {
            currentTask = defaultTask;
        }
        return currentTask;
    }

    public abstract void update();
}
