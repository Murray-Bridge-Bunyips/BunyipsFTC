package org.firstinspires.ftc.teamcode.glados.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Controller;
import org.firstinspires.ftc.teamcode.common.DualDeadwheelMecanumDrive;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

/**
 * RoadRunner Mecanum Drive test for TeleOp
 */
@TeleOp(name = "GLaDOS: RoadRunner Mecanum Drive", group = "GLaDOS")
//@Disabled
public class GLaDOSRRMecanum extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        drive = new DualDeadwheelMecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br, config.localizerCoefficients, config.parallelEncoder, config.perpendicularEncoder);
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