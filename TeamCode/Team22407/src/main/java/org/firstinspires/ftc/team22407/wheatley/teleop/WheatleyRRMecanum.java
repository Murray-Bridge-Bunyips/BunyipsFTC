package org.firstinspires.ftc.team22407.wheatley.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team22407.wheatley.components.WheatleyConfig;
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.ftc.bunyipslib.MecanumDrive;
import org.murraybridgebunyips.ftc.bunyipslib.RobotConfig;

/**
 * RoadRunner Mecanum Drive test for TeleOp
 */
@TeleOp(name = "WHEATLEY: RoadRunner Mecanum Drive", group = "WHEATLEY")
//@Disabled
public class WheatleyRRMecanum extends BunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;

    @Override
    protected void onInit() {
        config.init(this);
        RobotConfig.setLastKnownPosition(null);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
    }

    @Override
    protected void activeLoop() {
        drive.setSpeedUsingController(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        drive.update();
    }

    @Override
    protected void onStop() {
        drive.stop();
    }
}