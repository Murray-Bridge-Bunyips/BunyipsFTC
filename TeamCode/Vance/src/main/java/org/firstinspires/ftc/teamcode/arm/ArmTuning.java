package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;

@TeleOp
public class ArmTuning extends BunyipsOpMode {
    @Override
    protected void activeLoop() {
        Motor.debug(Vance.instance.hw.shoulder, "Shoulder", telemetry);
        Vance.instance.shoulder.setPower(-gamepad1.lsy);
        Vance.instance.shoulder.update();
        Motor.debug(Vance.instance.hw.elbow, "Elbow", telemetry);
        Vance.instance.elbow.setPower(-gamepad1.rsy);
        Vance.instance.elbow.update();
    }
}
