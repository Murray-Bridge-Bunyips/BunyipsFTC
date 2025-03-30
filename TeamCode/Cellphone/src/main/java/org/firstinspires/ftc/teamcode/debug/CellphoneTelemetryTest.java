package org.firstinspires.ftc.teamcode.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;

@TeleOp
public class CellphoneTelemetryTest extends BunyipsOpMode {
    @Override
    protected void activeLoop() {
//        telemetry.add("Hello world!");
        telemetry.add("Goodbye world!");
    }
}
