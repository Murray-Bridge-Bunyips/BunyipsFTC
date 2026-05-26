package org.firstinspires.ftc.teamcode.teleop;


// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;

@Config
@TeleOp(name = "Flywheel Tuning")
public class FlywheelTuning extends BunyipsOpMode {
    private final Jonas robot = new Jonas();

    @Override
    protected void onInit() {
        robot.init();

        robot.output.setDefaultTask(robot.output.tasks.control(() -> gamepad1.left_stick_y));
    }

    @Override
    protected void onStart() {
        robot.preventer.open();
    }

    @Override
    protected void activeLoop() {
        telemetry.addData("currentVelocity", robot.hw.output.getVelocity());
        telemetry.addData("targetVelocity", robot.hw.output.getRunUsingEncoderController().pidf().get().getSetpoint());
        telemetry.addData("kP", robot.kP);
        telemetry.addData("kI", robot.kI);
        telemetry.addData("kD", robot.kD);
        telemetry.addData("kF", robot.kF);

        Scheduler.update();
    }
}