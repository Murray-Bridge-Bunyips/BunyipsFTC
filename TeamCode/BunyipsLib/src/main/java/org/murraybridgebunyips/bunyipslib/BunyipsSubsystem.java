package org.murraybridgebunyips.bunyipslib;

import androidx.annotation.NonNull;

import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

public abstract class BunyipsSubsystem extends BunyipsComponent {
    private Task currentTask;
    private Task defaultTask;

    protected BunyipsSubsystem(@NonNull BunyipsOpMode opMode) {
        super(opMode);
    }

    public Task getCurrentTask() {
        if (currentTask == null || currentTask.isFinished()) {
            currentTask = defaultTask;
        }
        return currentTask;
    }

    public final void setDefaultTask(Task defaultTask) {
        this.defaultTask = defaultTask;
    }

    public final void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public final void run() {
        Task task = getCurrentTask();
        if (task != null) {
            if (task == defaultTask && defaultTask.isFinished()) {
                throw new EmergencyStop("Default task should never finish!");
            }
            task.run();
        }
        update();
    }

    public abstract void update();
}
