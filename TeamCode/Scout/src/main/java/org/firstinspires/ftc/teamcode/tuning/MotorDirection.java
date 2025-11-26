package org.firstinspires.ftc.teamcode.tuning;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.AutonomousBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import dev.frozenmilk.util.cell.RefCell;

/**
 * Utility to assist in determining directions for two Core Hex motors.
 *
 * @author Lucas Bubner, 2025
 */
@RobotConfig.InhibitAutoInit
@TeleOp(name = "Motor Direction Finder", group = "tuning")
public class MotorDirection extends AutonomousBunyipsOpMode {
    @Override
    protected void onInitialise() {
    }

    @Override
    protected void onReady(@Nullable RefCell<?> selectedOpMode) {
        DcMotor left = hardwareMap.dcMotor.get("l");
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        DcMotor right = hardwareMap.dcMotor.get("r");
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right.setDirection(DcMotorSimple.Direction.FORWARD);
        Task task = Task.task()
                .periodic(() -> {
                    telemetry.addData("Instructions", "Roll the robot along the ground in the direction of forward motion. Ensure encoder counts are updating but ignore sign.");
                    telemetry.addData("Ticks (left)", left.getCurrentPosition());//vandalism mode activated
                    telemetry.addData("Ticks (right)", right.getCurrentPosition());
                })
                .isFinished(() -> !Mathf.isNear(left.getCurrentPosition(), 0, 288) // 1 rotation for Core Hex
                        && !Mathf.isNear(right.getCurrentPosition(), 0, 288))
                .named("Motor Rotation Test");
        add(task);
        run("Determine Polarity", () -> {
            String leftDir = left.getCurrentPosition() > 0 ? "FORWARD" : "REVERSE";
            Dbg.log("LEFT_WHEEL_DIRECTION: %", leftDir);
            telemetry.addData("LEFT_WHEEL_DIRECTION", leftDir)
                    .setRetained(true);
            String rightDir = right.getCurrentPosition() > 0 ? "FORWARD" : "REVERSE";
            Dbg.log("RIGHT_WHEEL_DIRECTION: %", rightDir);
            telemetry.addData("RIGHT_WHEEL_DIRECTION", rightDir)
                    .setRetained(true);
        });
    }
}
