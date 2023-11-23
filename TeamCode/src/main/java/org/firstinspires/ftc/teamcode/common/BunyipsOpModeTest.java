package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Universal OpMode to test the BunyipsOpMode functionality
 */
@TeleOp(name = "BunyipsOpMode Test")
//@Disabled
public class BunyipsOpModeTest extends BunyipsOpMode {
    @Override
    protected void onInit() {
        addRetainedTelemetry("======= BunyipsOpMode =======");
    }

    @Override
    protected void activeLoop() {
        addTelemetry(getMovingAverageTimer().toString());
        addTelemetry(Controller.makeRobotPose(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x).toString());
    }
}
