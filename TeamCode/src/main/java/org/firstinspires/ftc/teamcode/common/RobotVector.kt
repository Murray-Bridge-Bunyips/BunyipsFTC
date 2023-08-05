package org.firstinspires.ftc.teamcode.common

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Definitions of directional vectors in the format (x, y, r).
 * We don't have to define (z) as 3D space is irrelevant in this context.
 */
data class RobotVector(var x: Double, var y: Double, var r: Double) {
    // Convert vectors to angles for use in IMU by calculating tan^-1(y/x)
    val angle: Double
        get() = Math.toDegrees(atan2(y, x))

    /**
     * Add two vectors together.
     */
    operator fun plus(other: RobotVector): RobotVector {
        return RobotVector(x + other.x, y + other.y, r + other.r)
    }

    /**
     * Flip the vector to be of opposite magnitude.
     */
    fun flip() {
        x = -x
        y = -y
        r = -r
    }

    /**
     * Rotate the vector 90 degrees clockwise.
     */
    fun right() {
        x = y
        y = -x
    }

    /**
     * Rotate the vector 90 degrees anticlockwise.
     */
    fun left() {
        x = -y
        y = x
    }

    /**
     * Normalise the vector by a given magnitude.
     */
    fun normalise(mag: Double) {
        val magnitude = mag / sqrt(x * x + y * y)
        x *= magnitude
        y *= magnitude
    }

    companion object {
        /**
         * Calculate the robot XY vector from an angle.
         * @param angle Angle to calculate vector from.
         * @param type Unit of angle.
         */
        fun calcPolar(angle: Double, type: AngleUnit): RobotVector {
            var rad = angle
            if (type == AngleUnit.DEGREES) {
                rad = Math.toRadians(angle)
            }

            // Calculated using unit circle trigonometry, this was originally implemented
            // alongside the drive systems but has been moved here for clarity and during transformation
            // to vector-based navigation.
            val x = cos(rad)
            val y = sin(rad)

            // View visualisation for polar to cartesian conversion:
            // https://www.desmos.com/calculator/py4u0xwrc8
            return RobotVector(x, y, 0.0)
        }
    }
}
