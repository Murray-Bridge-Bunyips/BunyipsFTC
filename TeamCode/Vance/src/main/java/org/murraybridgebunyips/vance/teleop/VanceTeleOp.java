package org.murraybridgebunyips.vance.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.TriDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.HolonomicVectorDriveTask;
import org.murraybridgebunyips.vance.Vance;

/**
 * TeleOp for Vance
 * <p></p>
 * <b>gamepad1:</b><br>
 * Left Stick X: Strafe<br>
 * Left Stick Y: Forward/Back<br>
 * <p></p>
 * <b>gamepad2:</b><br>
 * Left Stick Y: Vertical Arm<br>
 * Right Stick Y: Horizontal Arm<br>
 * X: Toggle Left Claw<br>
 * B: Toggle Right Claw<br>
 * Y: Open Both Claws<br>
 * A: Close Both Claws<br>
 *
 * @author Lachlan Paul, 2024
 */
@TeleOp
public class VanceTeleOp extends CommandBasedBunyipsOpMode {
    private final Vance robot = new Vance();
    private MecanumDrive drive;
    private HoldableActuator verticalArm;
    private HoldableActuator horizontalArm;
    private DualServos claws;

    //    private BlinkinLights lights;

    @Override
    protected void onInitialise() {
        robot.init();
        drive = new TriDeadwheelMecanumDrive(
                robot.driveConstants, robot.mecanumCoefficients, robot.imu, robot.fl, robot.fr, robot.bl, robot.br,
                robot.localiserCoefficients, robot.dwleft, robot.dwright, robot.dwx).withName("Drive");
        verticalArm = new HoldableActuator(robot.verticalArm);
        horizontalArm = new HoldableActuator(robot.horizontalArm);
        // TODO: check open/close values
        claws = new DualServos(robot.leftClaw, robot.rightClaw, 0.0, 1.0, 0.0, 1.0);
//        lights = new BlinkinLights(config.lights, RevBlinkinLedDriver.BlinkinPattern.RAINBOW_FOREST_PALETTE);

        gamepad1.set(Controls.AnalogGroup.STICKS, Controller.SQUARE);
    }

    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.X)
                        .run(claws.tasks.toggleLeft());
        operator().whenPressed(Controls.B)
                        .run(claws.tasks.toggleRight());
        operator().whenPressed(Controls.Y)
                        .run(claws.tasks.openBoth());
        operator().whenPressed(Controls.A)
                        .run(claws.tasks.closeBoth());

        verticalArm.setDefaultTask(verticalArm.tasks.control(() -> gamepad2.lsy));
        horizontalArm.setDefaultTask(verticalArm.tasks.control(() -> gamepad2.rsy));
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
        // Some drivers have noted that they sometimes cannot tell whether a claw is open or closed.
        // Hopefully this helps. Update: It did :)
        // The actual string is set to the opposite of what you might expect, by driver request.
        telemetry.add("\n---------");
        telemetry.add("Left Claw: " + (claws.isOpen(DualServos.ServoSide.LEFT) ? "Closed" : "Open")).big();
        telemetry.add("Right Claw: " + (claws.isOpen(DualServos.ServoSide.RIGHT) ? "Closed" : "Open")).big();
        telemetry.add("---------\n");
//
//        telemetry.add("Top Switch Is %", config.topLimit.isPressed() ? "Pressed" : "Not Pressed");
//        telemetry.add("Bottom Switch Is %", config.bottomLimit.isPressed() ? "Pressed" : "Not Pressed");
    }
}
