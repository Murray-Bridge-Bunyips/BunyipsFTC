package org.firstinspires.ftc.teamcode.proto.config;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.BaseTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

public class ProtoSignalParkTask extends BaseTask implements Task {

    private CameraOp cam;
    private ProtoDrive drive;

    public ProtoSignalParkTask(BunyipsOpMode opMode, double time, CameraOp cam, ProtoDrive drive) {
        super(opMode, time);
        this.cam = cam;
        this.drive = drive;
    }

    @Override
    public void run() {

    }
}
