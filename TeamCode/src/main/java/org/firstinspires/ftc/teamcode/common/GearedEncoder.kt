package org.firstinspires.ftc.teamcode.common

/**
 * Extension of DcMotor that implements a gear reduction calculation for tracking the position of a motor.
 * @author Lucas Bubner, 2023
 */
interface GearedEncoder : Encoder {
    val gearRatio: Double
    override fun position(scope: Encoder.Scope): Double {
        return super.position(scope) * gearRatio
    }
}