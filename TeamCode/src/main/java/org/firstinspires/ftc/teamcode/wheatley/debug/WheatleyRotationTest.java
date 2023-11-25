package org.firstinspires.ftc.teamcode.wheatley.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

@TeleOp(name = "WHEATLEY: Rotation Test", group = "WHEATLEY")
public class WheatleyRotationTest extends BunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInit() {
        config.init(this, hardwareMap);
    }

    @Override
    protected void activeLoop() {
        config.ra.setPower(gamepad1.left_stick_y);
    }
}
