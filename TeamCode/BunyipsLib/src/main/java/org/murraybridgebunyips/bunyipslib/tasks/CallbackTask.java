package org.murraybridgebunyips.bunyipslib.tasks;

import androidx.annotation.NonNull;

import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;

public class CallbackTask extends RunOnceTask {
    private final Runnable callback;

    public CallbackTask(@NonNull BunyipsOpMode opMode, Runnable callback) {
        super(opMode);
        this.callback = callback;
    }

    @Override
    public void runOnce() {
        callback.run();
    }
}
