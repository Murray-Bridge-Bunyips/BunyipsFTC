package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

/**
 * Template (response)
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "template", group = "GLaDOS")
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
