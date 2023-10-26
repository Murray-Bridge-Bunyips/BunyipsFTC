package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorImpl

/**
 * Extension of DcMotor that implements a pivotal encoder for tracking the position of a pivot.
 * @author Lucas Bubner, 2023
 */

class PivotMotor(
    override val motor: DcMotorEx,
    override val ticksPerRevolution: Double,
) : DcMotorImpl(motor.controller, motor.portNumber), Encoder {
    override val wheelDiameterMM = null
    override var snapshot: Double = 0.0

    fun getDegrees(scope: Encoder.Scope = Encoder.Scope.RELATIVE): Double {
        return (position(scope) / ticksPerRevolution) * 360
    }

}