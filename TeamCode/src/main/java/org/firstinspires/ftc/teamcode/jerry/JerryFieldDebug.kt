package org.firstinspires.ftc.teamcode.jerry

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.CameraOp
import org.firstinspires.ftc.teamcode.common.FieldPositioning
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.jerry.components.JerryConfig

/**
 * Experimental OpMode for testing field positioning
 */
class JerryFieldDebug : BunyipsOpMode() {
    private var config: JerryConfig? = null
    private var field: FieldPositioning? = null
    private var imu: IMUOp? = null
    private var cam: CameraOp? = null

    override fun onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry)
        imu = IMUOp(this, config?.imu)
        cam = CameraOp(this, config?.webcam, config!!.monitorID, CameraOp.CamMode.STANDARD)
        field = FieldPositioning(this, config?.x, config?.y, cam!!, imu!!, FieldPositioning.StartingPositions.BLUE_LEFT, FieldPositioning.StartingDirections.RIGHTWARD)
    }

    override fun activeLoop() {

    }
}