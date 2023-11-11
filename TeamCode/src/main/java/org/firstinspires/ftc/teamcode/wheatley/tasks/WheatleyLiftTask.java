package org.firstinspires.ftc.teamcode.wheatley.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyLift;

/**
 * Lift task for Autonomous
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyLiftTask extends Task {
    private final WheatleyLift lift;
    private int move;

    public WheatleyLiftTask(@NonNull BunyipsOpMode opMode, double time, WheatleyLift lift, int move) {
        super(opMode, time);
        this.lift = lift;
    }

    @Override
    public void init() {
        // noop
    }

    @Override
    public void run() {
        // TODO: Set proper values
        lift.armLift(move);
    }

    @Override
    public void onFinish() {
        // noop
    }

    @Override
    public boolean isTaskFinished() {
        // Time control
        return false;
    }
}
