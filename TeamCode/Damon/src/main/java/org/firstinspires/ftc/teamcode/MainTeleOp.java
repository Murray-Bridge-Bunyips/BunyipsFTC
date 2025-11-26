package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;

/**
 * Primary iterative TeleOp.
 * Bootstrapped through bunyipslib-for-rookies.
 *
 * @author Lucas Sacco, 2025
 */
@Config
@Disabled
@TeleOp(name = "TeleOp")
public class MainTeleOp extends BunyipsOpMode {
    private final Damon robot = new Damon();
    public static double Power = 1;
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

        if(gamepad1.x) {
            robot.intake.setPower(1);
        } else if(gamepad1.a) {
            robot.intake.setPower(-1);
        } else {
            robot.intake.setPower(0);
        }

        if(gamepad1.y) {
            robot.shooter.setPower(Power);
        } else {
            robot.shooter.setPower(0);
        }

        if(gamepad1.b && robot.hw.shooter.getRunUsingEncoderController().pidf().get().atSetpoint()) {
            robot.transferWheel.setPower(1);
        } else {
            robot.transferWheel.setPower(0);
        }


        BunyipsSubsystem.updateAll();
    }
}
