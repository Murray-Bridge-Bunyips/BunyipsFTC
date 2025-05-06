package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.lang.reflect.Field;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsLib;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.CommandBasedBunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.executables.UserSelection;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.UnaryFunction;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Controller;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.HolonomicVectorDriveTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Dbg;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.util.Threads;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends CommandBasedBunyipsOpMode {
    public static boolean FIELD_CENTRIC_ENABLED = true;

    private final Vance vance = Vance.instance;

    @Override
    protected void onInitialise() {
        try {
            // we are hacking. for some reason my controller is seen as a Gamepad instead of a Controller
            // which causes some issues and crashes the code. so for now we just manually set it.
            Field gp1 = OpMode.class.getSuperclass().getDeclaredField("gamepad1");
            Field gp2 = OpMode.class.getSuperclass().getDeclaredField("gamepad2");
            // overrides java safety(scary adn stoopid)
            gp1.setAccessible(true);
            gp2.setAccessible(true);
            gp1.set(BunyipsLib.getOpMode(), gamepad1);
            gp2.set(BunyipsLib.getOpMode(), gamepad2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

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

        Dbg.log(BunyipsLib.getOpMode().gamepad1 instanceof Controller);
        operator().whenPressed(Controls.Y)
                .run(vance.intake.tasks.run(Vance.EJECT).asPriority())
                .finishIfButtonRetriggered();
        operator().whenPressed(Controls.A)
                .run(vance.intake.tasks.run(Vance.INTAKE).asPriority())
                .finishIfButtonRetriggered();
        vance.shoulder.tasks.control(() -> -gamepad2.lsy).setAsDefaultTask();
        vance.elbow.tasks.control(() -> -gamepad2.rsy).setAsDefaultTask();
    }
}
