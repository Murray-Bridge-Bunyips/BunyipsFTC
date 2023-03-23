package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.common.Deadwheel
import org.firstinspires.ftc.teamcode.common.RobotConfig

class JerryConfig : RobotConfig() {
    // Add declarations here
    var webcam: WebcamName? = null
    var monitorID = 0
    var bl: DcMotorEx? = null
    var br: DcMotorEx? = null
    var fl: DcMotorEx? = null
    var fr: DcMotorEx? = null
    var x: Deadwheel? = null
    var y: Deadwheel? = null
    var claw1: Servo? = null
    var claw2: Servo? = null
    var arm1: DcMotorEx? = null
    var arm2: DcMotorEx? = null
    var imu: BNO055IMU? = null
    var limit: TouchSensor? = null

    override fun init(hardwareMap: HardwareMap?, telemetry: Telemetry) {
        setTelemetry(telemetry)
        this.hardwareMap = hardwareMap
        webcam = getHardware("Webcam", WebcamName::class.java) as? WebcamName
        monitorID = hardwareMap!!.appContext.resources.getIdentifier(
            "cameraMonitorViewId", "id", hardwareMap.appContext.packageName
        )
        bl = getHardware("Back Left", DcMotorEx::class.java) as? DcMotorEx
        br = getHardware("Back Right", DcMotorEx::class.java) as? DcMotorEx
        fl = getHardware("Front Left", DcMotorEx::class.java) as? DcMotorEx
        fr = getHardware("Front Right", DcMotorEx::class.java) as? DcMotorEx
        arm1 = getHardware("Arm Motor 1", DcMotorEx::class.java) as? DcMotorEx
        arm2 = getHardware("Arm Motor 2", DcMotorEx::class.java) as? DcMotorEx
        claw1 = getHardware("Claw Servo 1", Servo::class.java) as? Servo
        claw2 = getHardware("Claw Servo 2", Servo::class.java) as? Servo
        limit = getHardware("Arm Stop", TouchSensor::class.java) as? TouchSensor

        // Motor direction configuration
        fl?.direction = Direction.REVERSE
        fr?.direction = Direction.FORWARD
        bl?.direction = Direction.FORWARD
        br?.direction = Direction.REVERSE

        // Encoder configuration (Using modified DcMotor classes with built-in distance calculations)
        // These encoders will mirror a DcMotor, but will be attached to their own port (for example,
        // motor 0 and 1 on Expansion Hub, but without any power connection)
        x = getHardware("X Encoder", Deadwheel::class.java) as? Deadwheel
        y = getHardware("Y Encoder", Deadwheel::class.java) as? Deadwheel


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
        printHardwareErrors()
    }

    // Deadwheel specific functions
    fun areDeadwheelsAvail(): Boolean {
        return x != null && y != null
    }

    fun resetDeadwheels() {
        x?.resetTracking()
        y?.resetTracking()
    }

    fun startDeadwheels() {
        x?.enableTracking()
        y?.enableTracking()
    }

    fun stopDeadwheels() {
        x?.disableTracking()
        y?.disableTracking()
    }

    companion object {
        fun newConfig(hardwareMap: HardwareMap?, telemetry: Telemetry): JerryConfig {
            val config = JerryConfig()
            config.init(hardwareMap, telemetry)
            return config
        }
    }
}