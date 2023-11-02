package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Definitions of directional vectors in the format (x, y, r).
 * We don't have to define (z) as 3D space is irrelevant in this context.
 * @author Lucas Bubner, 2023
 */
data class RobotVector(var x: Double, var y: Double, var r: Double) {
    // Use inverse trig to find a polar angle from a Cartesian quantity
    val angle: Double
        // Robot vector of 0 deg forward is 90 degrees on the unit circle, hence we correct it here
        get() = Math.toDegrees(atan2(y, x)) - 90.0

    /**
     * Add two vectors together.
     */
    operator fun plus(other: RobotVector): RobotVector {
        return RobotVector(
            Range.clip(x + other.x, -1.0, 1.0),
            Range.clip(y + other.y, -1.0, 1.0),
            Range.clip(r + other.r, -1.0, 1.0)
        )
    }

    /**
     * Subtract two vectors from each other.
     */
    operator fun minus(other: RobotVector): RobotVector {
        return RobotVector(
            Range.clip(x - other.x, -1.0, 1.0),
            Range.clip(y - other.y, -1.0, 1.0),
            Range.clip(r - other.r, -1.0, 1.0)
        )
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
        val newX = y
        val newY = -x
        x = newX
        y = newY
    }

    /**
     * Rotate the vector 90 degrees anticlockwise.
     */
    fun left() {
        val newX = -y
        val newY = x
        x = newX
        y = newY
    }

    /**
     * Normalise the vector by a given magnitude.
     */
    fun normalise(mag: Double) {
        if (mag == 0.0) {
            x = 0.0
            y = 0.0
            return
        }
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
