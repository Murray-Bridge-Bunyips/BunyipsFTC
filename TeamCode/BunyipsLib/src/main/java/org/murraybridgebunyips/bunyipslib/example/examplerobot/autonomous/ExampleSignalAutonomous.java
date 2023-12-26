package org.murraybridgebunyips.bunyipslib.example.examplerobot.autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.OpenCVCam;
import org.murraybridgebunyips.bunyipslib.example.examplerobot.components.ExampleConfig;
import org.murraybridgebunyips.bunyipslib.tasks.AutoTask;
import org.murraybridgebunyips.bunyipslib.tasks.GetSignalTask;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;

import java.util.List;

/**
 * Autonomous example of using POWERPLAY AprilTag detection to determine the parking position.
 */
public class ExampleSignalAutonomous extends AutonomousBunyipsOpMode {
    private final ExampleConfig config = new ExampleConfig();
    private GetSignalTask initTask;
    @SuppressWarnings("FieldCanBeLocal")
    private OpenCVCam cam;

    @Override
    protected void onInitialisation() {
        config.init(this);
        cam = new OpenCVCam(this, config.webcam, null);
        initTask = new GetSignalTask(this, cam);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return null;
    }

    @Override
    protected AutoTask setInitTask() {
        // Will run this task until it is complete, then move on to onInitDone(), or will terminate
        // once the OpMode is started.
        return initTask;
    }

    @Override
    protected void onInitDone() {
        // Can access initTask.getPosition() here
        // e.g
        if (initTask.getPosition() == GetSignalTask.ParkingPosition.CENTER) {
            // Do something. Note that the first and last variants of the addTask method respect
            // the asynchronous nature of onQueueReady, and will be queued appropriately.
            addTaskFirst(new WaitTask(this, 5.0));
        }
    }

    @Override
    protected void onQueueReady(OpModeSelection selectedOpMode) {
        addTask(new WaitTask(this, 5.0));
    }
}
