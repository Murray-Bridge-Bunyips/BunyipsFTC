package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;

import org.firstinspires.ftc.teamcode.Joker;

/**
 * "Whoooaaa! Looking cool, Joker!"
 */
@TeleOp(name = "TeleOp")
public class TeleOpCommandBASED extends CommandBasedBunyipsOpMode {
    private final Joker robot = new Joker();
//    public static StartingConfiguration.Position startingPos;

    @Override
    protected void onInitialise() {
        robot.init();
        robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.LAWN_GREEN);
    }

    @Override
    protected void onStart() {
        robot.outtakeGrip.open();
    }

    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.RIGHT_BUMPER)
            .run(robot.outtakeGrip.tasks.toggle());

        robot.ascentArm.setDefaultTask(robot.ascentArm.tasks.control(() -> gamepad2.dpad_left ? -0.3 : gamepad2.dpad_right ? 0.3 : 0));

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive).withFieldCentric(() -> true);

        //TODO: debug this because it always starts teleop with backwards robot centric controls
        /*Measure<Angle> offset;
        if (startingPos == null) {
            offset = Radians.of(-Storage.memory().lastKnownPosition.heading.toDouble());
            Dbg.log("offset was null");
        }
        else if (startingPos.isLeft()) {
            if (startingPos.isBlue()) {offset = Radians.of(-Storage.memory().lastKnownPosition.heading.toDouble()-Math.PI/2);}
            else {offset = Radians.of(-Storage.memory().lastKnownPosition.heading.toDouble()+Math.PI/2);}
        }
        else {
            if (startingPos.isBlue()) {offset = Radians.of(-Storage.memory().lastKnownPosition.heading.toDouble()-Math.PI);}
            else {offset = Radians.of(-Storage.memory().lastKnownPosition.heading.toDouble()+Math.PI/2);}
        }

        driveTask.setFieldCentricOffset(offset);*/

        driver().whenPressed(Controls.A)
            .run(driveTask::resetFieldCentricOrigin);
        robot.drive.setDefaultTask(driveTask);
        robot.intake.setDefaultTask(robot.intake.tasks.control(() -> -gamepad2.lsy));
        robot.lift.setDefaultTask(robot.lift.tasks.control(() -> -gamepad2.rsy));
    }

    @Override
    protected void periodic() {
        robot.hw.hook.setPower(gamepad2.dpad_up ? 1 : gamepad2.dpad_down ? -1 : 0);
        robot.hw.spintake.setPower(gamepad2.left_trigger - gamepad2.right_trigger);
        telemetry.addData("lift current position", robot.hw.liftMotor.getCurrentPosition());
        telemetry.addData("lift target position", robot.hw.liftMotor.getTargetPosition());
        telemetry.addData("lift power", robot.hw.liftMotor.getPower());
    }
}