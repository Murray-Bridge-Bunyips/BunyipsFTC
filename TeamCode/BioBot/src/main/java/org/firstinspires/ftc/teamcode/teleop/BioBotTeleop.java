package org.firstinspires.ftc.teamcode.teleop;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BioBot;

@Config
@TeleOp(name = "TeleOp")
public class BioBotTeleop extends BunyipsOpMode {
    private final BioBot robot = new BioBot();
    private final double powerMult = 0.9;

    @Override
    protected void onInit() {
        robot.init();

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> false);
        robot.drive.setDefaultTask(driveTask);

        gamepad1.button(A)
                .onTrue("Reset Field-Centric Origin", driveTask::resetFieldCentricOrigin);
        gamepad1.button(Y)
                .onTrue("Invert Field-Centric Origin", () -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        robot.intake.setDefaultTask(robot.intake.tasks.control(() -> -gamepad2.lsy));
        robot.outtake.setDefaultTask(robot.outtake.tasks.control(() -> (-gamepad2.rsy * powerMult)));

        gamepad2.button(X)
                .onTrue(robot.depollinator.tasks.toggle());
    }

    @Override
    protected void onStart() {
        robot.depollinator.open();
    }

    @Override
    protected void activeLoop() {
        Scheduler.update();
    }
}
