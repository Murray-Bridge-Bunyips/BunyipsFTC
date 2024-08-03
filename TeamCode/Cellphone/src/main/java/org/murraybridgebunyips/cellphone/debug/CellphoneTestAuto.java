package org.murraybridgebunyips.cellphone.debug;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.Milliseconds;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.bunyipslib.tasks.groups.ParallelTaskGroup;

/**
 * Test batch of 100 WaitTasks.
 */
@Autonomous
public class CellphoneTestAuto extends AutonomousBunyipsOpMode {
    @Override
    protected void onInitialise() {
//        setOpModes("Test Auto", "Test Auto 2", "3,", "4", "5", "6", "7", "8", "9", "10", "18", "19", "20");
        setOpModes(StartingPositions.use());
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        for (int i = 0; i < 100; i++) {
            addTask(new WaitTask(Milliseconds.of(100)).withName("WaitTask no. " + i));
        }
        addTask(new ParallelTaskGroup(
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000)),
                new WaitTask(Milliseconds.of(20000))
        ));
    }
}
