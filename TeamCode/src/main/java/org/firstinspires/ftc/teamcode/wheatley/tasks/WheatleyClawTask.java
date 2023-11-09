package org.firstinspires.ftc.teamcode.wheatley.tasks;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyLift;

/**
 * Claw task for Autonomous
 *
 * @author Lachlan Paul, 2023
 */
public class WheatleyClawTask extends Task {
    private final WheatleyLift lift;
    public WheatleyClawTask(@NonNull BunyipsOpMode opMode, double time, WheatleyLift lift) {
        super(opMode, time);
        this.lift = lift;
    }

    @Override
    public void init() {
        // noop
    }

    @Override
    public void run() {
//        lift.armLift();
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
