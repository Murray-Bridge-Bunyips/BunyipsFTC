package org.firstinspires.ftc.team22407.wheatley.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team22407.wheatley.components.WheatleyConfig;
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode;

/**
 * Template (response)
 */
@TeleOp(name = "")
@Disabled
public class WheatleyDummy extends BunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();

    @Override
    protected void onInit() {
        config.init(this);
    }

    @Override
    protected void activeLoop() {

    }
}