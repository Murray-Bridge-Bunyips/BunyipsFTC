package org.firstinspires.ftc.teamcode.teleop;


// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;

@Config
@TeleOp(name = "TeleOp")
public class TeleOperated extends BunyipsOpMode {
    private final Jonas robot = new Jonas();

    @Override
    protected void onInit() {
        robot.init();

        robot.drive.setDefaultTask(new HolonomicDriveTask(gamepad1, robot.drive));

        robot.intake.setDefaultTask(robot.intake.tasks.control(() -> -gamepad2.left_stick_y));
        robot.output.setDefaultTask(robot.output.tasks.control(() -> -gamepad2.right_stick_y));

        gamepad2.button(LEFT_BUMPER)
                .onTrue(robot.preventer.tasks.toggle());
        
        gamepad2.button(A)
                .onTrue(robot.launch);

        gamepad2.button(Y)
                .onTrue(robot.launchStaggered);
    }

    @Override
    protected void onStart() {
        robot.preventer.close();
    }

    @Override
    protected void activeLoop() {
        Scheduler.update();
    }
}