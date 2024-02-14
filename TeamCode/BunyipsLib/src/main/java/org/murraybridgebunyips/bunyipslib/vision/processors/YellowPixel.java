package org.murraybridgebunyips.bunyipslib.vision.processors;

import com.acmerobotics.dashboard.config.Config;
import org.opencv.core.Scalar;

@Config
public class YellowPixel extends YCbCrColourThreshold {
    public static double LOWER_Y = 0.0;
    public static double LOWER_CB = 0.0;
    public static double LOWER_CR = 0.0;
    public static double UPPER_Y = 255.0;
    public static double UPPER_CB = 255.0;
    public static double UPPER_CR = 82.2;

    @Override
    public String getName() {
        return "yellowpixel";
    }

    @Override
    public Scalar getLower() {
        return new Scalar(LOWER_Y, LOWER_CB, LOWER_CR);
    }

    @Override
    public Scalar getUpper() {
        return new Scalar(UPPER_Y, UPPER_CB, UPPER_CR);
    }

    @Override
    public int getBoxColour() {
        return 0xFFFFFF00;
    }
}
