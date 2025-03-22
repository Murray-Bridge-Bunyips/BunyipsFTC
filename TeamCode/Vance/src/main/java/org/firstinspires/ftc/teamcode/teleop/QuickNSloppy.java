package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.UnaryFunction;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Threads;

/**
 * the scrimmage is like now so i need to write fast
 * arhahahaha
 */
@Config
@TeleOp
public class QuickNSloppy extends CommandBasedBunyipsOpMode {
    /**
     * Field-centric mode.
     */
    public static boolean FC = true;
    private final Vance robot = new Vance();

    @Override
    protected void onInitialise() {
        robot.init();
        setInitTask(Task.task()
                .init(() -> Threads.start("sel",
                        new UserSelection<>((m) -> FC = m == null || m.equals("FIELD-CENTRIC"))))
                .isFinished(() -> !Threads.isRunning("sel")));
        gamepad1.set(Controls.AnalogGroup.STICKS, UnaryFunction.SQUARE_KEEP_SIGN);
    }

    @Override
    protected void assignCommands() {
        HolonomicDriveTask driveTask = new HolonomicDriveTask(gamepad1, robot.simpleDrive);
        driveTask.withFieldCentric(() -> FC).setAsDefaultTask();
        driver().whenPressed(Controls.A)
                .run("Reset Field Centric Origin", driveTask::resetFieldCentricOrigin);

        robot.shoulder.tasks.control(() -> -gamepad2.lsy).setAsDefaultTask();
        robot.elbow.tasks.control(() -> -gamepad2.rsy).setAsDefaultTask();

        operator().whenPressed(Controls.DPAD_UP)
                .run(robot.elbow.tasks.goTo(-60).with(robot.shoulder.tasks.goTo(550)))
                .finishIf(() -> !gamepad2.atRest());

        // this kinda destroyed the intake a day before competition, sorry giulio and your team
//        operator().whenPressed(Controls.DPAD_DOWN)
//                .run(robot.elbow.tasks.goTo(-155).with(robot.shoulder.tasks.goTo(0)))
//                .finishIf(() -> !gamepad2.atRest());

        operator().whenPressed(Controls.BACK)
                .run(() -> {
                    robot.hw.elbow.resetEncoder();
                    robot.hw.shoulder.resetEncoder();
                });
    }

    @Override
    protected void periodic() {
        robot.hw.intake.setPower(gamepad2.a ? 1 : gamepad2.y ? -1 : 0);
    }
}
