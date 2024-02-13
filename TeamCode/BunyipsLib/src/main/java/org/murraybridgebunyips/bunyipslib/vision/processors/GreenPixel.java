package org.murraybridgebunyips.bunyipslib.vision.processors;

import com.acmerobotics.dashboard.config.Config;

import org.opencv.core.Scalar;

@Config
public class GreenPixel extends YCbCrColourThreshold {
    public static double LOWER_Y = 0.0;
    public static double LOWER_CB = 0.0;
    public static double LOWER_CR = 0.0;
    public static double UPPER_Y = 255.0;
    public static double UPPER_CB = 147.3;
    public static double UPPER_CR = 111.9;

    @Override
    public String getName() {
        return "greenpixel";
    }

    @Override
    public Scalar getLower() {
        return new Scalar(LOWER_Y, LOWER_CB, LOWER_CR);
    }

    @Override
    public Scalar getUpper() {
        return new Scalar(UPPER_Y, UPPER_CB, UPPER_CR);
    }
}
