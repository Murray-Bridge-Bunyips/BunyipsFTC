package org.firstinspires.ftc.team15215.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team15215.glados.components.GLaDOSConfigCore;
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.ftc.bunyipslib.DualDeadwheelMecanumDrive;

/**
 * RoadRunner Mecanum Drive test for TeleOp
 */
@TeleOp(name = "RoadRunner Mecanum Drive")
//@Disabled
public class GLaDOSRRMecanum extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;

    @Override
    protected void onInit() {
        config.init(this);
        drive = new DualDeadwheelMecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelEncoder, config.perpendicularEncoder);
    }

    @Override
    protected void activeLoop() {
        drive.setSpeedUsingController(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        drive.update();
    }

    @Override
    protected void onStop() {
        drive.stop();
    }
}