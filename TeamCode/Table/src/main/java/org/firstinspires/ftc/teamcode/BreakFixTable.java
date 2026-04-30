package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.A;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.B;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;

/**
 * Note that this OpMode assumes that the table starts fixed.
 */
@Config
@TeleOp(name = "Break/Fix Table")
public class BreakFixTable extends BunyipsOpMode {
    private final Table robot = new Table();

    protected void onInit() {
        robot.init();

        //Breaks the table
        gamepad1.button(A)
                .onTrue(robot.lineActuator.tasks.goTo(-robot.lineDistance));

        //Fixes the table
        gamepad1.button(B)
                .onTrue(robot.lineActuator.tasks.goTo(0));
    }

    protected void onStart() {
        robot.hw.line.resetEncoder();
    }

    protected void activeLoop() {
        Scheduler.update();
    }
}
