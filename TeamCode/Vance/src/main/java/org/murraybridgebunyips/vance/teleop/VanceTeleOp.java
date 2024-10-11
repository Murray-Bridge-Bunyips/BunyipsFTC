package org.murraybridgebunyips.vance.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.TriDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.subsystems.Switch;
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
 * X: Open Both Claws<br>
 * B: Close Both Claws<br>
 * A: Toggle Claw Rotator<br>
 * Y: Toggle Basket Rotator<br>
 *
 * @author Lachlan Paul, 2024
 */
@TeleOp
public class VanceTeleOp extends CommandBasedBunyipsOpMode {
    private final Vance robot = new Vance();
    private MecanumDrive drive;
    private HoldableActuator verticalArm;
    private HoldableActuator horizontalArm;
    private Switch clawRotator;
    private Switch basketRotator;
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

        clawRotator = new Switch(robot.clawRotator);
        basketRotator = new Switch(robot.basketRotator);

        // TODO: check open/close values
        claws = new DualServos(robot.leftClaw, robot.rightClaw, 0.0, 1.0, 0.0, 1.0);
//        lights = new BlinkinLights(config.lights, RevBlinkinLedDriver.BlinkinPattern.RAINBOW_FOREST_PALETTE);

        gamepad1.set(Controls.AnalogGroup.STICKS, Controller.SQUARE);
    }

    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.X)
                        .run(claws.tasks.openBoth());
        operator().whenPressed(Controls.B)
                        .run(claws.tasks.closeBoth());
        operator().whenPressed(Controls.A)
                        .run(clawRotator.tasks.toggle());
        operator().whenPressed(Controls.Y)
                        .run(basketRotator.tasks.toggle());

        verticalArm.setDefaultTask(verticalArm.tasks.control(() -> gamepad2.lsy));
        horizontalArm.setDefaultTask(horizontalArm.tasks.control(() -> gamepad2.rsy));
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
        telemetry.add("Claws: " + (claws.isOpen(DualServos.ServoSide.BOTH) ? "Closed" : "Open")).big();
        telemetry.add("Claw Rotator: " + (clawRotator.isOpen() ? "Closed" : "Open")).big();
        telemetry.add("Basket Rotator: " + (basketRotator.isOpen() ? "Closed" : "Open")).big();
        telemetry.add("---------\n");
//
//        telemetry.add("Top Switch Is %", config.topLimit.isPressed() ? "Pressed" : "Not Pressed");
//        telemetry.add("Bottom Switch Is %", config.bottomLimit.isPressed() ? "Pressed" : "Not Pressed");
    }
}
