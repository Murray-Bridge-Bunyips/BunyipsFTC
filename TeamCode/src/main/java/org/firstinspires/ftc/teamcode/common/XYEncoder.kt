package org.firstinspires.ftc.teamcode.common


/**
 * Additional abstraction for DcMotors for use with X-Y encoders.
 */
interface XYEncoder : Encoder {
    /**
     * Enum for the axis of the encoder
     */
    enum class Axis {
        X, Y, BOTH
    }

    /**
     * Enable encoder and begin tracking location (Ensure to reset to zero unless intended)
     */
    fun enableTracking(encoder: Axis)

    /**
     * Disable encoder and keep last saved position
     */
    fun disableTracking(encoder: Axis)

    /**
     * Reset encoder position to zero
     */
    fun resetTracking(encoder: Axis)

    /**
     * Get the distance travelled by the encoder since the last reset and if tracking was enabled
     *
     * @return millimetres indicating how far the encoder has travelled
     */
    fun travelledMM(encoder: Axis): Double

    /**
     * Get the reading from the encoder
     *
     * @return encoder value
     */
    fun encoderReading(encoder: Axis): Double

    /**
     * Get the reading from both encoders in both encoder and MM format.
     *
     * @return Four values, X encoder value, Y encoder value, X MM value, Y MM value
     */
    fun allEncoderReadings(): DoubleArray

    /**
     * Return whether the encoders have reached a goal
     *
     * @param goal Goal in mm
     * @return boolean expression whether the encoders read a goal has been completed
     */
    fun targetReached(encoder: Axis, goal: Double): Boolean
}