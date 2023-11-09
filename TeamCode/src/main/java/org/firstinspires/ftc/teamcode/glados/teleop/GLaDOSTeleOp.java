package org.firstinspires.ftc.teamcode.glados.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.IMUOp;
import org.firstinspires.ftc.teamcode.common.RelativeVector;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSAlignmentCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSArmCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSFieldDriveCore;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSServoCore;

/**
 * TeleOp for GLaDOS robot FTC 15215
 * > gamepad1.left_stick for planar translation
 * > gamepad1.right_stick for in-place rotation
 * > gamepad1.y to reset field relative heading to current heading
 *
 * @author Lucas Bubner, 2023
 */
@TeleOp(name = "GLaDOS: TeleOp", group = "GLaDOS")
public class GLaDOSTeleOp extends BunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private GLaDOSFieldDriveCore drive;
    private GLaDOSArmCore arm;
    private IMUOp imu;

    private boolean x_pressed;
    private boolean b_pressed;

    @Override
    protected void onInit() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        imu = new IMUOp(this, config.imu);
        drive = new GLaDOSFieldDriveCore(this, config.fl, config.bl, config.fr, config.br, imu, false, RelativeVector.FORWARD);
        arm = new GLaDOSArmCore(this, config.sr, config.sa, config.al, config.ls, config.rs, GLaDOSAlignmentCore.Mode.MANUAL);
        arm.getServoController().update();
    }

    @Override
    protected void activeLoop() {
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x;

        drive.setSpeedUsingController(x, y, r / 2);

        arm.getSliderController().setTargetAngleUsingController(gamepad2.left_stick_y);
        arm.getSliderController().setExtrusionPowerUsingController(gamepad2.right_stick_y);
        arm.getAlignmentController().setPositionUsingDpad(gamepad2.dpad_up, gamepad2.dpad_down);

        if (gamepad2.x && !x_pressed) {
            arm.getServoController().toggleServo(GLaDOSServoCore.ServoSide.LEFT);
        }

        if (gamepad2.b && !b_pressed) {
            arm.getServoController().toggleServo(GLaDOSServoCore.ServoSide.RIGHT);
        }

        if (gamepad1.y) {
            imu.resetHeading();
        }

        // Ensure that the buttons are only registered once per press
        x_pressed = gamepad2.x;
        b_pressed = gamepad2.b;

        drive.update();
        arm.update();
    }
}
