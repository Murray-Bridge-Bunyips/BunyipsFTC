package org.firstinspires.ftc.teamcode.debug;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;
import org.firstinspires.ftc.teamcode.Vance;

/**
 * PID Testing
 */
//@TeleOp
public class VancePID extends BunyipsOpMode {
    private final Vance robot = new Vance();

    @Override
    protected void onInit() {
        robot.init();
    }

    @Override
    protected void activeLoop() {
        robot.shoulder.setPower(-gamepad1.lsy);
        Motor.debug(robot.hw.shoulder, "Vertical Lift", t);
        robot.shoulder.update();
    }
}
