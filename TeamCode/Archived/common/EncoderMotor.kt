package org.murraybridgebunyips.bunyipslib

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import org.murraybridgebunyips.bunyipslib.external.units.Angle
import org.murraybridgebunyips.bunyipslib.external.units.Distance
import org.murraybridgebunyips.bunyipslib.external.units.Measure
import org.murraybridgebunyips.bunyipslib.external.units.Units.Millimeters
import org.murraybridgebunyips.bunyipslib.external.units.Units.Revolutions
import kotlin.math.roundToInt

/**
 * A generic encoder based motor, which is used to track the position of a motor in both relative and absolute scopes.
 * Includes methods for tracking, resetting, and calculating distance travelled based on WPIUnits.
 *
 * @author Lucas Bubner, 2023
 */
class EncoderMotor @JvmOverloads constructor(
    motor: DcMotorEx,
    /**
     * The reduction ratio of the motor, default is 1.0
     */
    var reduction: Double = 1.0,
    /**
     * The diameter of the wheel attached to the motor, default is null
     */
    var wheelDiameter: Measure<Distance>? = null,
    /**
     * The number of ticks per revolution of the encoder, default will try to use the motor type's ticks per revolution
     */
    var ticksPerRevolution: Double = motor.motorType.ticksPerRev,
) : DcMotorImplEx(motor.controller, motor.portNumber) {
    /**
     * Store a snapshot of encoder position when tracking is started.
     */
    var snapshot: Int = 0
        private set

    private var lock: Boolean = false

    /**
     * Set the mode of the motor. If the mode is STOP_AND_RESET_ENCODER, the encoder snapshot will also be cleared.
     */
    override fun setMode(mode: DcMotor.RunMode) {
        if (mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
            // Make setMode(STOP_AND_RESET_ENCODER) do the same as [reset]
            // This ensures the motor always is in a mode that is not the reset mode
            resetEncoder()
            return
        }
        super.setMode(mode)
    }

    /**
     * Hold the current position of the encoder using RUN_TO_POSITION.
     * This position may be released with [resetHoldPosition], allowing this method to be invoked in a loop.
     * @param holdingPower the power to hold the position at, default is 1.0
     */
    @JvmOverloads
    fun holdCurrentPosition(holdingPower: Double = 1.0) {
        if (!lock) {
            targetPosition = currentPosition
            lock = true
        }
        mode = DcMotor.RunMode.RUN_TO_POSITION
        power = holdingPower
    }

    /**
     * Stop holding the current position set by [holdCurrentPosition].
     */
    fun resetHoldPosition() {
        lock = false
    }

    /**
     * Run to the set position at the set power.
     * @param targetPosition the position to run to
     * @param power the power to run at
     */
    fun runToPosition(targetPosition: Int, power: Double) {
        this.targetPosition = targetPosition
        mode = DcMotor.RunMode.RUN_TO_POSITION
        this.power = power
    }

    /**
     * Run to the set position at the set power.
     * @param targetAngle the angle to run to
     * @param unit the unit of the angle
     * @param power the power to run at
     */
    fun runToPosition(targetAngle: Double, unit: Angle, power: Double) {
        setTargetPosition(targetAngle, unit)
        runToPosition(targetPosition, power)
    }

    /**
     * Run to the set position at the set power.
     * @param targetDistance the distance to run to
     * @param unit the unit of the distance
     * @param power the power to run at
     */
    fun runToPosition(targetDistance: Double, unit: Distance, power: Double) {
        if (wheelDiameter == null) {
            throw IllegalStateException("EncoderMotor: wheelDiameter must be set to use runToPosition() with a distance")
        }
        setTargetPosition(targetDistance, unit)
        runToPosition(targetPosition, power)
    }

    /**
     * Set the target position of the motor in angle.
     * @param targetAngle the target angle to set the motor to
     * @param unit the unit of the angle
     */
    fun setTargetPosition(targetAngle: Double, unit: Angle) {
        targetPosition = (unit.of(targetAngle).inUnit(Revolutions) * ticksPerRevolution).roundToInt()
    }

    /**
     * Set the target position of the motor in distance.
     * @param targetDistance the target distance to set the motor to
     */
    fun setTargetPosition(targetDistance: Double, unit: Distance) {
        if (wheelDiameter == null) {
            throw IllegalStateException("EncoderMotor: wheelDiameter must be set to use setTargetPosition() with a distance")
        }
        // Convert distance back to encoder ticks
        // if the distance D = (2 * pi * r) * (P / ticksPerRevolution)
        // then the encoder ticks = (ticksPerRevolution * D) / (2 * pi * r)
        targetPosition = ((ticksPerRevolution * unit.of(targetDistance).inUnit(Millimeters))
                        / (Math.PI * wheelDiameter!!.inUnit(Millimeters))).roundToInt()
    }

    /**
     * Reset encoder positions to zero at a defined scope.
     * This is the equivalent of setting the mode to STOP_AND_RESET_ENCODER, then setting the mode back to the
     * previous mode. Note that the motor power is also set to zero if using a HARDWARE scope reset.
     * @param scope the scope of the reset, default is RELATIVE. RELATIVE will not reset the actual encoder, but
     *              will track the position from the last reset. HARDWARE will reset the encoder at a hardware level.
     *              You can access values from either reset point using the getCurrent...() methods.
     */
    @JvmOverloads
    fun resetEncoder(scope: Scope = Scope.RELATIVE) {
        if (scope == Scope.RELATIVE) {
            snapshot = super.getCurrentPosition()
            return
        }
        this.snapshot = 0
        power = 0.0
        val prev = mode
        super.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        targetPosition = 0
        mode = prev
    }

    /**
     * Get a position reading in ticks from the encoder since the last [resetEncoder] call.
     * @return encoder value relative in a RELATIVE scope
     */
    override fun getCurrentPosition(): Int {
        return getCurrentPosition(Scope.RELATIVE)
    }

    /**
     * Set the target position of the motor in ticks. This will add the current snapshot to the target position for
     * relative tracking.
     * @param position the target position to set the motor to
     */
    override fun setTargetPosition(position: Int) {
        super.setTargetPosition(position + snapshot)
    }

    /**
     * Get a position reading in ticks from the encoder since the last [resetEncoder] call.
     * Can use an optional parameter to calculate since the last [resetEncoder] at a HARDWARE scope.
     * @return encoder value relative to last RELATIVE or HARDWARE scope
     */
    fun getCurrentPosition(scope: Scope): Int {
        return if (scope == Scope.RELATIVE) {
            ((super.getCurrentPosition() - snapshot) * reduction).roundToInt()
        } else {
            (super.getCurrentPosition() * reduction).roundToInt()
        }
    }

    /**
     * Get the number of revolutions the encoder has travelled since the last [resetEncoder] call.
     * Can use an optional parameter to calculate since the last [resetEncoder] at a HARDWARE scope.
     * @return revolutions indicating how far the encoder has travelled
     */
    @JvmOverloads
    fun getCurrentAngle(scope: Scope = Scope.RELATIVE): Measure<Angle> {
        // Equation: encoder ticks / ticksPerRevolution
        if (ticksPerRevolution == 0.0) {
            throw IllegalStateException("EncoderMotor: ticksPerRevolution must be set to use travelledRevolutions()")
        }
        // Return travelled revolutions depending on selected accuracy
        return Revolutions.of(getCurrentPosition(scope) / ticksPerRevolution)
    }

    /**
     * Get the distance the encoder has travelled since the last [resetEncoder] call.
     * Can use an optional parameter to calculate since the last [resetEncoder] at a HARDWARE scope.
     * @return distance indicating how far the encoder has travelled
     */
    @JvmOverloads
    fun getCurrentDistance(scope: Scope = Scope.RELATIVE): Measure<Distance> {
        // Equation: circumference (2*pi*r) * (encoder ticks / ticksPerRevolution)
        if (wheelDiameter == null || ticksPerRevolution == 0.0) {
            throw IllegalStateException("EncoderMotor: wheelDiameter and ticksPerRevolution must be set to use travelledMM()")
        }
        // Return travelled distance in millimetres depending on selected accuracy
        return Millimeters.of(Math.PI * wheelDiameter!!.inUnit(Millimeters) * (getCurrentPosition(scope) / ticksPerRevolution))
    }

    /**
     * Scope of the encoder tracking.
     */
    enum class Scope {
        /**
         * Since the last [resetEncoder] call at a class-level (default) scope.
         * Unless explicitly mentioned, relative scope is used for all EncoderMotors.
         */
        RELATIVE,

        /**
         * Since the last full STOP_AND_RESET_ENCODER call (e.g. [resetEncoder] at a HARDWARE scope).
         */
        HARDWARE
    }
}