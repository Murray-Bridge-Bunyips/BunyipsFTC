package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsLib;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.DualTelemetry;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Controller;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;

@TeleOp
public class SanityCheck extends BunyipsOpMode {
    @Override
    protected void activeLoop() {
        Dbg.log(BunyipsLib.getOpMode().gamepad1 instanceof Controller);
        Dbg.log(BunyipsLib.getOpMode().gamepad2 instanceof Controller);
        Dbg.log(BunyipsLib.getOpMode().telemetry instanceof DualTelemetry);
    }
}
