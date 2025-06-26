package org.firstinspires.ftc.teamcode.teleop;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Radians;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Joker;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Angle;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Measure;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.StartingConfiguration;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Storage;

// bubner hamchurger

/**
 * "Whoooaaa! Looking cool, Joker!"
 */
@TeleOp(name = "TeleOp")
public class TeleOpCommandBASED extends CommandBasedBunyipsOpMode {
    private final Joker robot = new Joker();
    public static StartingConfiguration.Position startingPos;
    private Measure<Angle> offset;

    @Override
    protected void onInitialise() {
        robot.init();
        // below is a fragile piece of code known only as the field centric fixer
        // it did not work until it did with the same code for no reason
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
    }
//Giulio was here and he is better than you at coding
    @Override
    protected void onStart() {
        robot.outtakeGrip.open();
    }

    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.RIGHT_BUMPER)
                .run(robot.outtakeGrip.tasks.toggle());
        operator().whenPressed(Controls.LEFT_BUMPER)
                .run(robot.intakeAlign.tasks.toggle());

        operator().whenRising(Controls.Analog.LEFT_TRIGGER, (v) -> v > 0.9)
                .run(robot.intakeGrip.tasks.toggle());

        operator().whenPressed(Controls.A)
                .run(robot.lift.tasks.home().timeout(Seconds.of(3)));
        operator().whenPressed(Controls.X)
                .run(robot.lift.tasks.goTo(270).timeout(Seconds.of(3)));
        operator().whenPressed(Controls.Y)
                .run(robot.lift.tasks.goTo(2400).timeout(Seconds.of(2)));

        robot.ascentArm.setDefaultTask(robot.ascentArm.tasks.control(() -> gamepad2.dpad_left ? -0.3 : gamepad2.dpad_right ? 0.3 : 0));

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive).withFieldCentric(() -> true);
        driveTask.setFieldCentricOffset(offset);

        driver().whenPressed(Controls.A)
                .run(driveTask::resetFieldCentricOrigin);
        driver().whenPressed(Controls.Y)
                .run(() -> driveTask.setFieldCentricOffset(Radians.of(robot.drive.getPose().heading.toDouble() + Math.PI)));

        robot.drive.setDefaultTask(driveTask);
        robot.intake.setDefaultTask(robot.intake.tasks.control(() -> -gamepad2.lsy));
        robot.lift.setDefaultTask(robot.lift.tasks.control(() -> -gamepad2.rsy));
    }

    @Override
    protected void periodic() {
        robot.hw.hook.setPower(gamepad2.dpad_up ? 1 : gamepad2.dpad_down ? -1 : 0);
        telemetry.addData("lift current position", robot.hw.liftMotor.getCurrentPosition());
        telemetry.addData("lift target position", robot.hw.liftMotor.getTargetPosition());
        telemetry.addData("lift power", robot.hw.liftMotor.getPower());
        telemetry.addData("intake power", robot.hw.intakeMotor.getPower());
    }
    // lucas bubner was here and NO ONE WILL BELIEVE YOU
}