package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.DualServos;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.HoldableActuator;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.subsystems.drive.SimpleMecanumDrive;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.AlignToPointDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;

/**
 * Basic Command-based TeleOp.
 * This is modelled after the basic robot from the robot configuration page.
 */
@Config
@TeleOp(name = "TeleOp")
public class DamonTeleOp extends CommandBasedBunyipsOpMode {
    private final Damon robot = new Damon();
    @Override
    protected void onInitialise() {
        robot.init();
    }

    @Override
    protected void assignCommands() {
        new HolonomicDriveTask(gamepad1, robot.drive).setAsDefaultTask();
        robot.intake.tasks.control(() -> gamepad1.x ? 1 : gamepad1.a ? -1 : 0).setAsDefaultTask();
        robot.shooter.tasks.control(() -> gamepad1.y ? 1 : 0).setAsDefaultTask();
        robot.transferWheel.tasks.control(() -> gamepad1.b && robot.hw.shooter.getRunUsingEncoderController().pidf().get().atSetpoint() ? 1 : 0).setAsDefaultTask();

        driver().whenPressed(Controls.RIGHT_BUMPER)
                .run(new AlignToPointDriveTask(() -> new Vector2d(0,0), gamepad1, robot.drive))
                .finishIf(() -> !gamepad1.right_bumper);
    }
}