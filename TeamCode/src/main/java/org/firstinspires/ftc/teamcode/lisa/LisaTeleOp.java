package org.firstinspires.ftc.teamcode.lisa;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.lisa.config.LisaConfiguration;
import org.firstinspires.ftc.teamcode.lisa.config.LisaDrive;

@TeleOp(name="<LISA> TeleOp")
public class LisaTeleOp extends BunyipsOpMode {
    private LisaConfiguration config;
    private LisaDrive drive = null;

    @Override
    protected void onInit() {
        config = LisaConfiguration.newConfig(hardwareMap, telemetry);
        try {
            drive = new LisaDrive(this,
                    config.left, config.right, config.fws, config.dws);
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
