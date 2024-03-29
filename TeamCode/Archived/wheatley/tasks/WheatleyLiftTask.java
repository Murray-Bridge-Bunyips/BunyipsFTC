package org.murraybridgebunyips.wheatley.tasks;

import androidx.annotation.NonNull;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.tasks.Task;
import org.murraybridgebunyips.wheatley.components.WheatleyLift;

/**
 * Lift task for Autonomous
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyLiftTask extends Task {
    private final WheatleyLift lift;
    private final int targetPosition;

    public WheatleyLiftTask(@NonNull BunyipsOpMode opMode, double time, WheatleyLift lift, int targetPosition) {
        super(opMode, time);
        this.lift = lift;
        this.targetPosition = targetPosition;
    }

    @Override
    public void init() {
        lift.setPosition(targetPosition);
    }

    @Override
    public void run() {
        getOpMode().addTelemetry("Lift running: %/%", lift.getMotor().getCurrentPosition(), targetPosition);
    }

    @Override
    public boolean isTaskFinished() {
        return lift.getMotor().isBusy();
    }

    @Override
    public void onFinish() {
        // noop
    }
}
