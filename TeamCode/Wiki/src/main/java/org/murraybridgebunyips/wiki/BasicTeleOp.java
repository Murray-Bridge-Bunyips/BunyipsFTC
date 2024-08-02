package org.murraybridgebunyips.wheatley.components;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controller;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.drive.CartesianMecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;

/**
 * A basic TeleOp that doesn't use command based functions.
 */
public class BasicTeleOp extends BunyipsOpMode {
    private final ExampleConfig config = new ExampleConfig();
    private CartesianMecanumDrive drive;
    private HoldableActuator arm;
    private DualServos claws;
    @Override
    protected void onInit() {
        // For this example, we use CartesianMecanumDrive, however, if you have mecanum coefficients, imu, and drive constants
        // configured, you should use MecanumDrive().
        drive = new CartesianMecanumDrive(config.front_left, config.front_right, config.back_left, config.back_right);
        arm = new HoldableActuator(config.arm);

        // The open and closed arguments refer to what positions between 0 and 1 (as set on the servo programmer)
        // should be considered as fully open and fully closed. You can change this if needed,
        // but most use cases will just need them to be 0.0 and 1.0.
        claws = new DualServos(config.left_claw, config.right_claw, 0.0, 1.0, 0.0, 1.0);
    }

    @Override
    protected void activeLoop() {
        // This sets the left stick to be used as the movement, and the right stick X-axis for rotation.
        // Robots with this configuration control with Tank Controls. Up is always forward, left is always left, etc.
        // If you've ever played a classic Resident Evil, it's just like that. You have become the fixed camera angle.
        drive.setSpeedUsingController(gamepad1.lsx, gamepad1.lsy, gamepad1.rsx);
        arm.setPower(-gamepad2.lsy);  // Sets the arm to be controlled with the second gamepad's left stick, reversed.

        // The special getDebounced() function gets a button press, but only once per press.
        // Simply getting gamepad2.x just checks to see if it's pressed, and since the loop runs many times a second,
        // one press can be registered as many. This can be useful, but probably not what you want for claw controls.
        if (gamepad2.getDebounced(Controls.X)) {
            claws.toggle(DualServos.ServoSide.LEFT);
        } else if (gamepad2.getDebounced(Controls.B)) {
            claws.toggle(DualServos.ServoSide.RIGHT);
        }

        // These send stateful updates to the hardware and are REQUIRED.
        drive.update();
        arm.update();
    }
}
