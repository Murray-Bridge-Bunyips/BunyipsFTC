package org.firstinspires.ftc.teamcode.common

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation

/**
 * IMUOperation custom common class for internal BNO055IMUs
 * @author Lucas Bubner - FTC 15215 Captain; Oct 2022 - Murray Bridge Bunyips
 */
class IMUOp(opMode: BunyipsOpMode?, private val imu: BNO055IMU?) : BunyipsComponent(opMode) {
    @Volatile
    var currentAngles: Orientation? = null
    private var previousHeading = 0.0

    /**
     * Get the current heading reading from the internal IMU, with support for 360 degree readings
     * Instead of using Euler readings, this will return a number within 0 to 360
     * @return Z value of Orientation axes in human-friendly reading range [0, 360]
     */
    var heading = 0.0
        get() {
            val currentHeading = currentAngles!!.thirdAngle.toDouble()
            var delta = currentHeading - previousHeading

            // Detects if there is a sudden 180 turn which means we have turned more than the 180
            // degree threshold. Adds 360 to additively inverse the value and give us a proper delta
            if (delta < -180) {
                delta += 360.0
            } else if (delta >= 180) {
                delta -= 360.0
            }
            field += delta
            previousHeading = currentHeading

            // Limit heading readings to only be in a (0, 360) index
            if (field >= 360.0) {
                field -= 360.0
            } else if (field < 0.0) {
                field += 360.0
            }
            return field
        }
        private set
    private var capture: Double? = null

    init {
        assert(imu != null)
    }

    /**
     * Update the latest state in the IMU to current data
     */
    fun tick() {
        currentAngles = imu?.getAngularOrientation(
            AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES
        )
    }

    /**
     * Get the current roll reading from the internal IMU
     * @return Y value of Orientation axes
     */
    val roll: Double
        get() = currentAngles!!.secondAngle.toDouble()

    /**
     * Get the current pitch reading from the internal IMU
     * @return X value of Orientation axes
     */
    val pitch: Double
        get() = currentAngles!!.firstAngle.toDouble()

    /**
     * Start PrecisionDrive IMU alignment algorithm and capture the original angle
     */
    fun startCapture() {
        tick()
        capture = heading
    }

    /**
     * Stop and reset PrecisionDrive IMU alignment algorithm
     */
    fun resetCapture() {
        capture = null
    }

    /**
     * Query motor alignment speed for ROTATIONAL speed through PrecisionDrive
     * @param original_speed supply the intended speed for the R SPEED value
     * @return queried speed based on parameters given, returns the unaltered speed if PrecisionDrive is not online
     */
    fun getRPrecisionSpeed(original_speed: Double, tolerance: Int): Double {
        if (capture == null) return original_speed
        val current = heading
        if (current < capture!! - tolerance) return Math.abs(original_speed + 0.1)
        return if (current > capture!! + tolerance) Math.abs(original_speed - 0.1) else original_speed
    }
}