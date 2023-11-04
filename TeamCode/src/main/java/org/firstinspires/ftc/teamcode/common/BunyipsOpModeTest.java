package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Universal OpMode to test the BunyipsOpMode functionality
 */
@TeleOp(name = "BunyipsOpMode Test")
@Disabled
public class BunyipsOpModeTest extends BunyipsOpMode {
    @Override
    protected void onInit() {
        addTelemetry("======= BunyipsOpMode =======", true);
    }

    @Override
    protected void activeLoop() {
        assert getMovingAverageTimer() != null;
        telemetry.addLine(getMovingAverageTimer().toString());
    }
}
