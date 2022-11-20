package org.firstinspires.ftc.teamcode.common.pipelines;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;
import org.openftc.easyopencv.OpenCvPipeline;

// This pipeline might be unreliable, as tests done on an sleeve prove not accurate.
// It is for this reason that this code is not in use, but still exists for other applications
// Perhaps one day or in another application this will be useful.
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
        String result = qr.detectAndDecode(input);
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