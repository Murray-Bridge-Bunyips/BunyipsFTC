package org.firstinspires.ftc.teamcode.lisa.components

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.common.RobotConfig

/**
 * Robot configuration for high-speed minibot Lisa.
 */
class LisaConfig(override var hardwareMap: HardwareMap) : RobotConfig() {
    var left: DcMotorEx? = null
    var right: DcMotorEx? = null
    var imu: IMU? = null

    val motors: List<DcMotorEx?>
        get() = listOf(left, right)

    override fun init() {
        left = getHardware("Left Motor", DcMotorEx::class.java) as DcMotorEx
        right = getHardware("Right Motor", DcMotorEx::class.java) as DcMotorEx
        imu = getHardware("imu", IMU::class.java) as IMU

        right?.direction = Direction.REVERSE

        // Control Hub IMU configuration
        val parameters = IMU.Parameters(
            RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
            )
        )
        imu?.initialize(parameters)
    }

    companion object {
        fun newConfig(hardwareMap: HardwareMap): LisaConfig {
            val config = LisaConfig(hardwareMap)
            config.init()
            return config
        }
    }
}