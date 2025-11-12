package org.firstinspires.ftc.teamcode.teleop;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Jonas;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.InterpolatedLookupTable;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Angle;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.groups.ParallelTaskGroup;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;

@Config
@TeleOp(name = "TeleOp")
public class MainTeleOp extends BunyipsOpMode {
    public static double outputPower = 1.0;
    public static boolean FIELD_CENTRIC_ENABLED = true;
    private final Jonas robot = new Jonas();
    private final InterpolatedLookupTable distanceToGoalPower = new InterpolatedLookupTable() {{
        add(20.5, 0.3);
        add(45, 0.9);
        add(74, 1.0);
        createLUT();
    }};
    private Vector2d goal = new Vector2d(-62, -62); // default to blue (arbitrary). this is set in init otherwise

    @Override
    protected void onInit() {
        robot.init();

        Measure<Angle> offset;
        StartingConfiguration.Position startingPos = Storage.memory().lastKnownStartingConfiguration;
        if (startingPos == null) {
            // starting position does not exist
            offset = Radians.of(Storage.memory().lastKnownPosition.heading.toDouble());
        } else {
            // starting position is valid and exists
            goal = new Vector2d(-62, -62 * startingPos.alliance.getDirectionMultiplier());
            offset = Radians.of(startingPos.toFieldPose().heading.toDouble());
        }
        Dbg.log(offset);

        UserSelection<String> fieldCentricSelector = new UserSelection<>(
            (s) -> FIELD_CENTRIC_ENABLED = s == null || s.equals("FIELD-CENTRIC"), "ROBOT-CENTRIC", "FIELD-CENTRIC")
            .captionLayer(0, "SELECT DRIVE MODE");
        setInitTask(fieldCentricSelector.asAsyncTask());

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> FIELD_CENTRIC_ENABLED);
        driveTask.setFieldCentricOffset(offset);
        robot.drive.setDefaultTask(driveTask);

        gamepad1.button(A)
            .onTrue("Reset FC Origin", driveTask::resetFieldCentricOrigin);
        gamepad1.button(Y)
            .onTrue("Invert FC Origin", () -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        gamepad2.button(DPAD_UP)
            .whileTrue(robot.output.tasks.control(() -> outputPower));
        gamepad2.button(LEFT_BUMPER)
            .whileTrue(robot.intake.tasks.run(1));
        gamepad2.button(DPAD_LEFT)
            .onTrue(robot.preventer.tasks.toggle());

        gamepad2.button(Y)
            .toggleOnTrue(
                new ParallelTaskGroup(
                    robot.lights.tasks.setPatternFor(Seconds.of(2.4), RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE)
                        .then(robot.lights.tasks.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE)),
                            robot.output.tasks.control(() -> outputPower),
                            robot.intake.tasks.run(1)
                            .after(robot.preventer.tasks.open().after(2, Seconds))
                ).until(gamepad2.button(A))
            );
        gamepad2.button(A)
            .toggleOnTrue(
                new ParallelTaskGroup(
                    robot.intake.tasks.run(1).with(robot.preventer.tasks.close()),
                    robot.lights.tasks.setPattern(RevBlinkinLedDriver.BlinkinPattern.GRAY)
                ).until(gamepad2.button(Y))
            );
        gamepad2.button(RIGHT_BUMPER)
            // Use an adaptive guess for the output power based on the interpolated lookup table
            // ** Assumes that the robot knows where it is on the field from auto or elsewhere.
            .toggleOnTrue(looping(() -> {
                // modulus of the vector between the goal and robot
                double distance = goal.minus(robot.drive.getPose().position).norm();
                outputPower = distanceToGoalPower.get(distance);
                telemetry.addData("Distance to goal (in)", distance);
                telemetry.add("ADAPTIVE FLYWHEEL ENABLED").color("green").h1();
            }).onFinish(() -> outputPower = 1).named("Adaptive Flywheel"));
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