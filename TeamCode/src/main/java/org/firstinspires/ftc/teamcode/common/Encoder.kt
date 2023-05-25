package org.firstinspires.ftc.teamcode.common

/**
 * Interface abstraction for encoder motors, for functionality such as enabling/disabling tracking.
 */
interface Encoder {
    /**
     * Enable encoder and begin tracking location (Ensure to reset to zero unless intended)
     */
    fun enableTracking()

    /**
     * Disable encoder and keep last saved position
     */
    fun disableTracking()

    /**
     * Reset encoder position to zero
     */
    fun resetTracking()

    /**
     * Get the distance travelled by the encoder since the last reset and if tracking was enabled
     *
     * @return millimetres indicating how far the encoder has travelled
     */
    fun travelledMM(): Double

    /**
     * Get the reading from the encoder
     *
     * @return encoder value
     */
    fun encoderReading(): Double
}