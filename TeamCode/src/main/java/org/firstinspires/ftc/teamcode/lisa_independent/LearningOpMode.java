package org.firstinspires.ftc.teamcode.lisa_independent;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "chair")
public class LearningOpMode extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Hello","World");
        telemetry.update ();

    }
//How do I code in java?

    @Override
    public void loop() {

    }
}
