package org.firstinspires.ftc.teamcode.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Controller;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

/**
 * RoadRunner Mecanum Drive test for TeleOp
 */
@TeleOp(name = "GLaDOS: RoadRunner Mecanum Drive", group = "GLADOS")
//@Disabled
public class GLaDOSRRMecanum extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
    }

    @Override
    protected void activeLoop() {
        drive.setWeightedDrivePower(Controller.makeRobotPose(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x));
        drive.update();
    }

    @Override
    protected void onStop() {
        drive.teardown();
    }
}