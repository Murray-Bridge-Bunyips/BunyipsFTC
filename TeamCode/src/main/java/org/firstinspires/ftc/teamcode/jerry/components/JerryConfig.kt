package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.common.RobotConfig
import kotlin.properties.Delegates

/**
 * Jerry robot configuration and hardware declarations.
 */
class JerryConfig(override var hardwareMap: HardwareMap) : RobotConfig() {
    var monitorID by Delegates.notNull<Int>()
    var webcam: WebcamName? = null
    var bl: DcMotorEx? = null
    var br: DcMotorEx? = null
    var fl: DcMotorEx? = null
    var fr: DcMotorEx? = null
    var claw: Servo? = null
    var arm1: DcMotorEx? = null
    var arm2: DcMotorEx? = null
    var imu: IMU? = null
    var limit: TouchSensor? = null

    val driveMotors: List<DcMotorEx?>
        get() = listOf(bl, br, fl, fr)

    val armComponents: List<HardwareDevice?>
        get() = listOf(arm1, arm2, claw, limit)

    override val deviceMappings: HashMap<HardwareDevice?, String> = hashMapOf(
        webcam to "Webcam",
        bl to "Back Left",
        br to "Back Right",
        fl to "Front Left",
        fr to "Front Right",
        arm1 to "Arm Motor 1",
        arm2 to "Arm Motor 2",
        claw to "Claw Servo",
        limit to "Arm Stop",
        imu to "ch_imu"
    )

    override fun init() {
        for ((device, deviceName) in deviceMappings) {
            val hardwareDevice = getHardware(deviceName, device?.javaClass)

            // Assign the hardware device to the corresponding variable
            when (device) {
                webcam -> webcam = hardwareDevice as? WebcamName
                bl -> bl = hardwareDevice as? DcMotorEx
                br -> br = hardwareDevice as? DcMotorEx
                fl -> fl = hardwareDevice as? DcMotorEx
                fr -> fr = hardwareDevice as? DcMotorEx
                arm1 -> arm1 = hardwareDevice as? DcMotorEx
                arm2 -> arm2 = hardwareDevice as? DcMotorEx
                claw -> claw = hardwareDevice as? Servo
                limit -> limit = hardwareDevice as? TouchSensor
                imu -> imu = hardwareDevice as? IMU
            }

            // Update mapping with the proper hardware device
            deviceMappings.remove(device)
            deviceMappings[hardwareDevice] = deviceName
        }

        monitorID = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
        )

        /*
           Deadwheels attached as an encoder on the front two motors
           X encoder = port 3, Front Left
           Y encoder = port 4, Front Right
         */

        // Motor direction configuration
        fl?.direction = Direction.REVERSE
        fr?.direction = Direction.FORWARD
        bl?.direction = Direction.FORWARD
        br?.direction = Direction.REVERSE

        // Control Hub IMU configuration
        val parameters = IMU.Parameters(
            RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
            )
        )
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