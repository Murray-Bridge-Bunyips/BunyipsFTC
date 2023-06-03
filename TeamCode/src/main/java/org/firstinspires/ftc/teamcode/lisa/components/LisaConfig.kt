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

    override val deviceMappings: HashMap<HardwareDevice?, String> = hashMapOf(
        left to "Left Motor",
        right to "Right Motor",
        imu to "imu"
    )

    override fun init() {
        for ((device, deviceName) in deviceMappings) {
            val hardwareDevice = getHardware(deviceName, device?.javaClass)

            // Assign the hardware device to the corresponding variable
            when (device) {
                left -> left = hardwareDevice as? DcMotorEx
                right -> right = hardwareDevice as? DcMotorEx
                imu -> imu = hardwareDevice as? IMU
            }

            // Update mapping with the proper hardware device
            deviceMappings.remove(device)
            deviceMappings[hardwareDevice] = deviceName
        }

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