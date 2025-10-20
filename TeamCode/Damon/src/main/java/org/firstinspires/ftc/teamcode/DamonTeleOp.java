package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.task;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;

/**
 * Basic Command-based TeleOp.
 * This is modelled after the basic robot from the robot configuration page.
 */
@TeleOp(name = "TeleOp")
public class DamonTeleOp extends BunyipsOpMode {
    private final Damon robot = new Damon();

    @Override
    protected void onInit() {
        robot.init();

        new HolonomicDriveTask(gamepad1, robot.drive).setAsDefaultTask();
        gamepad1().button(X)
                .whileTrue(robot.intake.tasks.run(1));
        gamepad1().button(A)
                .whileTrue(robot.intake.tasks.run(-1));
        robot.intake.tasks.control(() -> gamepad1.x ? 1 : gamepad1.a ? -1 : 0).setAsDefaultTask();
        robot.shooter.tasks.control(() -> gamepad1.y ? 1 : 0).setAsDefaultTask();
        robot.transferWheel.tasks.control(() -> gamepad1.b && robot.hw.shooter.getRunUsingEncoderController().pidf().get().atSetpoint() ? 1 : 0).setAsDefaultTask();

        gamepad1().button(RIGHT_BUMPER)
                .whileTrue(new AlignToPointDriveTask(() -> new Vector2d(0,0), gamepad1, robot.drive));
    }

    @Override
    protected void activeLoop() {
        Scheduler.update();
    }
}