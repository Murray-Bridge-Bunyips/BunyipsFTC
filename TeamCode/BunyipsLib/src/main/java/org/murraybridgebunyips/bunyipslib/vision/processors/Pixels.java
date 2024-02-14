package org.murraybridgebunyips.bunyipslib.vision.processors;

import android.graphics.Canvas;

import org.murraybridgebunyips.bunyipslib.vision.Processor;
import org.murraybridgebunyips.bunyipslib.vision.data.ContourData;
import org.opencv.core.Mat;

public class Pixels extends Processor<ContourData> {
    // TODO: test if we need to make new frames for each processor or if we can stack them
    // if so we can make this a list-based system
    private final WhitePixel whitePixel = new WhitePixel();
    private final PurplePixel purplePixel = new PurplePixel();
    private final YellowPixel yellowPixel = new YellowPixel();
    private final GreenPixel greenPixel = new GreenPixel();

    @Override
    public String getName() {
        return "allpixels";
    }

    @Override
    public void update() {
        whitePixel.update();
        purplePixel.update();
        yellowPixel.update();
        greenPixel.update();
    }

    @Override
    public Object onProcessFrame(Mat frame, long captureTimeNanos) {
        whitePixel.onProcessFrame(frame, captureTimeNanos);
        purplePixel.onProcessFrame(frame, captureTimeNanos);
        yellowPixel.onProcessFrame(frame, captureTimeNanos);
        greenPixel.onProcessFrame(frame, captureTimeNanos);
        return frame;
    }

    @Override
    public void onFrameDraw(Canvas canvas) {
        whitePixel.onFrameDraw(canvas);
        purplePixel.onFrameDraw(canvas);
        yellowPixel.onFrameDraw(canvas);
        greenPixel.onFrameDraw(canvas);
    }
}
