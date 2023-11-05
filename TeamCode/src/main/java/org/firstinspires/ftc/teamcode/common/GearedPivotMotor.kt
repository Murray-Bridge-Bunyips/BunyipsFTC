package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotorEx

/**
 * Extension of DcMotor that implements a pivotal encoder for tracking the position of a pivot.
 * @author Lucas Bubner, 2023
 */

class GearedPivotMotor(
    override val motor: DcMotorEx,
    override val ticksPerRevolution: Double,
    override val gearRatio: Double,
) : PivotMotor(motor, ticksPerRevolution), GearedEncoder {
    override var snapshot: Double = 0.0

    override fun setDegrees(degrees: Double) {
        motor.targetPosition =
            ((degrees / 360) * ticksPerRevolution / gearRatio).toInt() - snapshot.toInt()
    }

    override fun setRadians(radians: Double) {
        motor.targetPosition =
            ((radians / (2 * Math.PI)) * ticksPerRevolution / gearRatio).toInt() - snapshot.toInt()
    }
}