package org.murraybridgebunyips.cellphone.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;

/**
 * Html test
 */
@TeleOp
public class CellphoneHtmlTest extends BunyipsOpMode {
    @Override
    protected void onInit() {
        setInitTask(new WaitTask(Seconds.of(1), false));
    }

    @Override
    protected void activeLoop() {
        telemetry.add("red fg").color("red");
        telemetry.add("green fg").color("green");
        telemetry.add("blue fg").color("blue");
        telemetry.add("red bg").bgColor("red");
        telemetry.add("green bg").bgColor("green");
        telemetry.add("blue bg").bgColor("blue");
        telemetry.add("big").big();
        telemetry.add("small").small();
        telemetry.add("h1").h1();
        telemetry.add("h2").h2();
        telemetry.add("h3").h3();
        telemetry.add("h4").h4();
        telemetry.add("h5").h5();
        telemetry.add("h6").h6();
        telemetry.add("bold").bold();
        telemetry.add("italics").italic();
        telemetry.add("underline").underline();
        telemetry.add("strikethrough").strikethrough();
        telemetry.add("subscript").subscript();
        telemetry.add("superscript").superscript();
    }
}
