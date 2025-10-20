package org.firstinspires.ftc.teamcode;

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
        gamepad1().button(Y)
                .whileTrue(robot.shooter.tasks.run(1));
        gamepad1().button(B).and(() -> robot.hw.shooter.getRunUsingEncoderController().pidf().get().atSetpoint())
                .whileTrue(robot.transferWheel.tasks.run(1));

        gamepad1().button(RIGHT_BUMPER)
                .whileTrue(new AlignToPointDriveTask(() -> new Vector2d(0,0), gamepad1, robot.drive));
    }

    @Override
    protected void activeLoop() {
        Scheduler.update();
    }
}