package org.firstinspires.ftc.teamcode.groot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

@TeleOp(name="<GROOT> TeleOp")
public class GrootTeleOp extends BunyipsOpMode {
    private GrootConfiguration config;
    private GrootDrive drive = null;

    @Override
    protected void onInit() {
        config = GrootConfiguration.newConfig(hardwareMap, telemetry);
        try {
            drive = new GrootDrive(this,
                    config.left, config.right);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise motors.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        double leftPower = (gamepad1.left_trigger - gamepad1.right_trigger) + gamepad1.left_stick_x;
        double rightPower = (gamepad1.left_trigger - gamepad1.right_trigger) - gamepad1.left_stick_x;

        drive.setPower(rightPower, leftPower);
        drive.update();
    }
}
