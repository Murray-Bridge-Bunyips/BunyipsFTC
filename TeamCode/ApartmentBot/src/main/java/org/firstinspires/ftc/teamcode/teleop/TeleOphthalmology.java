package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.ApartmentBot;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;

@Config
@TeleOp(name = "TeleOp")
public class TeleOphthalmology extends BunyipsOpMode {
    private final ApartmentBot robot = new ApartmentBot();

    @Override
    protected void onInit() {
        robot.init();

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> false);
//                .withFieldCentric(() -> true);
        robot.drive.setDefaultTask(driveTask);

        gamepad1.button(A)
                .onTrue("Reset Field-Centric Origin", driveTask::resetFieldCentricOrigin);
        gamepad1.button(Y)
                .onTrue("Invert Field-Centric Origin", () -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        robot.intake.setDefaultTask(robot.intake.tasks.control(() -> -gamepad2.lsy));
        robot.output.setDefaultTask(robot.output.tasks.control(() -> -gamepad2.rsy));

        gamepad2.button(LEFT_BUMPER)
                .onTrue(robot.preventer.tasks.toggle());

        gamepad2.button(A)
                .onTrue(robot.launch);
        gamepad2.button(B)
                .onTrue(robot.eject);
    }

    @Override
    protected void onStart() {
        robot.preventer.close();
    }

    @Override
    protected void activeLoop() {
        Scheduler.update();
    }
}
