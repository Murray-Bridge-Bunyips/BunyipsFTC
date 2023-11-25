package org.firstinspires.ftc.teamcode.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Controller;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

/**
 * POV drivetrain only for GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLaDOS: Drivetrain", group = "GLaDOS")
public class GLaDOSDrivetrain extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;

    @Override
    protected void onInit() {
        config.init(this, hardwareMap);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.bl, config.fr, config.br);
    }

    @Override
    protected void activeLoop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;
        drive.setWeightedDrivePower(Controller.makeRobotPose(x, y, r / 2));
        drive.update();
    }
}

