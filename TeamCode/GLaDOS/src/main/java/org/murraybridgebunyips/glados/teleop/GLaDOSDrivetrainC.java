package org.murraybridgebunyips.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.Scheduler;
import org.murraybridgebunyips.bunyipslib.tasks.CallbackTask;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * POV drivetrain only for GLaDOS.
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "Drivetrain")
@Disabled
public class GLaDOSDrivetrainC extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private final Scheduler scheduler = new Scheduler();
    private MecanumDrive drive;

    @Override
    protected void onInit() {
        config.init(this);
        drive = new DualDeadwheelMecanumDrive(
                this, config.driveConstants, config.mecanumCoefficients,
                hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight,
                config.backLeft, config.backRight, config.localizerCoefficients,
                config.parallelEncoder, config.perpendicularEncoder
        );
        setCommands();
    }

    private void setCommands() {
        drive.setDefaultTask(new CallbackTask(() -> drive.update()));
    }

    @Override
    protected void activeLoop() {
        scheduler.run();
//        double x = gamepad1.left_stick_x;
//        double y = gamepad1.left_stick_y;
//        double r = gamepad1.right_stick_x;
//        drive.setSpeedUsingController(x, y, r);
//        drive.update();
    }
}

