package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotorEx

interface Encoder : DcMotorEx {
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
    val travelledMM: Double

    /**
     * Get the reading from the encoder
     *
     * @return encoder value
     */
    val encoderReading: Double

    /**
     * Return whether the encoders have reached a goal
     * @return
     */
    fun targetReached(goal: Double): Boolean
}