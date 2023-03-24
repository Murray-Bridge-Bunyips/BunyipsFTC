package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor.RunMode

abstract class DeadwheelMotor : Encoder {
    @Volatile
    var position = 0.0

    /**
     * Enable encoder and begin tracking location (Ensure to reset to zero unless intended)
     */
    override fun enableTracking() {
        this.mode = RunMode.RUN_USING_ENCODER
    }

    /**
     * Disable encoder and keep last saved position
     */
    @Synchronized
    override fun disableTracking() {
        position += this.currentPosition.toDouble()
        this.mode = RunMode.STOP_AND_RESET_ENCODER
    }

    /**
     * Reset encoder position to zero
     */
    override fun resetTracking() {
        this.mode = RunMode.STOP_AND_RESET_ENCODER
        position = 0.0
    }

    /**
     * Get the distance travelled by the encoder since the last reset and if tracking was enabled
     *
     * @return millimetres indicating how far the encoder has travelled
     */
    override val travelledMM: Double
        get() = Math.PI * WHEEL_DIAMETER_MM * ((encoderReading + position) / TICKS_PER_REVOLUTION) * 10

    /**
     * Get the reading from the encoder
     *
     * @return encoder value
     */
    override val encoderReading: Double
        get() = this.currentPosition + position

    /**
     * Return whether the encoders have reached a goal
     *
     * @param goal Goal in mm
     * @return boolean expression whether the encoders read a goal has been completed
     */
    override fun targetReached(goal: Double): Boolean {
        return Math.abs(travelledMM) >= Math.abs(goal)
    }

    companion object {
        // These inputs are for the BQLZR 600P/R encoders, using two-phase output
        // https://www.amazon.com.au/Signswise-Incremental-Encoder-Dc5-24v-Voltage/dp/B00UTIFCVA
        private const val TICKS_PER_REVOLUTION = 2400
        private const val WHEEL_DIAMETER_MM = 50
    }
}
