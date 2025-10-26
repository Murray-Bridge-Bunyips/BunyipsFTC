package org.firstinspires.ftc.teamcode.debug;

import static au.edu.sa.mbhs.studentrobotics.bunyipslib.external.units.Units.Minutes;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls.Analog.*;
import static au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Task.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsSubsystem;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.Scheduler;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.WaitTask;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.tasks.bases.Lambda;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.transforms.Controls;


/**
 * Scheduler testing.
 */
@TeleOp
public class CellphoneWithAnAbnormallyLongName extends BunyipsOpMode {
    private TestSubsystem s;

    @Override
    protected void onInit() {
        s = new TestSubsystem();
//        try (Storage.Filesystem f = Storage.filesystem()) {
//        }

//        setInitTask(new MessageTask(Seconds.of(2), "hello world"));
//        throw new YourCodeSucksException();
        on(this::opModeIsActive).onTrue(() -> telemetry.add("hi"));
        // self destruct in 1 minute
        s.setDefaultTask(new WaitTask(Minutes.of(1)));
        gamepad1().button(BACK).onFalse(new Lambda(() -> {
        }).named("TaskTask").timeout(Minutes.of(2)));
        gamepad1().button(LEFT_STICK_BUTTON).withActiveDelay(Minutes.of(60))
                .whileTrue(new Lambda(() -> telemetry.add("left stick button")).repeatedly());
    }

    @Override
    protected void activeLoop() {
        Scheduler.update();
    }

    private static class YourCodeSucksException extends RuntimeException {
        public YourCodeSucksException() {
            super("your code is bad and have been reported to the authority");
        }
    }

    static class TestSubsystem extends BunyipsSubsystem {
        public TestSubsystem() {
//            assertParamsNotNull((Object) null);
        }

        @Override
        protected void periodic() {

        }
    }
}
