package org.murraybridgebunyips.bunyipslib;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Task scheduler for use with BunyipsLib.
 * @author Lucas Bubner, 2024
 */
public class Scheduler {
    private final ArrayList<BunyipsSubsystem> subsystems = new ArrayList<>();

    public void addSubsystems(BunyipsSubsystem... dispatch) {
        subsystems.addAll(Arrays.asList(dispatch));
    }

    public void run() {
        for (BunyipsSubsystem subsystem : subsystems) {
            subsystem.run();
        }
    }

    // TODO: Integrate logical bindings
}
