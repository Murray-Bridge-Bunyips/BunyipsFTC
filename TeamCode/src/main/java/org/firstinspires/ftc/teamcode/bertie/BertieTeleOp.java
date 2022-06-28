package org.firstinspires.ftc.teamcode.bertie;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsController;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;

@Disabled // Disabled until I find out how this works
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

    }
}
