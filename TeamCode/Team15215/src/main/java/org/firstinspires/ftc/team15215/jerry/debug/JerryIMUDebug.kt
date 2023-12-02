package org.firstinspires.ftc.team15215.jerry.debug

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.team15215.jerry.components.JerryConfig
import org.murraybridgebunyips.ftc.bunyipslib.BunyipsOpMode
import org.murraybridgebunyips.ftc.bunyipslib.IMUOp

/**
 * Debug opmode for IMU readouts.
 */
@TeleOp(name = "JERRY: IMU Debug", group = "JERRY")
class JerryIMUDebug : BunyipsOpMode() {
    private var config = JerryConfig()
    private var imu: IMUOp? = null

    override fun onInit() {
        config.init(this)
        imu = IMUOp(this, config.imu!!)
        imu?.startCapture()
    }

    override fun activeLoop() {
//        addTelemetry(
//            config?.imu?.getAngularOrientation(
//                AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES
//            )?.thirdAngle.toString()
//        )
        addTelemetry(imu?.heading.toString())
        imu?.tick()
    }
}