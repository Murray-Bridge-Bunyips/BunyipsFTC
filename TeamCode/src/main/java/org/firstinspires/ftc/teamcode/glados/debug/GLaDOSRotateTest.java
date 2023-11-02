package org.firstinspires.ftc.teamcode.glados.debug;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

@TeleOp(name="GLaDOS: Rotator Test", group="GLaDOS")
public class GLaDOSRotateTest extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    double target;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        config.sr.track();
        config.sr.setTargetPosition(0);
        config.sr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        config.sr.setPower(0.3);
    }

    @Override
    protected void activeLoop() {
        target += gamepad1.left_stick_y * 2;
        if (gamepad1.a) {
            target = 0.0;
        }
        addTelemetry(String.valueOf(config.sr.getDegrees()));
        config.sr.setDegrees(target);
    }
}
