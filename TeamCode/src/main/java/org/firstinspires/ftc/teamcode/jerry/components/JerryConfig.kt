package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.common.RobotConfig

/**
 * Jerry robot configuration and hardware declarations.
 */
class JerryConfig(override var hardwareMap: HardwareMap) : RobotConfig() {
    // Add declarations here
    var webcam: WebcamName? = null
    var monitorID: Int? = null
    var bl: DcMotorEx? = null
    var br: DcMotorEx? = null
    var fl: DcMotorEx? = null
    var fr: DcMotorEx? = null
    var claw: Servo? = null
    var arm1: DcMotorEx? = null
    var arm2: DcMotorEx? = null
    var imu: BNO055IMU? = null
    var limit: TouchSensor? = null

    override fun init() {
        webcam = getHardware("Webcam", WebcamName::class.java) as? WebcamName
        monitorID = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
        )
        bl = getHardware("Back Left", DcMotorEx::class.java) as? DcMotorEx
        br = getHardware("Back Right", DcMotorEx::class.java) as? DcMotorEx

        // Deadwheels attached as an encoder on the front two motors
        // X encoder = port 3, Front Left
        fl = getHardware("Front Left", DcMotorEx::class.java) as? DcMotorEx
        // Y encoder = port 4, Front Right
        fr = getHardware("Front Right", DcMotorEx::class.java) as? DcMotorEx

        arm1 = getHardware("Arm Motor 1", DcMotorEx::class.java) as? DcMotorEx
        arm2 = getHardware("Arm Motor 2", DcMotorEx::class.java) as? DcMotorEx
        claw = getHardware("Claw Servo", Servo::class.java) as? Servo
        limit = getHardware("Arm Stop", TouchSensor::class.java) as? TouchSensor

        // Motor direction configuration
        fl?.direction = Direction.REVERSE
        fr?.direction = Direction.FORWARD
        bl?.direction = Direction.FORWARD
        br?.direction = Direction.REVERSE

        // Control Hub IMU configuration
        // This uses the legacy methods for IMU initialisation, this should be refactored and updated
        // at some point in time. (23 Nov 2022)
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.loggingEnabled = true
        parameters.loggingTag = "IMU"
        parameters.accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        imu = getHardware("imu", BNO055IMU::class.java) as BNO055IMU
        imu?.initialize(parameters)
    }

    companion object {
        fun newConfig(hardwareMap: HardwareMap): JerryConfig {
            val config = JerryConfig(hardwareMap)
            config.init()
            return config
        }
    }
}