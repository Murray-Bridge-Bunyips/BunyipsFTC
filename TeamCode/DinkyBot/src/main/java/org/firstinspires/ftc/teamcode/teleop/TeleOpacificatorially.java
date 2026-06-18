package org.firstinspires.ftc.teamcode.teleop;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.A;

import org.firstinspires.ftc.teamcode.DinkyBot;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;

public class TeleOpacificatorially extends BunyipsOpMode {
    private final DinkyBot robot = new DinkyBot();

    @Override
    protected void onInit() {
        robot.init();

        robot.drive.setDefaultTask(new HolonomicDriveTask(gamepad1, robot.drive));

        robot.flywheel.setDefaultTask(robot.flywheel.tasks.control(() -> -gamepad2.right_stick_y));

        gamepad2.button(A)
                .onTrue(robot.pusher.tasks.toggle());
    }

    @Override
    protected void activeLoop() {

    }
}
