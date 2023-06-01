package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import kotlin.math.abs

/**
 * Deadwheel operation class for X and Y axis configurations, including tracking and translation.
 * @author Lucas Bubner, 2023
 */
class Deadwheels(
    opMode: BunyipsOpMode,
    private var x: DcMotorEx,
    private var y: DcMotorEx // It is fine to declare motors as not nullable as we'd have bigger problems if they weren't initialised by now
) : BunyipsComponent(opMode), XYEncoder {
    // Hold the encoder values for the X and Y axis
    @Volatile
    var positions = mutableMapOf(XYEncoder.Axis.X to 0.0, XYEncoder.Axis.Y to 0.0)
        private set

    override fun enableTracking(encoder: XYEncoder.Axis) {
        if (positions[XYEncoder.Axis.X] == 0.0 && positions[XYEncoder.Axis.Y] == 0.0) {
            x.mode = RunMode.STOP_AND_RESET_ENCODER
            y.mode = RunMode.STOP_AND_RESET_ENCODER
        }
        when (encoder) {
            XYEncoder.Axis.X -> {
                x.mode = RunMode.RUN_WITHOUT_ENCODER
            }

            XYEncoder.Axis.Y -> {
                y.mode = RunMode.RUN_WITHOUT_ENCODER
            }

            XYEncoder.Axis.BOTH -> {
                x.mode = RunMode.RUN_WITHOUT_ENCODER
                y.mode = RunMode.RUN_WITHOUT_ENCODER
            }
        }
    }

    override fun enableTracking() {
        enableTracking(XYEncoder.Axis.BOTH)
    }

    @Synchronized
    override fun disableTracking(encoder: XYEncoder.Axis) {
        when (encoder) {
            XYEncoder.Axis.X -> {
                positions[XYEncoder.Axis.X] =
                    positions[XYEncoder.Axis.X]!! + x.currentPosition.toDouble()
                x.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            XYEncoder.Axis.Y -> {
                positions[XYEncoder.Axis.Y] =
                    positions[XYEncoder.Axis.Y]!! + y.currentPosition.toDouble()
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            XYEncoder.Axis.BOTH -> {
                positions[XYEncoder.Axis.X] =
                    positions[XYEncoder.Axis.X]!! + x.currentPosition.toDouble()
                positions[XYEncoder.Axis.Y] =
                    positions[XYEncoder.Axis.Y]!! + y.currentPosition.toDouble()
                x.mode = RunMode.STOP_AND_RESET_ENCODER
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }
        }
        x.mode = RunMode.RUN_WITHOUT_ENCODER
        y.mode = RunMode.RUN_WITHOUT_ENCODER
    }

    override fun disableTracking() {
        disableTracking(XYEncoder.Axis.BOTH)
    }

    override fun resetTracking(encoder: XYEncoder.Axis) {
        when (encoder) {
            XYEncoder.Axis.X -> {
                x.mode = RunMode.STOP_AND_RESET_ENCODER
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            XYEncoder.Axis.Y -> {
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            XYEncoder.Axis.BOTH -> {
                x.mode = RunMode.STOP_AND_RESET_ENCODER
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }
        }
        x.mode = RunMode.RUN_WITHOUT_ENCODER
        y.mode = RunMode.RUN_WITHOUT_ENCODER
        positions = mutableMapOf(XYEncoder.Axis.X to 0.0, XYEncoder.Axis.Y to 0.0)
    }

    override fun resetTracking() {
        resetTracking(XYEncoder.Axis.BOTH)
    }

    override fun travelledMM(encoder: XYEncoder.Axis): Double {
        // Equation: circumference (2*pi*r) * (encoder ticks / ticksPerRevolution)
        return Math.PI * WHEEL_DIAMETER_MM * (encoderReading(encoder) / TICKS_PER_REVOLUTION)
    }

    override fun travelledMM(): Double {
        return travelledMM(XYEncoder.Axis.BOTH)
    }

    override fun encoderReading(encoder: XYEncoder.Axis): Double {
        return when (encoder) {
            XYEncoder.Axis.X -> x.currentPosition.toDouble() + positions[XYEncoder.Axis.X]!!
            XYEncoder.Axis.Y -> y.currentPosition.toDouble() + positions[XYEncoder.Axis.Y]!!
            XYEncoder.Axis.BOTH -> throw IllegalArgumentException("Deadwheels: Cannot retrieve encoder value with BOTH axis. Use allEncoderReadings() or specify an axis other than BOTH.")
        }
    }

    override fun encoderReading(): Double {
        return encoderReading(XYEncoder.Axis.BOTH)
    }

    override fun allEncoderReadings(): DoubleArray {
        return doubleArrayOf(
            x.currentPosition.toDouble() + positions[XYEncoder.Axis.X]!!,
            y.currentPosition.toDouble() + positions[XYEncoder.Axis.Y]!!,
            travelledMM(XYEncoder.Axis.X),
            travelledMM(XYEncoder.Axis.Y)
        )
    }

    override fun targetReached(encoder: XYEncoder.Axis, goal: Double): Boolean {
        return abs(travelledMM(encoder)) >= abs(goal)
    }

    companion object {
        // These inputs are for the BQLZR 600P/R encoders, using two-phase output
        // https://www.amazon.com.au/Signswise-Incremental-Encoder-Dc5-24v-Voltage/dp/B00UTIFCVA
        private const val TICKS_PER_REVOLUTION = 2400
        private const val WHEEL_DIAMETER_MM = 50
    }
}
