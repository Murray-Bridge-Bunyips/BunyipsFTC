package org.firstinspires.ftc.teamcode.common

/**
 * Interface abstraction for encoder motors, for functionality such as enabling/disabling tracking.
 */
interface Encoder {
    enum class Scope {
        RELATIVE, GLOBAL
    }

    val wheelDiameterMM: Double?
    val ticksPerRevolution: Double?

    /**
     * Store a snapshot of encoder position when tracking is started.
     */
    var snapshot: Double

    /**
     * Enable encoder and start tracking, which will also save a snapshot of the encoder position
     */
    fun track()

    /**
     * Reset encoder positions to zero. Useful when saved state is not needed or can be discarded.
     */
    fun reset()

    /**
     * Get a movement reading in ticks from the encoder since the last track()
     * Can use an optional parameter to use since reset() position instead of track()
     * @return encoder value relative to last track() call, or since the last reset() call
     */
    fun position(scope: Scope = Scope.RELATIVE): Double

    /**
     * Get the distance travelled by the encoder since the last track()
     * Can use an optional parameter to use since reset() position instead of track()
     * @return millimetres indicating how far the encoder has travelled
     */
    fun travelledMM(scope: Scope = Scope.RELATIVE): Double
}