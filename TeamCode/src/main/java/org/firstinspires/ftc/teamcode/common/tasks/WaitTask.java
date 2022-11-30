package org.firstinspires.ftc.teamcode.common.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class WaitTask extends Task implements TaskImpl {
    public WaitTask(BunyipsOpMode opMode, double time) {
        super(opMode, time);

    }

    void update() {}

    @Override
    public void run() {
        if (isFinished()) {
            return;
        }
    }
}
