package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;

@TeleOp
public class teleop extends BunyipsOpMode {

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
    }
}
//he's gone