package org.firstinspires.ftc.teamcode.kanga;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.kanga.config.KangaConfiguration;
import org.firstinspires.ftc.teamcode.kanga.config.KangaDrive;

@TeleOp(name="<KANGA> TeleOp")
public class KangaTeleOp extends BunyipsOpMode {
    private KangaConfiguration config;
    private KangaDrive drive = null;

    @Override
    protected void onInit() {
        config = KangaConfiguration.newConfig(hardwareMap, telemetry);
        try {
            drive = new KangaDrive(this,
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
