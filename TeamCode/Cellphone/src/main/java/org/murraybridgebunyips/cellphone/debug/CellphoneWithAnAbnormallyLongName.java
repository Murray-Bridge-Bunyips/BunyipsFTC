package org.murraybridgebunyips.cellphone.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Minutes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.BunyipsSubsystem;
import org.murraybridgebunyips.bunyipslib.CommandBasedBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.tasks.RunTask;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.cellphone.components.CellphoneConfig;

/**
 * Scheduler testing.
 */
@TeleOp
public class CellphoneWithAnAbnormallyLongName extends CommandBasedBunyipsOpMode {
    private final CellphoneConfig config = new CellphoneConfig();
    private TestSubsystem s;

    @Override
    protected void onInitialise() {
        config.init();
        s = new TestSubsystem();
        addSubsystems(s);
//        try (Storage.Filesystem f = Storage.filesystem()) {
//        }

//        setInitTask(new MessageTask(Seconds.of(2), "hello world"));
//        throw new YourCodeSucksException();
    }

    @Override
    protected void assignCommands() {
        scheduler().always().run(() -> telemetry.add("hi"));
        // self destruct in 1 minute
        s.setDefaultTask(new WaitTask(Minutes.of(1)));
        driver().whenReleased(Controls.BACK).run(new RunTask(() -> {}, s, false).withName("TaskTask").withTimeout(Minutes.of(2)));
        driver().whenHeld(Controls.LEFT_STICK_BUTTON).run(() -> telemetry.add("left stick button")).in(Minutes.of(60));
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
