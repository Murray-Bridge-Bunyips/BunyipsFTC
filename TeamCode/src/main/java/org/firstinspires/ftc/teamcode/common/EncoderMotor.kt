package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImpl
import java.lang.IllegalArgumentException


/**
 * Implementation of standard motor encoder functionality, including built-in tracking and translation.
 * @author Lucas Bubner, 2023
 */
class EncoderMotor(
    motor: DcMotor,
    private val WHEEL_DIAMETER_MM: Double?,
    private val TICKS_PER_REVOLUTION: Double?
) : DcMotorImpl(motor.controller, motor.portNumber), Encoder {
    // Store capture of encoder position
    @Volatile
    private var position: Double = 0.0

    override fun enableTracking() {
        if (position == 0.0) {
            this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        }
        this.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun disableTracking() {
        position += encoderReading()
        this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    override fun resetTracking() {
        this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        position = 0.0
        this.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun travelledMM(): Double {
        // Equation: circumference (2*pi*r) * (encoder ticks / ticksPerRevolution)
        if (WHEEL_DIAMETER_MM == null || TICKS_PER_REVOLUTION == null) {
            throw IllegalArgumentException("EncoderMotor: WHEEL_DIAMETER_MM and TICKS_PER_REVOLUTION must be set to use travelledMM()")
        }
        return Math.PI * WHEEL_DIAMETER_MM * (encoderReading() / TICKS_PER_REVOLUTION)
    }

    override fun encoderReading(): Double {
        return this.currentPosition.toDouble() + position
    }
}