package au.edu.sa.mbhs.studentrobotics.ftc24736.joker.teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.ftc24736.joker.Joker;

/**
 * "Whoooaaa! Looking cool, Joker!"
 */
@TeleOp(name = "TeleOp")
public class TeleOperationCommandBASED extends CommandBasedBunyipsOpMode {
    private final Joker robot = new Joker();

    @Override
    protected void onInitialise() {
        robot.init();
        robot.lights.setPattern(RevBlinkinLedDriver.BlinkinPattern.LAWN_GREEN);
    }

    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.RIGHT_BUMPER)
            .run(robot.outtakeGrip.tasks.toggle());

        robot.ascentArm.setDefaultTask(robot.ascentArm.tasks.control(() -> gamepad2.dpad_left ? -0.4 : gamepad2.dpad_right ? 0.4 : 0));

        FieldOrientableDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.drive).withFieldCentric(() -> true);
        driver().whenPressed(Controls.A)
            .run(driveTask::resetFieldCentricOrigin);
        robot.drive.setDefaultTask(driveTask);
        robot.intake.setDefaultTask(robot.intake.tasks.control(() -> gamepad2.lsy));
        robot.lift.setDefaultTask(robot.lift.tasks.control(() -> gamepad2.rsy));

    }

    @Override
    protected void periodic() {
        robot.hw.hook.setPower(gamepad2.dpad_up ? 1 : gamepad2.dpad_down ? -1 : 0);
        robot.hw.spintake.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
    }
}
