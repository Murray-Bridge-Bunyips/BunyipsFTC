package org.murraybridgebunyips.wheatley.components;

import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;

/**
 * Basic Command-Based TeleOp.
 */
public class CommandBasedTeleOp extends CommandBasedBunyipsOpMode {
    private final ExampleConfig config = new ExampleConfig();
    private CartesianMecanumDrive drive;
    private HoldableActuator arm;
    private DualServos claws;

    @Override
    protected void onInitialise() {
        // For this example, we use CartesianMecanumDrive, however, if you have mecanum coefficients, imu, and drive constants
        // configured, you should use MecanumDrive().
        drive = new CartesianMecanumDrive(config.front_left, config.front_right, config.back_left, config.back_right);
        arm = new HoldableActuator(config.arm);

        // The open and closed arguments refer to what positions between 0 and 1 (as set on the servo programmer)
        // should be considered as fully open and fully closed. You can change this if needed,
        // but most use cases will just need them to be 0.0 and 1.0.
        claws = new DualServos(config.left_claw, config.right_claw, 0.0, 1.0, 0.0, 1.0);

        // This sets gamepad1 and gamepad2 as driver and operator respectively. REQUIRED for Command-Based TeleOp.
        gamepad1.set(Controls.AnalogGroup.STICKS, Controller.SQUARE);
    }

    @Override
    protected void assignCommands() {
        // Command-Based TeleOp uses tasks. Most functions, unless they're explicitly tasks, have task and non task variants,
        // allowing for use in both kinds of TeleOp.
        operator().whenPressed(Controls.X)
                .run(claws.toggleTask(DualServos.ServoSide.LEFT));
        operator().whenPressed(Controls.B)
                .run(claws.toggleTask(DualServos.ServoSide.RIGHT));

        arm.setDefaultTask(linearActuator.controlTask(() -> -gamepad2.lsy));
        drive.setDefaultTask(new HolonomicDriveTask(gamepad1, drive, () -> false));
    }
}
