package org.murraybridgebunyips.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.FieldCentricMecanumDrive;
import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * FC drivetrain only for GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "Drivetrain Field-Centric")
public class GLaDOSDrivetrainFC extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;

    @Override
    protected void onInit() {
        config.init(this);
        drive = new FieldCentricMecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight);
    }

    @Override
    protected void activeLoop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;
        drive.setSpeedUsingController(x, y, r);
        drive.update();
        if (gamepad1.y) {
            drive.resetYaw();
        }
    }
}

