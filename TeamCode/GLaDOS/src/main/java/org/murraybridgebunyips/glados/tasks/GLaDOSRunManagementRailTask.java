package org.murraybridgebunyips.glados.tasks;

import org.murraybridgebunyips.common.personalitycore.submodules.PersonalityCoreManagementRail;
import org.murraybridgebunyips.bunyipslib.tasks.bases.Task;

public class GLaDOSRunManagementRailTask extends Task {
    private final PersonalityCoreManagementRail managementRail;
    private final double power;

    public GLaDOSRunManagementRailTask(double time, PersonalityCoreManagementRail managementRail, double power) {
        super(time);
        this.managementRail = managementRail;
        if (managementRail == null) finishNow();
        this.power = power;
    }

    @Override
    public void init() {
        managementRail.setPower(power);
    }

    @Override
    public void periodic() {
        managementRail.update();
    }

    @Override
    public void onFinish() {
        // noop
    }

    @Override
    public boolean isTaskFinished() {
        // Relying on the Task timing control
        return false;
    }
}
