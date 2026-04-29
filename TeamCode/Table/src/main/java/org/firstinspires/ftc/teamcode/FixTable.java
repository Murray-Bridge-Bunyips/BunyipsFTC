package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;

/**
 * Note that this OpMode assumes that the table starts broken.
 */
@Config
@TeleOp(name = "Fix Table")
public class FixTable extends BunyipsOpMode {
    private final Table robot = new Table();

    protected void onInit() {
        robot.init();
    }

    protected void onStart() {
        robot.hw.line.resetEncoder();
    }

    protected void activeLoop() {
        robot.lineActuator.tasks.goTo(robot.lineDistance);
    }
}
