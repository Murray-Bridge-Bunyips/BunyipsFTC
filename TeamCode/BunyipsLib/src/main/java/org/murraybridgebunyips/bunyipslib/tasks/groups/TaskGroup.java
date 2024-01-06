package org.murraybridgebunyips.bunyipslib.tasks.groups;

import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

import java.util.ArrayDeque;
import java.util.Arrays;

public abstract class TaskGroup extends Task {
    protected final ArrayDeque<Task> tasks = new ArrayDeque<>();

    protected TaskGroup(Task... tasks) {
        super(0);
        this.tasks.addAll(Arrays.asList(tasks));
    }

    @Override
    public final void init() {
        // noop
    }

    @Override
    public final void onFinish() {
        // noop
    }
}
