package org.firstinspires.ftc.teamcode.teleop;


// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DinkyBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.DifferentialDriveTask;

@Config
@TeleOp(name = "TeleOp")
public class TeleOpacificatorially extends BunyipsOpMode {
    private final DinkyBot robot = new DinkyBot();

    @Override
    protected void onInit() {
        robot.init();

        robot.drive.setDefaultTask(new DifferentialDriveTask(gamepad1, robot.drive));

//        robot.flywheel.setDefaultTask(robot.flywheel.tasks.control(() -> -gamepad2.right_stick_y));

        gamepad2.button(A)
                .onTrue(robot.pusher.tasks.toggle());
    }

    @Override
    protected void onStart() {
        robot.pusher.open();
    }

    @Override
    protected void activeLoop() {
        // It got bleak
         robot.flywheel.setPower(-gamepad2.left_stick_y);
         Scheduler.update();
    }
}
