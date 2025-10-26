package org.firstinspires.ftc.teamcode.teleop;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Angle;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Threads;

@Config
@TeleOp(name = "TeleOp")
public class MainTeleOp extends BunyipsOpMode {
    public static boolean FIELD_CENTRIC_ENABLED = true;
    private final Jonas robot = new Jonas();

    @Override
    protected void onInit() {
        robot.init();

        Measure<Angle> offset;
        StartingConfiguration.Position startingPos = Storage.memory().lastKnownStartingConfiguration;
        if (startingPos == null) {
            offset = Radians.of(Storage.memory().lastKnownPosition.heading.toDouble());
            Dbg.log("startingPos was null");
        } else if (startingPos.isRed() || startingPos.isBlue()) {
            offset = Radians.of(startingPos.toFieldPose().heading.toDouble());
            Dbg.log("startingPos was valid (red or blue)");
        }
        else {
            offset = Radians.of(Storage.memory().lastKnownPosition.heading.toDouble());
            Dbg.log("startingPos was not null or valid");
        }
        Dbg.log(offset);

        UserSelection<String> fieldCentricSelector = new UserSelection<>(
                (s) -> FIELD_CENTRIC_ENABLED = s == null || s.equals("FIELD-CENTRIC"), "ROBOT-CENTRIC", "FIELD-CENTRIC")
                .captionLayer(0, "SELECT DRIVE MODE");
        setInitTask(task().init(() -> Threads.start("drive selector", fieldCentricSelector))
                .isFinished(() -> !Threads.isRunning(fieldCentricSelector)));

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive);
        driveTask.setFieldCentricOffset(offset);
        driveTask.withFieldCentric(() -> FIELD_CENTRIC_ENABLED)
                .setAsDefaultTask();
        gamepad1.button(A)
                .onTrue("Reset FC Origin", driveTask::resetFieldCentricOrigin);
        gamepad1.button(Y)
                .onTrue("Invert FC Origin", () -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        gamepad2.button(DPAD_UP)
                .whileTrue(robot.output.tasks.run(1));
        gamepad2.button(LEFT_BUMPER)
                .whileTrue(robot.intake.tasks.run(1));
        gamepad2.button(DPAD_LEFT)
                .onTrue(robot.preventer.tasks.toggle());

        gamepad2.button(Y)
                .toggleOnTrue(
                        new ParallelTaskGroup(
                                robot.output.tasks.run(1),
                                robot.intake.tasks.run(1)
                                        .after(robot.preventer.tasks.open().after(1.6, Seconds))
                        ).until(gamepad2.button(A))
                );
        gamepad2.button(A)
                .toggleOnTrue(
                        robot.intake.tasks.run(1).with(robot.preventer.tasks.close())
                            .until(gamepad2.button(Y))
                );

        robot.drive.setDefaultTask(driveTask);
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