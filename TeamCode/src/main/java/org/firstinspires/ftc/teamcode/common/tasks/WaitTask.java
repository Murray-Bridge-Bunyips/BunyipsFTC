package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class WaitTask extends BaseTask implements Task {


    public WaitTask(BunyipsOpMode opMode, double time) {
        super(opMode, time);

    }

    void update() {

    }

    @Override
    public void run() {
        if (isFinished()) {
            return;
        }

    }

}
