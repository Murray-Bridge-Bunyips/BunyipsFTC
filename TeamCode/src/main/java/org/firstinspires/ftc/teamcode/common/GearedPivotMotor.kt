package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorImpl

/**
 * Extension of DcMotor that implements a pivotal encoder for tracking the position of a pivot.
 * @author Lucas Bubner, 2023
 */

class GearedPivotMotor(
    override val motor: DcMotorEx,
    override val ticksPerRevolution: Double,
    override val gearRatio: Double,
) : DcMotorImpl(motor.controller, motor.portNumber), GearedEncoder {
    override val wheelDiameterMM = null
    override var snapshot: Double = 0.0

    fun getDegrees(scope: Encoder.Scope = Encoder.Scope.RELATIVE): Double {
        return (position(scope) / ticksPerRevolution) * 360
    }

    // Java interop
    fun getDegrees(): Double {
        return getDegrees(Encoder.Scope.RELATIVE)
    }

    fun getRadians(scope: Encoder.Scope = Encoder.Scope.RELATIVE): Double {
        return (position(scope) / ticksPerRevolution) * (2 * Math.PI)
    }

    // Java interop
    fun getRadians(): Double {
        return getRadians(Encoder.Scope.RELATIVE)
    }

    fun setDegrees(degrees: Double) {
        motor.targetPosition = ((degrees / 360) * ticksPerRevolution / gearRatio).toInt()
    }

    fun setRadians(radians: Double) {
        motor.targetPosition = ((radians / (2 * Math.PI)) * ticksPerRevolution / gearRatio).toInt()
    }

}