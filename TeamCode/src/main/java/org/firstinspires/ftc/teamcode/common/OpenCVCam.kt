package org.firstinspires.ftc.teamcode.common

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline

/**
 * Simplified version of CameraOp class to include only OpenCV operations, heavily reducing bloat
 * for OpModes that don't need to use Vuforia or TensorFlow.
 * @author Lucas Bubner, 2023
 */
class OpenCVCam(
    opmode: BunyipsOpMode,
    webcam: WebcamName?,
    // monitorID in this version is optional
    monitorID: Int? = null,
) : BunyipsComponent(opmode) {
    private var instance: OpenCvCamera? = null

    init {
        if (webcam == null) {
            errorHandler()
        }
        instance = if (monitorID != null) {
            OpenCvCameraFactory.getInstance().createWebcam(webcam, monitorID)
        } else {
            OpenCvCameraFactory.getInstance().createWebcam(webcam)
        }
        instance?.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                instance?.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                errorHandler(errorCode)
            }
        })
    }

    private fun errorHandler(code: Int? = null) {
        if (code != null) {
            opMode.addTelemetry("An error occurred in initialising OpenCV. Error code: $code")
        } else {
            opMode.addTelemetry("An error occurred in initialising OpenCV.")
        }
        instance = null
    }

    fun setPipeline(pipeline: OpenCvPipeline) {
        instance?.setPipeline(pipeline)
    }
}