package org.murraybridgebunyips.joker.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.joker.Joker;

@TeleOp
public class DriveBase extends BunyipsOpMode {
    private final Joker robot = new Joker();
    private CartesianMecanumDrive drive;
    @Override
    protected void onInit() {
        robot.init();
        drive = new CartesianMecanumDrive(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight);

    }

    @Override
    protected void activeLoop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;
        drive.setSpeedUsingController(leftStickX, leftStickY, rightStickX);
        drive.update();
    }
}
