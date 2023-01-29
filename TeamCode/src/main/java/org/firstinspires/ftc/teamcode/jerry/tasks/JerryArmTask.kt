package org.firstinspires.ftc.teamcode.jerry.tasks;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm;

public class JerryArmTask extends Task implements TaskImpl {

    private final JerryArm arm;
    private final int targetposition;
    private final double speed;

    public JerryArmTask(BunyipsOpMode opMode, double time, JerryArm arm, double speed, int targetposition) {
        super(opMode, time);
        this.arm = arm;
        this.speed = speed;
        this.targetposition = targetposition;
    }

    @Override
    public void init() {
        super.init();
        arm.liftSetPower(speed);
        arm.liftSetPosition(targetposition);
    }

    @Override
    public void run() {
        arm.update();
    }
}
