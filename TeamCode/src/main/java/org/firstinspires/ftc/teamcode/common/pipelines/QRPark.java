package org.firstinspires.ftc.teamcode.common.pipelines;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * Utilises the OpenCV detection for QR codes in order to determine parking position.
 * Simple, effective, and intelligent. It may take a few seconds to detect a QR code.
 * @author Lucas Bubner - FTC 15215 Captain; Nov 2022
 */
public class QRPark extends OpenCvPipeline {

    private volatile ParkingPosition parkingPosition = null;
    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    public ParkingPosition getPosition() {
        return parkingPosition;
    }

    // Using OpenCVs object detection for QR codes
    QRCodeDetector qr = new QRCodeDetector();

    @Override
    public Mat processFrame(Mat input) {

        // Must use a curved decoder, as the cone is a curved surface
        String result = qr.detectAndDecodeCurved(input);

        // Check the QR code to see what it is
        // If it matches the strings, then set parking position and the task running this pipeline
        // should detect this change by calling getPosition();
        switch (result) {
            case "MURRAY": // 1
                parkingPosition = ParkingPosition.LEFT;
                break;
            case "BRIDGE": // 2
                parkingPosition = ParkingPosition.CENTER;
                break;
            case "BUNYIPS": // 3
                parkingPosition = ParkingPosition.RIGHT;
                break;
        }
        return input;
    }
}
