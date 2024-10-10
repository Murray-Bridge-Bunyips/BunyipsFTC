package org.murraybridgebunyips.vance.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.TriDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicVectorDriveTask;
import org.murraybridgebunyips.vance.Vance;

/**
 * TeleOp for Vance
 *
 * @author Lachlan Paul, 2024
 */
@TeleOp
public class VanceTeleOp extends CommandBasedBunyipsOpMode {
    private final Vance robot = new Vance();
    private MecanumDrive drive;

    //    private BlinkinLights lights;

    @Override
    protected void onInitialise() {
        robot.init();
        drive = new TriDeadwheelMecanumDrive(
                robot.driveConstants, robot.mecanumCoefficients, robot.imu, robot.fl, robot.fr, robot.bl, robot.br,
                robot.localiserCoefficients, robot.dwleft, robot.dwright, robot.dwx).withName("Drive");
//        lights = new BlinkinLights(config.lights, RevBlinkinLedDriver.BlinkinPattern.RAINBOW_FOREST_PALETTE);

        gamepad1.set(Controls.AnalogGroup.STICKS, Controller.SQUARE);
    }

    @Override
    protected void assignCommands() {
        drive.setDefaultTask(new HolonomicVectorDriveTask(gamepad1, drive, () -> false)
                    .withTranslationalPID(0.1, 0, 0)
                    .withRotationalPID(1, 0, 0.0001));
    }

    @Override
    protected void periodic() {
//        // LED Management
//        if (config.bottomLimit.isPressed()) { // Make an && for the top limit when it's reinstated
//            lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
//        }
//
//        // Some drivers have noted that they sometimes cannot tell whether a claw is open or closed.
//        // Hopefully this helps. Update: It did :)
//        telemetry.add("\n---------");
//
//        // The actual string is set to the opposite of what you might expect, by driver request.
//        telemetry.add("Left Claw: " + (claws.isOpen(DualServos.ServoSide.LEFT) ? "Closed" : "Open")).big();
//        telemetry.add("Right Claw: " + (claws.isOpen(DualServos.ServoSide.RIGHT) ? "Closed" : "Open")).big();
//        telemetry.add("---------\n");
//
//        telemetry.add("Top Switch Is %", config.topLimit.isPressed() ? "Pressed" : "Not Pressed");
//        telemetry.add("Bottom Switch Is %", config.bottomLimit.isPressed() ? "Pressed" : "Not Pressed");
    }
}
