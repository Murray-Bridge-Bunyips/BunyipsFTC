package org.murraybridgebunyips.bunyipslib.vision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.murraybridgebunyips.bunyipslib.BunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.EmergencyStop;
import org.murraybridgebunyips.bunyipslib.vision.processors.ColourThreshold;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.GreenPixel;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.PurplePixel;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.WhitePixel;
import org.murraybridgebunyips.bunyipslib.vision.processors.centerstage.YellowPixel;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ** This class is WIP and should not be used at the moment. **
 * Manual tuner that allows the user to tune the colour thresholds the camera uses to detect pixels
 * using gamepad1's left analogue stick.
 * <p></p>
 * <p>
 * Threshold Index:<br>
 * 0: lower_y<br>
 * 1: lower_cb<br>
 * 2: lower_cr<br>
 * 3: upper_y<br>
 * 4: upper_cb<br>
 * 5: upper_cr<br>
 * <p></p>
 * <p>
 * Pixel Index:<br>
 * 0: White Pixel<br>
 * 1: Purple Pixel<br>
 * 2: Yellow Pixel<br>
 * 3: Green Pixel<br>
 *
 * <p></p>
 * Controls:<br>
 * dpad_up: Increment selected pixel index<br>
 * dpad_down: Decrement selected pixel index<br>
 * dpad_left: Decrement selected Threshold index<br>
 * dpad_right: Increment selected Threshold index<br>
 * right_bumper(while held): Changes amount Threshold is changed by. Equation is left stick x and y times by 2<br>
 * right_trigger: Saves values (to telem maybe? dunno yet)
 *
 * @author Lachlan Paul, 2024
 */
@TeleOp(name = "Vision Tuner")
@Disabled
public class VisionTuner extends BunyipsOpMode {

    // Make this more generic and use abstraction to pass in processors, this class only works for pixels
    //   and isn't a general contour tuner. It should be able to tune any contour ColourThreshold.
    //   This involves using a ColourThreshold[] instead of the individual pixel variables. and setting scalars
    //   based off the index of the ColourThreshold in the array. This will also reduce the number of variables used.

    int thresholdIndex = 0;
    int pixelIndex = 0;
    double scalarDelta = 0;

    ColourThreshold whitePixel;
    ColourThreshold purplePixel;
    ColourThreshold yellowPixel;
    ColourThreshold greenPixel;
    ColourThreshold currentPixel;

    double lower_y;
    double lower_cb;
    double lower_cr;
    double upper_y;
    double upper_cb;
    double upper_cr;

    ArrayList<ColourThreshold> pixels;
    ArrayList<Double> scalars;

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    @Override
    protected void onInit() {
        try {
            WebcamName webcam = (WebcamName) hardwareMap.get("webcam");
            Vision vision = new Vision(webcam);
            whitePixel = new WhitePixel();
            purplePixel = new PurplePixel();
            yellowPixel = new YellowPixel();
            greenPixel = new GreenPixel();

            // Make it so it doesn't start them all at once.
            //  It's fine for now but for performance's sake.
            vision.init(whitePixel, purplePixel, yellowPixel, greenPixel);
            vision.start(whitePixel, purplePixel, yellowPixel, greenPixel);
            vision.startPreview();

            pixels = new ArrayList<>(Arrays.asList(whitePixel, purplePixel, yellowPixel, greenPixel));
            // By having this array, you will override the values of the next processor when you switch to it,
            //      rather than respecting the current values of the processor you are switching to. Will need to modify
            //      the processor itself as they already store scalar values in setUpper() setLower() getUpper() getLower().
            scalars = new ArrayList<>(Arrays.asList(lower_y, lower_cb, lower_cr, upper_y, upper_cb, upper_cr));
            currentPixel = whitePixel;  // Set to white pixel by default
        } catch (IllegalArgumentException e) {
            throw new EmergencyStop("VisionTuner is missing a webcam called 'webcam'!");
        }
    }

    @Override
    protected void activeLoop() {
        // This code is crap code, hopefully temp code.
        //  It's hard to expand upon, and could be optimised

        if (gamepad1.right_bumper) {
            scalarDelta = (gamepad1.left_stick_y + gamepad1.left_stick_x) / 10;
        } else {
            scalarDelta = 0;
        }

        if (gamepad1.left_bumper) {
            scalars.set(thresholdIndex, 0.0);
        }

        if (gamepad1.a) {
            telemetry.add("Pixel: %", pixels.get(pixelIndex));

            telemetry.add("lower_y: %", scalars.get(0));
            telemetry.add("lower_cb: %", scalars.get(1));
            telemetry.add("lower_cr: %", scalars.get(2));
            telemetry.add("upper_y: %", scalars.get(3));
            telemetry.add("upper_cb: %", scalars.get(4));
            telemetry.add("upper_cr: %", scalars.get(5));
        }

        if (gamepad1.dpad_up && !upPressed && !downPressed) {
            if (pixelIndex == 3) {
                pixelIndex = 0;
            } else {
                pixelIndex++;
            }
        }

        if (gamepad1.dpad_down && !upPressed && !downPressed) {
            if (pixelIndex == 0) {
                pixelIndex = 3;
            } else {
                pixelIndex--;
            }
        }

        if (gamepad1.dpad_left && !leftPressed && !rightPressed) {
            if (thresholdIndex == 0) {
                thresholdIndex = 5;
            } else {
                thresholdIndex--;
            }
        }

        if (gamepad1.dpad_right && !leftPressed && !rightPressed) {
            if (thresholdIndex == 5) {
                thresholdIndex = 0;
            } else {
                thresholdIndex++;
            }
        }

        currentPixel = pixels.get(pixelIndex);
        scalars.set(thresholdIndex, scalars.get(thresholdIndex) + scalarDelta);

        // Makes it so the button presses are only registered once per press.
        upPressed = gamepad1.dpad_up;
        downPressed = gamepad1.dpad_down;
        leftPressed = gamepad1.dpad_left;
        rightPressed = gamepad1.dpad_right;

        if (scalars.get(thresholdIndex) < 0) {
            scalars.set(thresholdIndex, 0.0);
        } else if (scalars.get(thresholdIndex) > 255) {
            scalars.set(thresholdIndex, 255.0);
        }

        // TEST: This is a safety net
        // It checks it earlier up but just in case
        // I also might have misinterpreted the message I got about this
        // Might also be unnecessary with the catch up top
        if (scalarDelta > 0 && scalars.get(thresholdIndex) > 255) {
            currentPixel.setLower(new Scalar(scalars.get(0), scalars.get(1), scalars.get(2)));
            currentPixel.setUpper(new Scalar(scalars.get(3), scalars.get(4), scalars.get(5)));
        }

        telemetry.add("Press A to save results");
        telemetry.add("Current Threshold: %", scalars.get(thresholdIndex));
        telemetry.add("Current Pixel : %", pixels.get(pixelIndex));
        telemetry.add("Current Value to Change Threshold By: %", scalarDelta);
    }
}
