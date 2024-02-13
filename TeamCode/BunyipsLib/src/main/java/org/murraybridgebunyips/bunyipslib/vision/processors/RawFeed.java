package org.murraybridgebunyips.bunyipslib.vision.processors;

import android.graphics.Canvas;

import org.murraybridgebunyips.bunyipslib.vision.Processor;
import org.murraybridgebunyips.bunyipslib.vision.data.VisionData;
import org.opencv.core.Mat;

/**
 * Raw Feed processor. To use this pass Vision.raw as an instance, you do not need to
 * construct this processor manually.
 */
public class RawFeed extends Processor<VisionData> {
    @Override
    public String getName() {
        return "rawfeed";
    }

    @Override
    public void update() {
        // no-op
    }

    @Override
    public Object onProcessFrame(Mat frame, long captureTimeNanos) {
        return frame;
    }

    @Override
    public void onFrameDraw(Canvas canvas) {
        // no-op
    }
}
