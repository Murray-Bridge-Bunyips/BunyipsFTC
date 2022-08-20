package org.firstinspires.ftc.teamcode.bertie;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

@TeleOp(name = "<BERTIE> TeleOp Drive")
public class BertieTeleOp extends BunyipsOpMode {
    private BertieBunyipConfiguration config;
    private BertieBunyipDrive drive = null;
    @Override
    protected void onInit() {
        config = BertieBunyipConfiguration.newConfig(hardwareMap, telemetry);

        try {
            drive = new BertieBunyipDrive(this,
                    config.frontLeft, config.frontRight,
                    config.backLeft, config.backRight,
                    false);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise drive.");
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;
        drive.setSpeedXYR(y,-x,r);
        drive.update();
    }
}
