package org.firstinspires.ftc.teamcode;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Seconds;

import com.acmerobotics.dashboard.config.Config;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.UnaryFunction;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicVectorDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Threads;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends CommandBasedBunyipsOpMode {
    public static boolean FIELD_CENTRIC_ENABLED = true;

    private final Vance vance = Vance.instance;

    @Override
    protected void onInitialise() {
        UserSelection<String> fieldCentricSelector = new UserSelection<>(
                (s) -> FIELD_CENTRIC_ENABLED = s == null || s.equals("FIELD-CENTRIC"), "ROBOT-CENTRIC", "FIELD-CENTRIC")
                .captionLayer(0, "SELECT DRIVE MODE");
        setInitTask(Task.task()
                .init(() -> Threads.start("drive selector", fieldCentricSelector))
                .isFinished(() -> !Threads.isRunning(fieldCentricSelector)));
        gamepad1.set(Controls.AnalogGroup.STICKS, UnaryFunction.SQUARE_KEEP_SIGN);
    }

//giulio is not here and giulio is very annoying and he definitely typed this and it totally was not madison. Burger = burger

    @Override
    protected void assignCommands() {
        HolonomicVectorDriveTask hvdt = new HolonomicVectorDriveTask(gamepad1, vance.drive);
        hvdt.withFieldCentric(() -> FIELD_CENTRIC_ENABLED).setAsDefaultTask();
        driver().whenPressed(Controls.A)
                .run(hvdt::resetFieldCentricOrigin);

        operator().whenPressed(Controls.Y)
                .run(vance.intake.tasks.run(Vance.EJECT).asPriority())
                .finishIfButtonRetriggered();
        operator().whenPressed(Controls.A)
                .run(vance.intake.tasks.run(Vance.INTAKE).asPriority())
                .finishIfButtonRetriggered();
        operator().when(Controls.Analog.RIGHT_TRIGGER, (v) -> v == 1)
                .run(vance.shoulder.tasks.home().then(vance.elbow.tasks.home()));
        // TODO: driver assisted controls here
        operator().whenPressed(Controls.B)
                .run(vance.shoulder.tasks.goToProfiled(0).timeout(Seconds.of(2)).then(vance.elbow.tasks.goToProfiled(150)));
//        operator().whenPressed(Controls.X)
//                .run(vance.elbow.tasks.goToProfiled(150));
        vance.shoulder.tasks.control(() -> -gamepad2.lsy).setAsDefaultTask();
        vance.elbow.tasks.control(() -> -gamepad2.rsy).setAsDefaultTask();
    }
}
