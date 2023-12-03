package org.murraybridgebunyips.bunyipslib.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Cartesian;
import org.murraybridgebunyips.bunyipslib.Controller;

/**
 * Universal OpMode to test the BunyipsOpMode functionality
 */
@TeleOp(name = "COMMON: BunyipsOpMode")
@Disabled
public class BunyipsOpModeTest extends BunyipsOpMode {
    @Override
    protected void onInit() {
        addRetainedTelemetry("======= BunyipsOpMode =======");
    }

    @Override
    protected void activeLoop() {
        addTelemetry(getMovingAverageTimer().toString());
        addTelemetry(Cartesian.fromPose(Controller.makeRobotPose(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x)).toString());
        addTelemetry(Controller.makeRobotPose(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x).toString());
    }
}
