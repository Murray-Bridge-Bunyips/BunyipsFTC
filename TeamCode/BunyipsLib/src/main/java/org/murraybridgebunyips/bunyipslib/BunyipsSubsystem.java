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
        // Lockout if a task is currently running that is not the default task
        if (currentTask != defaultTask) {
            Dbg.warn("Attempted to set a task while another task was running in %, this was ignored.", this.getClass().getCanonicalName());
            return;
        }
        this.currentTask = currentTask;
    }

    public final void setHighPriorityCurrentTask(Task currentTask) {
        // Task will be cancelled abruptly, run the finish callback now
        if (currentTask != defaultTask) {
            Dbg.warn("A high-priority task has forcefully interrupted the current task in %.", this.getClass().getCanonicalName());
            currentTask.forceFinish();
        }
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
