package org.firstinspires.ftc.teamcode.common.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.CameraOp.CamMode
import org.firstinspires.ftc.teamcode.common.pipelines.QRParkPipeline
import org.firstinspires.ftc.teamcode.jerry.components.JerryArm

class GetQRSleeveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val cam: CameraOp,
    private val arm: JerryArm
) : Task(opMode, time), TaskImpl {
    private var qr: QRParkPipeline? = null

    @Volatile
    var position: ParkingPosition? = null

    enum class ParkingPosition {
        LEFT, CENTER, RIGHT
    }

    override fun init() {
        super.init()
        // Make sure camera is in OpenCV mode
        if (cam.mode != CamMode.OPENCV) cam.swapModes()

        // Setup the pipeline for operation
        qr = QRParkPipeline()
        cam.setPipeline(qr)
        arm.liftSetPower(0.4)
        arm.liftSetPosition(2)
    }

    override fun run() {
        // Null case needs to be done by the opMode, do this by checking the result if it is null,
        // and select a custom result based on the default (null) result
        if (isFinished()) return
        arm.update()

        // Try to get the position of the sleeve using QR codes
        // The string to parking position conversion is done by the pipeline
        val result = qr?.position
        opMode.telemetry.addLine("Waiting for QR Code detection... Currently seeing: $result")
        opMode.telemetry.update()
        if (result != null) {
            taskFinished = true
            return
        }
    }
}