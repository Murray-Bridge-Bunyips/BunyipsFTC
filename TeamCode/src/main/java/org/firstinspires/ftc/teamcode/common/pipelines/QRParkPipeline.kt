package org.firstinspires.ftc.teamcode.common.pipelines

import org.firstinspires.ftc.teamcode.common.tasks.GetQRSleeveTask
import org.opencv.core.Mat
import org.opencv.objdetect.QRCodeDetector
import org.openftc.easyopencv.OpenCvPipeline

/**
 * Utilises the OpenCV detection for QR codes in order to determine parking position.
 * Simple, effective, and intelligent. It may take a few seconds to detect a QR code.
 * @author Lucas Bubner - FTC 15215 Captain; Nov 2022
 */
class QRParkPipeline : OpenCvPipeline() {
    @Volatile
    var position: GetQRSleeveTask.ParkingPosition? = null
        private set

    // Using OpenCVs object detection for QR codes
    var qr = QRCodeDetector()
    override fun processFrame(input: Mat): Mat {

        // Must use a curved decoder, as the cone is a curved surface
        val result = qr.detectAndDecodeCurved(input)
        when (result) {
            "MURRAY" -> position = GetQRSleeveTask.ParkingPosition.LEFT
            "BRIDGE" -> position = GetQRSleeveTask.ParkingPosition.CENTER
            "BUNYIPS" -> position = GetQRSleeveTask.ParkingPosition.RIGHT
        }
        return input
    }
}