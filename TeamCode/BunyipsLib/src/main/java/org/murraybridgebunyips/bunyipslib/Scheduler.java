package org.murraybridgebunyips.bunyipslib;

import org.murraybridgebunyips.bunyipslib.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;

public class Scheduler {
    private final ArrayList<BunyipsSubsystem> subsystems = new ArrayList<>();

    public void addSubsystems(BunyipsSubsystem... dispatch) {
        subsystems.addAll(Arrays.asList(dispatch));
    }

    public void run() {
        for (BunyipsSubsystem subsystem : subsystems) {
            Task currentTask = subsystem.getCurrentTask();
            if (currentTask != null) {
                currentTask.run();
            }
            subsystem.update();
        }
    }
}
