package org.firstinspires.ftc.teamcode.wheatley.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

/**
 * Template (response)
 */
@TeleOp(name = "WHEATLEY: ", group = "WHEATLEY")
@Disabled
public class WheatleyDummy extends BunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInit() {
        config.init(this, hardwareMap);
    }

    @Override
    protected void activeLoop() {

    }
}