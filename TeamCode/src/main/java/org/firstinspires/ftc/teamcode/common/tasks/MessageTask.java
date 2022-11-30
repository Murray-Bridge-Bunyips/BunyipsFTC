package org.firstinspires.ftc.teamcode.common.tasks;

import android.annotation.SuppressLint;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class MessageTask extends Task implements TaskImpl {

    private final String message;

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
        opMode.telemetry.addLine(String.format("%s || %.2f", message, time));
    }

}
