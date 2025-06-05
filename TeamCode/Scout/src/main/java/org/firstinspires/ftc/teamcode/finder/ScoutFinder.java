package org.firstinspires.ftc.teamcode.finder;

import androidx.annotation.Nullable;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import dev.frozenmilk.util.cell.RefCell;

/**
 * Utility to assist in determining minibot configuration constants for two Core Hex motors, with logo facing up.
 *
 * @author Lucas Bubner, 2025
 */
// OpMode is registered in the Registrar class
@RobotConfig.InhibitAutoInit
public class ScoutFinder extends AutonomousBunyipsOpMode {
    @Override
    protected void onInitialise() {
        setOpModes("LEFT MOTOR", "RIGHT MOTOR", "IMU")
                .captionLayer(0, "FIND TARGET")
                .assignButton(0, 0, Controls.X)
                .assignButton(0, 1, Controls.B)
                .assignButton(0, 2, Controls.A);
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        if (selectedOpMode == null)
            return;
        switch ((String) selectedOpMode.get()) {
            case "LEFT MOTOR":
                DcMotor left = hardwareMap.dcMotor.get("l");
                left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                left.setDirection(DcMotorSimple.Direction.FORWARD);
                add(
                    Task.task()
                        .periodic(() -> {
                            telemetry.addData("Instructions", "Rotate left wheel in direction of forward motion until completed.");
                            telemetry.addData("Ticks", left.getCurrentPosition());//vandalism mode activated
                        })
                        .isFinished(() -> !Mathf.isNear(left.getCurrentPosition(), 0, 200))
                        .named("Left Motor Rotation Test")
                );
                run("Determine Polarity", () -> {
                    String direction = left.getCurrentPosition() > 0 ? "FORWARD" : "REVERSE";
                    Dbg.log("LEFT_WHEEL_DIRECTION: %", direction);
                    telemetry.addData("LEFT_WHEEL_DIRECTION", direction)
                            .setRetained(true);
                });
                break;
            case "RIGHT MOTOR":
                DcMotor right = hardwareMap.dcMotor.get("r");
                right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                right.setDirection(DcMotorSimple.Direction.FORWARD);
                add(
                        Task.task()
                                .periodic(() -> {
                                    telemetry.addData("Instructions", "Rotate right wheel in direction of forward motion until completed.");
                                    telemetry.addData("Ticks", right.getCurrentPosition());
                                })
                                .isFinished(() -> !Mathf.isNear(right.getCurrentPosition(), 0, 200))
                                .named("Right Motor Rotation Test")
                );
                run("Determine Polarity", () -> {
                    String direction = right.getCurrentPosition() > 0 ? "FORWARD" : "REVERSE";
                    Dbg.log("RIGHT_WHEEL_DIRECTION: %", direction);
                    telemetry.addData("RIGHT_WHEEL_DIRECTION", direction)
                            .setRetained(true);
                });
                break;
            case "IMU":
                IMU imu = hardwareMap.get(IMU.class, "imu");
                // Origin to use
                imu.initialize(new IMU.Parameters(
                        new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT))
                );
                imu.resetYaw();
                add(
                        Task.task()
                                .periodic(() -> {
                                    telemetry.addData("Instructions", "Rotate robot +CCW by 90 degrees and press gamepad1.a");
                                    telemetry.addData("Angle", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
                                })
                                .isFinished(() -> gamepad1.a)
                                .named("IMU Rotation Test")
                );
                run("Determine Direction", () -> {
                    String direction;
                    double angDeg = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                    if (Mathf.isNear(angDeg, 90, 45)) {
                        direction = "LEFT";
                    } else if (Mathf.isNear(angDeg, -90, 45)) {
                        direction = "RIGHT";
                    } else {
                        direction = "UNKNOWN, TRY AGAIN";
                    }
                    Dbg.log("IMU_USB_DIRECTION: %", direction);
                    telemetry.addData("IMU_USB_DIRECTION", direction)
                            .setRetained(true);
                });
                break;
        }
    }
}
