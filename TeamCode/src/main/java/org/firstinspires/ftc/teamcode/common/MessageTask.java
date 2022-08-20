package org.firstinspires.ftc.teamcode.common;

import android.annotation.SuppressLint;

public class MessageTask extends BaseTask implements Task {

    private String message;

    public MessageTask(BunyipsOpMode opMode, double time, String message) {
        super(opMode, time);
        this.message = message;

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        if (isFinished()) {
            return;
        }
        opMode.telemetry.addLine(String.format("%s %.2f", message, time));
    }

}
