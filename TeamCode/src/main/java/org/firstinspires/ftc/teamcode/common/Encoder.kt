package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

/**
 * Interface abstraction for encoder motors, for functionality such as enabling/disabling tracking.
 */
interface Encoder {
    enum class Scope {
        RELATIVE, GLOBAL
    }

    val wheelDiameterMM: Double?
    val ticksPerRevolution: Double?
    val motor: DcMotorEx

    /**
     * Store a snapshot of encoder position when tracking is started.
     */
    var snapshot: Double

    /**
     * Enable encoder and start tracking, which will also save a snapshot of the encoder position
     */
    fun track() {
        // Store the current encoder position
        this.snapshot = motor.currentPosition.toDouble()
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    /**
     * Reset encoder positions to zero. Useful when saved state is not needed or can be discarded.
     */
    fun reset() {
        this.snapshot = 0.0
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    /**
     * Get a movement reading in ticks from the encoder since the last track()
     * Can use an optional parameter to use since reset() position instead of track()
     * @return encoder value relative to last track() call, or since the last reset() call
     */
    fun position(scope: Scope = Scope.RELATIVE): Double {
        return if (scope == Scope.RELATIVE) {
            motor.currentPosition.toDouble() - snapshot
        } else {
            motor.currentPosition.toDouble()
        }
    }

    /**
     * Get the distance travelled by the encoder since the last track()
     * Can use an optional parameter to use since reset() position instead of track()
     * @return millimetres indicating how far the encoder has travelled
     */
    fun travelledMM(scope: Scope = Scope.RELATIVE): Double {
        // Equation: circumference (2*pi*r) * (encoder ticks / ticksPerRevolution)
        if (wheelDiameterMM == null || ticksPerRevolution == null) {
            throw IllegalStateException("Odometer: wheelDiameterMM and ticksPerRevolution must be set to use travelledMM()")
        }
        // Return travelled distance in millimetres depending on selected accuracy
        return Math.PI * wheelDiameterMM!! * (position(scope) / ticksPerRevolution!!)
    }
}