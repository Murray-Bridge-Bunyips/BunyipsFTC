package org.murraybridgebunyips.bunyipslib.tasks;

import androidx.annotation.NonNull;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;

/**
 * A task to run a callback before immediately completing.
 * <p>
 * {@code new CallbackTask(this, () -> addTelemetry("Hello world"));}
 */
public class CallbackTask extends RunOnceTask {
    private final Runnable callback;

    public CallbackTask(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void runOnce() {
        callback.run();
    }
}
