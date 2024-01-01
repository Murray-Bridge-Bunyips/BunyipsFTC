package org.murraybridgebunyips.bunyipslib

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline

/**
 * Wrapper for OpenFTC's EasyOpenCV library.
 *
 * It is better recommended to use the Vision class instead, as it will use the SDKs libraries which
 * are more reliable and have more interoperability. This wrapper should only be used if
 * absolutely necessary (e.g. if you need to use OpenCV directly and the SDK is not enough)
 *
 * Note that making a class that extends VisionProcessor will give you access to OpenCV pipeline methods,
 * and these can be attached to the VisionPortal in Vision. This will give you access to everything you need.
 *
 * @author Lucas Bubner, 2023
 * @deprecated Use Vision instead
 */
@Deprecated("Use Vision instead")
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