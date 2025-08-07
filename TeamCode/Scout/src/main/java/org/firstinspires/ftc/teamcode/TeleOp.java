package org.firstinspires.ftc.teamcode;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Geometry;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends BunyipsOpMode {
    private double mul = 1;

    @Override
    protected void activeLoop() {
        if (gamepad1.getDebounced(Controls.LEFT_BUMPER) && gamepad1.back)
            mul -= 0.25;
        if (gamepad1.getDebounced(Controls.RIGHT_BUMPER) && gamepad1.back)
            mul += 0.25;
        mul = Mathf.clamp(mul, 0, 1);
        telemetry.addData("Speed Multiplier (adjust with BACK+LB/RB)", mul);
        Scout.instance.drive.setPower(Geometry.vel((gamepad1.rt - gamepad1.lt) * mul, 0, -gamepad1.lsx * mul));
        Scout.instance.drive.update();
    }
}
