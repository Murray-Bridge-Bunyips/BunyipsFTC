package org.firstinspires.ftc.teamcode.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.RelativeVector;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.Text;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSDriveCore;

/**
 * TeleOp for GLaDOS robot FTC 15215
 * Functionality is currently unknown...
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLADOS: TeleOp", group = "GLADOS")
public class GLaDOSTeleOp extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private IMUOp imu;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        imu = new IMUOp(this, config.imu);
        // FIXME: Forward left is forward right, confirm back left and right do work. Forward left untested
        drive = new GLaDOSDriveCore(this, config.fl, config.bl, config.fr, config.br, imu, RelativeVector.FORWARD_LEFT);
    }

    @Override
    protected void activeLoop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;
        drive.setSpeedXYR(x, y, r);
        drive.update();

        if (gamepad1.y) {
            imu.resetHeading();
        }

        // IMU debugging
        addTelemetry(Text.format("Heading (deg): %s", imu.getHeading() % 360));
//        addTelemetry(Text.format("Heading (deg, degraw): %s %s", String.valueOf(imu.getHeading()), String.valueOf(imu.getRawHeading())));
        addTelemetry(Text.format("Heading (f,l,r,b): %s %s %s %s", String.valueOf(RelativeVector.FORWARD.getAngle()), String.valueOf(RelativeVector.LEFT.getAngle()), String.valueOf(RelativeVector.RIGHT.getAngle()), String.valueOf(RelativeVector.BACKWARD.getAngle())));
        addTelemetry(Text.format("Heading (fl,fr,bl,br): %s %s %s %s", String.valueOf(RelativeVector.FORWARD_LEFT.getAngle()), String.valueOf(RelativeVector.FORWARD_RIGHT.getAngle()), String.valueOf(RelativeVector.BACKWARD_LEFT.getAngle()), String.valueOf(RelativeVector.BACKWARD_RIGHT.getAngle())));
    }
}
