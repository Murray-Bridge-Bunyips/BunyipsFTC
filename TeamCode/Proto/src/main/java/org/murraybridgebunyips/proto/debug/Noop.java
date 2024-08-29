package org.murraybridgebunyips.proto.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.proto.Proto;

/**
 * No-op to init hardware and print timer status.
 */
@TeleOp
public class Noop extends BunyipsOpMode {
    private final Proto robot = new Proto();

    @Override
    protected void onInit() {
        robot.init();
    }

    @Override
    protected void activeLoop() {
        telemetry.add(timer);
    }
}
