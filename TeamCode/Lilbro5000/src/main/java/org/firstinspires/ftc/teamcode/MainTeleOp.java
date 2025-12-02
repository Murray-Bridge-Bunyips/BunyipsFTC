package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;

/**
 * Primary TeleOp.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Ziya, 2025
 */
@TeleOp(name = "TeleOp")
public class MainTeleOp extends BunyipsOpMode {
    private final Lilbro5000 robot = new Lilbro5000();
    private boolean toggle;

    @Override
    protected void onInit() {
        robot.init();
    }

    @Override
    protected void activeLoop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double rotation = -gamepad1.right_stick_x;
        robot.drive.setPower(Geometry.vel(forward, strafe, rotation));

        if (gamepad2.b) {
            robot.intake.setPower(-1);
        } else {
            robot.intake.setPower(gamepad2.left_trigger);
        }

        if (gamepad2.left_bumper) {
            robot.transfer.setPower(1);
        } else if (gamepad2.a) {
            robot.transfer.setPower(-1);
        } else {
            robot.transfer.setPower(0);
        }

        if (gamepad2.rightBumperWasPressed()) {
            toggle = !toggle;
        }
        if (gamepad2.right_trigger == 1) {
            robot.outtake.setPower(1);
        } else if (toggle) {
            robot.outtake.setPower(0.5);
        } else {
            robot.outtake.setPower(0);
        }

        if (gamepad2.x) {
            robot.middletake.setPower(1);
        } else if (gamepad2.y) {
            robot.middletake.setPower(-1);
        } else {
            robot.middletake.setPower(0);
//        }
//        if (gamepad2.left_bumper) {
//            robot.intake.setPower(1);
//            robot.middletake.setPower(-1);
//        } else {
//            robot.intake.setPower(0);
//            robot.middletake.setPower(0);
//        }
//
//        if (gamepad2.right_bumper) {
//            robot.outtake.setPower(1);
//            robot.transfer.setPower(1);
//        } else {
//            robot.outtake.setPower(0);
//            robot.transfer.setPower(0);
        }

        BunyipsSubsystem.updateAll();
    }
}