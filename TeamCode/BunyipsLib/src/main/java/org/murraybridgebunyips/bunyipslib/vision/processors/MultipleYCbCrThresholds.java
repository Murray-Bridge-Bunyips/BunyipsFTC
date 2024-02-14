package org.murraybridgebunyips.bunyipslib.vision.processors;

import android.graphics.Canvas;

import org.murraybridgebunyips.bunyipslib.vision.Processor;
import org.murraybridgebunyips.bunyipslib.vision.data.ContourData;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleYCbCrThresholds extends Processor<ContourData> {
    private final List<YCbCrColourThreshold> colourProcessors = new ArrayList<>();
    private final List<Mat> mats = new ArrayList<>();

    public MultipleYCbCrThresholds(YCbCrColourThreshold... thresholdProcessors) {
        colourProcessors.addAll(Arrays.asList(thresholdProcessors));
        for (YCbCrColourThreshold processor : colourProcessors) {
            mats.add(new Mat());
        }
    }

    @Override
    public String getName() {
        return "allpixels";
    }

    @Override
    public void update() {
        for (YCbCrColourThreshold processor : colourProcessors) {
            processor.update();
            data.addAll(processor.getData());
        }
    }

    @Override
    public Object onProcessFrame(Mat frame, long captureTimeNanos) {
        mats.replaceAll(old -> frame.clone());
        for (int i = 0; i < colourProcessors.size(); i++) {
            colourProcessors.get(i)
                    .onProcessFrame(mats.get(i), captureTimeNanos);
        }
        Core.merge(mats, frame);
        return frame;
    }

    @Override
    public void onFrameDraw(Canvas canvas) {
        for (YCbCrColourThreshold processor : colourProcessors) {
            processor.onFrameDraw(canvas);
        }
    }
}
