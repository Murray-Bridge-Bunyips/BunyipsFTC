package org.firstinspires.ftc.teamcode.common;

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
