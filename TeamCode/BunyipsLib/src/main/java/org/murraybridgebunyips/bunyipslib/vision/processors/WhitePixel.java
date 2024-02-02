package org.murraybridgebunyips.bunyipslib.vision.processors;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.murraybridgebunyips.bunyipslib.vision.Processor;
import org.murraybridgebunyips.bunyipslib.vision.data.ContourData;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class WhitePixel extends YCbCrColourThreshold {

    @Override
    public String getName() {
        return "whitepixel";
    }

    @Override
    public Scalar getLower() {
        return new Scalar(192.7, 123.3, 106.3);
    }

    @Override
    public Scalar getUpper() {
        return new Scalar(255, 255, 255);
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // noop
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        // noop
    }
}
