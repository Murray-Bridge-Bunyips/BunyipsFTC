package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.UnaryFunction;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.FieldOrientableDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicVectorDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.DeferredTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Threads;
import org.firstinspires.ftc.teamcode.Vance;

/**
 * TeleOp for Vance.
 *
 * @author Lachlan Paul, 2025
 */
@TeleOp(name = "TeleOp")
@Config
public class VanceTeleOp extends CommandBasedBunyipsOpMode {
    /**
     * Whether to force start the end-game light pattern.
     */
    public static boolean lightForceStart;
    /**
     * Field-centric mode.
     */
    public static boolean FC = true;
    private final Vance robot = new Vance();
    private int shoulderPosIndex = 0;
    private int elbowPosIndex = 0;

    @Override
    protected void onInitialise() {
        robot.init();
        Threads.start("sel", new UserSelection<>((m) -> {
            if (m == null) {
                FC = true;
                return;
            }
            if (m.equals("FIELD-CENTRIC")) {
                FC = true;
            } else if (m.equals("ROBOT-CENTRIC")) {
                FC = false;
            }
        }, "FIELD-CENTRIC", "ROBOT-CENTRIC"));
        setInitTask(Task.task().isFinished(() -> !Threads.isRunning("sel")));
        gamepad1.set(Controls.AnalogGroup.STICKS, UnaryFunction.SQUARE_KEEP_SIGN);
    }
    // giulio is still here
    @Override
    protected void assignCommands() {
        operator().whenPressed(Controls.X)
                .run(robot.intake.tasks.toggle());
//giulio was here he is also java to and he is the best coder here
        // bro someone GE this guy
        // todo: test this goofy stuff
        //  might be inefficient for drivers
        //  or just not work lmao
        //  also very messy tbh
        //  try to DRY it without the weird function from before happening again
        operator().when(Controls.Analog.LEFT_STICK_Y, (v) -> v > 0.5)
                .run(new DeferredTask(() -> robot.shoulder.tasks.goTo(robot.shoulderPositions[(int) Mathf.clamp(shoulderPosIndex++, 0, robot.shoulderPositions.length)])));
        operator().when(Controls.Analog.LEFT_STICK_Y, (v) -> v > -0.5)
                .run(new DeferredTask(() -> robot.shoulder.tasks.goTo(robot.shoulderPositions[(int) Mathf.clamp(shoulderPosIndex--, 0, robot.shoulderPositions.length)])));

        operator().when(Controls.Analog.RIGHT_STICK_Y, (v) -> v > 0.5)
                .run(new DeferredTask(() -> robot.elbow.tasks.goTo(robot.elbowPositions[(int) Mathf.clamp(elbowPosIndex++, 0, robot.elbowPositions.length)])));
        operator().when(Controls.Analog.RIGHT_STICK_Y, (v) -> v < -0.5)
                .run(new DeferredTask(() -> robot.elbow.tasks.goTo(robot.elbowPositions[(int) Mathf.clamp(elbowPosIndex--, 0, robot.elbowPositions.length)])));

        robot.drive.setDefaultTask(new HolonomicVectorDriveTask(gamepad1, robot.drive).withFieldCentric(() -> FC));
        driver().whenPressed(Controls.BACK)
                .run(new HolonomicDriveTask(gamepad1, robot.drive).withFieldCentric(() -> FC))
                .finishIf(() -> gamepad1.getDebounced(Controls.BACK));
        driver().whenPressed(Controls.A)
                .run("Reset FC Offset", () -> Task.cast(robot.drive.getCurrentTask(), FieldOrientableDriveTask.class).resetFieldCentricOrigin());
    }
}