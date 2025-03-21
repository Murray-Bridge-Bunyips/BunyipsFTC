package org.firstinspires.ftc.teamcode.teleop;

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
        Threads.start("sel", new UserSelection<>((m) -> FC = m == null || m.equals("FIELD-CENTRIC")));
        setInitTask(Task.task().isFinished(() -> !Threads.isRunning("sel")));
        gamepad1.set(Controls.AnalogGroup.STICKS, UnaryFunction.SQUARE_KEEP_SIGN);
    }

    @Override
    protected void assignCommands() {
        new HolonomicDriveTask(gamepad1, robot.simpleDrive).withFieldCentric(() -> FC).setAsDefaultTask();
        robot.shoulder.tasks.control(() -> -gamepad2.lsy).setAsDefaultTask();
        robot.elbow.tasks.control(() -> -gamepad2.rsy).setAsDefaultTask();

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
