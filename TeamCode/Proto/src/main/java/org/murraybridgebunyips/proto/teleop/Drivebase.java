package org.murraybridgebunyips.proto.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicVectorDriveTask;
import org.murraybridgebunyips.proto.Proto;

/**
 * Drivebase and localizer only TeleOp with auto-lock.
 */
@TeleOp(name = "Drivebase Control and Localizer Auto-Lock")
public class Drivebase extends CommandBasedBunyipsOpMode {
    private final Proto robot = new Proto();
    private DualDeadwheelMecanumDrive drive;

    @Override
    protected void onInitialise() {
        robot.init();
        drive = new DualDeadwheelMecanumDrive(
                robot.driveConstants, robot.mecanumCoefficients, robot.imu,
                robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight,
                robot.localizerCoefficients, robot.parallelDeadwheel, robot.perpendicularDeadwheel
        );
    }

    @Override
    protected void assignCommands() {
//        drive.setDefaultTask(new HolonomicDriveTask(gamepad1, drive, () -> false));
        drive.setDefaultTask(new HolonomicVectorDriveTask(gamepad1, drive, () -> false)
                .withTranslationalPID(0.1, 0, 0)
                .withRotationalPID(1, 0, 0.0001));
    }
}
