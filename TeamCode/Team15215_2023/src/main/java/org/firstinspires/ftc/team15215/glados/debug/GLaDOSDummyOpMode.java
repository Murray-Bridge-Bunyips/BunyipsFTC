package org.firstinspires.ftc.team15215.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team15215.glados.components.GLaDOSConfigCore;
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode;

/**
 * Template (response)
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "template")
@Disabled
public class GLaDOSDummyOpMode extends BunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInit() {
        config.init(this);
    }

    @Override
    protected void activeLoop() {

    }
}
