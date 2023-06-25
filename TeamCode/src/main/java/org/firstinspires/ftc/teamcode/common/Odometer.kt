package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

/**
 * Odometer class for tracking motor position, which persists across OpModes for continuity.
 * @author Lucas Bubner, 2023
 */
class Odometer(
    opMode: BunyipsOpMode,
    private var wheel: DcMotorEx,
    override val wheelDiameterMM: Double?,
    override val ticksPerRevolution: Double?
) : BunyipsComponent(opMode), Encoder {

    /**
     * Store a snapshot of encoder position when tracking is started.
     */
    override var snapshot = 0.0

    /**
     * Enable encoder and start tracking, which will also save a snapshot of the encoder position
     */
    override fun track() {
        // Store the current encoder position
        this.snapshot = wheel.currentPosition.toDouble()
        wheel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    /**
     * Reset encoder positions to zero. Useful when saved state is not needed or can be discarded.
     */
    override fun reset() {
        this.snapshot = 0.0
        wheel.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        wheel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    /**
     * Get a movement reading in ticks from the encoder since the last track()
     * Can use an optional parameter to use since reset() position instead of track()
     * @return encoder value relative to last track() call, or since the last reset() call
     */
    override fun position(scope: Encoder.Scope): Double {
        return if (scope == Encoder.Scope.RELATIVE) {
            wheel.currentPosition.toDouble() - snapshot
        } else {
            wheel.currentPosition.toDouble()
        }
    }

    /**
     * Get the distance travelled by the encoder since the last track()
     * Can use an optional parameter to use since reset() position instead of track()
     * @return millimetres indicating how far the encoder has travelled
     */
    override fun travelledMM(scope: Encoder.Scope): Double {
        // Equation: circumference (2*pi*r) * (encoder ticks / ticksPerRevolution)
        if (wheelDiameterMM == null || ticksPerRevolution == null) {
            throw IllegalStateException("Odometer: wheelDiameterMM and ticksPerRevolution must be set to use travelledMM()")
        }
        // Return travelled distance in millimetres depending on selected accuracy
        return Math.PI * wheelDiameterMM * (position(scope) / ticksPerRevolution)
    }
}