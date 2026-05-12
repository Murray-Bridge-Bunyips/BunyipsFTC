package org.firstinspires.ftc.teamcode.teleop;


import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

// ------ Recommended static imports for Scheduler, do not remove! --------
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;
// ------------------------------------------------------------------------

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

//TODO: Redo whole thing
@Config
@TeleOp(name = "TeleOp")
public class MainTeleOp extends BunyipsOpMode {
    public static double DEFAULT_OUTPUT_POWER = 0.8;
    public static double MAX_OUTPUT_POWER = 1.0;
    public static boolean FIELD_CENTRIC_ENABLED = true;

    private final Jonas robot = new Jonas();
    private final InterpolatedLookupTable distanceToGoalPower = new InterpolatedLookupTable() {{
        add(20.5, 0.3); // inches from goal base to front of wheel <-> optimal output power
        add(45, 0.9);
        add(74, 1.0);
        createLUT();
    }};
    private Vector2d goal = new Vector2d(-62, -62); // default to blue (arbitrary). this is set in init otherwise
    private double currentOutputPower = DEFAULT_OUTPUT_POWER;
    private boolean adaptiveControl = false;
    RevBlinkinLedDriver.BlinkinPattern lightsIntakeColour, lightsChargeColour, lightsLaunchColour;

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

        if (startingPos != null) {
            if (startingPos.isRed()) {
                lightsIntakeColour = RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED;
                lightsChargeColour = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
                lightsLaunchColour = RevBlinkinLedDriver.BlinkinPattern.RED;
            } else if ((startingPos.isBlue())) {
                lightsIntakeColour = RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_BLUE;
                lightsChargeColour = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE;
                lightsLaunchColour = RevBlinkinLedDriver.BlinkinPattern.BLUE;
            }
        }
        else {
            lightsIntakeColour = RevBlinkinLedDriver.BlinkinPattern.GRAY;
            lightsChargeColour = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
            lightsLaunchColour = RevBlinkinLedDriver.BlinkinPattern.WHITE;
        }

        UserSelection<String> fieldCentricSelector = new UserSelection<>(
            (s) -> FIELD_CENTRIC_ENABLED = s == null || s.equals("FIELD-CENTRIC"), "ROBOT-CENTRIC", "FIELD-CENTRIC")
            .captionLayer(0, "SELECT DRIVE MODE");
        setInitTask(fieldCentricSelector.asAsyncTask());

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive)
                .withFieldCentric(() -> FIELD_CENTRIC_ENABLED);
        driveTask.setFieldCentricOffset(offset);
        robot.drive.setDefaultTask(driveTask);

//        who up personaing they 5
        gamepad1.button(A)
            .onTrue("Reset FC Origin", driveTask::resetFieldCentricOrigin);
        gamepad1.button(Y)
            .onTrue("Invert FC Origin", () -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        gamepad2.button(DPAD_UP)
            .whileTrue(robot.output.tasks.control(() -> currentOutputPower));
        gamepad2.button(LEFT_BUMPER)
            .whileTrue(robot.intake.tasks.run(1));
        gamepad2.button(DPAD_LEFT)
            .onTrue(robot.preventer.tasks.toggle());

        gamepad2.button(Y)
            .toggleOnTrue(
                new ParallelTaskGroup(
                    robot.intake.tasks.runFor(Seconds.of(2), 1),
                    robot.lights.tasks.setPatternFor(Seconds.of(2.4), lightsChargeColour)
                        .then(robot.lights.tasks.setPattern(lightsLaunchColour)),
                    robot.output.tasks.control(() -> currentOutputPower),
                    robot.intake.tasks.run(1).after(robot.preventer.tasks.open().after(2.4, Seconds))
                ).until(gamepad2.button(A))
            );
        gamepad2.button(A)
            .toggleOnTrue(
                new ParallelTaskGroup(
                    robot.intake.tasks.run(1).with(robot.preventer.tasks.close()),
                    robot.lights.tasks.setPattern(lightsIntakeColour)
                ).until(gamepad2.button(Y))
            );
        gamepad2.button(RIGHT_BUMPER)
                .onTrue("Toggle Adaptive Flywheel", () -> adaptiveControl = !adaptiveControl);
    }

    @Override
    protected void onStart() {
        robot.preventer.close();
    }

    @Override
    protected void activeLoop() {
        // Use an adaptive guess for the output power based on the interpolated lookup table
        // ** Assumes that the robot knows where it is on the field from auto or elsewhere.
        if (adaptiveControl) {
            double distance = goal.minus(robot.drive.getPose().position).norm(); // |goal-robot| vector magnitude
            currentOutputPower = distanceToGoalPower.get(distance);
            telemetry.addData("Distance to goal (in)", distance);
            telemetry.add("ADAPTIVE FLYWHEEL ENABLED").color("green").h1();
        } else {
            currentOutputPower = DEFAULT_OUTPUT_POWER;
            telemetry.add("ADAPTIVE FLYWHEEL DISABLED").color("red").h1();
        }
        currentOutputPower = Math.min(currentOutputPower, MAX_OUTPUT_POWER);

        Scheduler.update();
    }
}