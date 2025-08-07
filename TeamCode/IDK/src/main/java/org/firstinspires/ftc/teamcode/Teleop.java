package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;

@TeleOp
public class Teleop extends BunyipsOpMode {
    private final IDK robot = new IDK();

    @Override
    protected void onInit() {
        robot.init();
    }

    @Override
    protected void activeLoop() {
        double forward = gamepad1.left_stick_y;
        double rotateCcw = gamepad1.right_stick_x;
        robot.drive.setPower(Controls.vel(0, forward, rotateCcw));
        robot.drive.update();


        if (gamepad2.dpad_left) {
            robot.claw.open();
        }
        if (gamepad2.dpad_right) {
            robot.claw.close();
        }
        robot.claw.update();


        robot.lift.setPower(-gamepad2.right_stick_y / 2);
        robot.lift.update();


        robot.Rotator.setPosition(robot.Rotator.getTarget() + (gamepad2.left_stick_y / 3) * timer.deltaTime().in(Seconds));
        robot.Rotator.update();
    }
}