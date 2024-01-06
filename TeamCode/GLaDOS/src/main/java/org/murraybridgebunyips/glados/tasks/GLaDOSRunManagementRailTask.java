package org.murraybridgebunyips.glados.tasks;

import androidx.annotation.NonNull;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.personalitycore.submodules.PersonalityCoreManagementRail;
import org.murraybridgebunyips.bunyipslib.tasks.bases.BunyipsTask;

public class GLaDOSRunManagementRailTask extends BunyipsTask {
    private final PersonalityCoreManagementRail managementRail;
    private final double power;

    public GLaDOSRunManagementRailTask(@NonNull BunyipsOpMode opMode, double time, PersonalityCoreManagementRail managementRail, double power) {
        super(opMode, time);
        this.managementRail = managementRail;
        if (managementRail == null) finishNow();
        this.power = power;
    }

    @Override
    public void init() {
        managementRail.setPower(power);
    }

    @Override
    public void run() {
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
