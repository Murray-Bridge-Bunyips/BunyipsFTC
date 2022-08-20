package org.firstinspires.ftc.teamcode.lisa;

import org.firstinspires.ftc.teamcode.bertie.BertieArm;
import org.firstinspires.ftc.teamcode.bertie.BertieBunyipConfiguration;
import org.firstinspires.ftc.teamcode.bertie.BertieBunyipDrive;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

public class LisaTeleOp extends BunyipsOpMode {
    private LisaConfiguration config;
    private LisaDrive drive = null;

    @Override
    protected void onInit() {
        config = LisaConfiguration.newConfig(hardwareMap, telemetry);
        try {
            drive = new LisaDrive(this,
                    config.left, config.right);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise motors.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        double leftPower = gamepad1.right_trigger - gamepad1.left_trigger;
        double rightPower = gamepad1.right_trigger - gamepad1.left_trigger;

        drive.setPower(leftPower, rightPower);
        drive.update();
    }
}
