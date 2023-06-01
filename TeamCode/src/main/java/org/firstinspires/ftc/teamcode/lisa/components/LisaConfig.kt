package org.firstinspires.ftc.teamcode.lisa.components

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.common.RobotConfig

/**
 * Robot configuration for high-speed minibot Lisa.
 */
class LisaConfig(override var hardwareMap: HardwareMap) : RobotConfig() {
    var left: DcMotorEx? = null
    var right: DcMotorEx? = null
    var imu: BNO055IMU? = null
    override fun init() {
        left = getHardware("Left Motor", DcMotorEx::class.java) as DcMotorEx
        right = getHardware("Right Motor", DcMotorEx::class.java) as DcMotorEx
        right?.direction = Direction.REVERSE

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
        if (imu != null) imu?.initialize(parameters)
    }

    companion object {
        fun newConfig(hardwareMap: HardwareMap): LisaConfig {
            val config = LisaConfig(hardwareMap)
            config.init()
            return config
        }
    }
}