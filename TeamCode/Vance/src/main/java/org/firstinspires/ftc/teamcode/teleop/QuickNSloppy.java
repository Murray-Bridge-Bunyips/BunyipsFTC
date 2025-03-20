package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Vance;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.UnaryFunction;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
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
    // giulio is still here
    @Override
    protected void assignCommands() {
//        operator().whenPressed(Controls.X)
//                .run(robot.hw.intake);

        new HolonomicDriveTask(gamepad1, robot.simpleDrive).withFieldCentric(() -> FC).setAsDefaultTask();
        robot.shoulder.setDefaultTask(robot.shoulder.tasks.control(() -> -gamepad2.lsy));
        robot.elbow.setDefaultTask(robot.elbow.tasks.control(() -> -gamepad2.rsy));
    }
}
