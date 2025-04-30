package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;

@TeleOp
public class ArmTuning extends BunyipsOpMode {
    @Override
    protected void activeLoop() {
        Vance.instance.shoulder.setPower(-gamepad1.lsy);
        Vance.instance.shoulder.update();
    }
}
