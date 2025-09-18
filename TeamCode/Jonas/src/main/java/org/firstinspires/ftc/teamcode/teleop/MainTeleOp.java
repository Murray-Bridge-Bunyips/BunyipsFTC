package org.firstinspires.ftc.teamcode.teleop;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Angle;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Threads;

@TeleOp(name = "TeleOp")
public class MainTeleOp extends CommandBasedBunyipsOpMode {
    private final Jonas robot = new Jonas();
    public static StartingConfiguration.Position startingPos;
    private Measure<Angle> offset;

    public static boolean FIELD_CENTRIC_ENABLED = true;

    @Override
    protected void onInitialise() {
        robot.init();

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
        startingPos = null;

        UserSelection<String> fieldCentricSelector = new UserSelection<>(
                (s) -> FIELD_CENTRIC_ENABLED = s == null || s.equals("FIELD-CENTRIC"), "ROBOT-CENTRIC", "FIELD-CENTRIC")
                .captionLayer(0, "SELECT DRIVE MODE");
        setInitTask(Task.task()
                .init(() -> Threads.start("drive selector", fieldCentricSelector))
                .isFinished(() -> !Threads.isRunning(fieldCentricSelector)));
    }

    @Override
    protected void assignCommands() {
        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive);
        driveTask.withFieldCentric(() -> FIELD_CENTRIC_ENABLED).setAsDefaultTask();

        driveTask.setFieldCentricOffset(offset);

        driver().whenPressed(Controls.A)
                .run(driveTask::resetFieldCentricOrigin);
        driver().whenPressed(Controls.Y)
                .run(() -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        robot.outputA.setDefaultTask(robot.outputA.tasks.control(() -> gamepad2.right_trigger));
        robot.outputB.setDefaultTask(robot.outputB.tasks.control(() -> gamepad2.right_trigger));

        robot.drive.setDefaultTask(driveTask);
    }
}