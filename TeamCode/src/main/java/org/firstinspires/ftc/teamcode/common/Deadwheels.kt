package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import kotlin.math.abs

/**
 * Deadwheel operation class for X and Y axis configurations.
 * @author Lucas Bubner, 2023
 */
class Deadwheels(
    opMode: BunyipsOpMode?,
    private var x: DcMotorEx,
    private var y: DcMotorEx // It is fine to declare motors as not nullable as we'd have bigger problems if they weren't initialised by now
) : BunyipsComponent(opMode), Encoder {
    // Hold the encoder values for the X and Y axis
    @Volatile
    var positions = mutableMapOf(Encoder.Axis.X to 0.0, Encoder.Axis.Y to 0.0)
        private set

    override fun enableTracking(encoder: Encoder.Axis) {
        when (encoder) {
            Encoder.Axis.X -> {
                x.mode = RunMode.RUN_USING_ENCODER
            }

            Encoder.Axis.Y -> {
                y.mode = RunMode.RUN_USING_ENCODER
            }

            Encoder.Axis.BOTH -> {
                x.mode = RunMode.RUN_USING_ENCODER
                y.mode = RunMode.RUN_USING_ENCODER
            }
        }
    }

    @Synchronized
    override fun disableTracking(encoder: Encoder.Axis) {
        when (encoder) {
            Encoder.Axis.X -> {
                positions[Encoder.Axis.X] =
                    positions[Encoder.Axis.X]!! + x.currentPosition.toDouble()
                x.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            Encoder.Axis.Y -> {
                positions[Encoder.Axis.Y] =
                    positions[Encoder.Axis.Y]!! + y.currentPosition.toDouble()
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            Encoder.Axis.BOTH -> {
                positions[Encoder.Axis.X] =
                    positions[Encoder.Axis.X]!! + x.currentPosition.toDouble()
                positions[Encoder.Axis.Y] =
                    positions[Encoder.Axis.Y]!! + y.currentPosition.toDouble()
                x.mode = RunMode.STOP_AND_RESET_ENCODER
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }
        }
    }

    override fun resetTracking(encoder: Encoder.Axis) {
        when (encoder) {
            Encoder.Axis.X -> {
                x.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            Encoder.Axis.Y -> {
                y.mode = RunMode.STOP_AND_RESET_ENCODER
            }

            Encoder.Axis.BOTH -> {
                x.mode = RunMode.STOP_AND_RESET_ENCODER
                y.mode = RunMode.STOP_AND_RESET_ENCODER

            }
        }
        positions = mutableMapOf(Encoder.Axis.X to 0.0, Encoder.Axis.Y to 0.0)
        selfTestErrorCount = 0
    }

    override fun selfTest(encoder: Encoder.Axis): Boolean {
        // Cycle encoder tracking off and back on to ensure a value is currently in the positions array
        enableTracking(encoder)
        disableTracking(encoder)

        // Check if the encoder values are 0
        if (encoderReading(encoder) == 0.0 && selfTestErrorCount < SELF_TEST_ERROR_THRESHOLD) {
            // If they are, it may be a case that it is the first run, so we'll log this and wait for the next run
            selfTestErrorCount++
        } else if (encoderReading(encoder) == 0.0 && selfTestErrorCount >= SELF_TEST_ERROR_THRESHOLD) {
            // If the encoder values are still 0 after multiple runs, then we can assume the encoder is not working
            return false
        }
        return true
    }

    override fun travelledMM(encoder: Encoder.Axis): Double {
        // Equation: (2 * pi * r * (encoderReading + position)) / ticksPerRevolution
        return Math.PI * WHEEL_DIAMETER_MM * ((encoderReading(encoder) + positions[encoder]!!) / TICKS_PER_REVOLUTION) * 10
    }

    override fun encoderReading(encoder: Encoder.Axis): Double {
        return when (encoder) {
            Encoder.Axis.X -> x.currentPosition.toDouble() + positions[Encoder.Axis.X]!!
            Encoder.Axis.Y -> y.currentPosition.toDouble() + positions[Encoder.Axis.Y]!!
            Encoder.Axis.BOTH -> throw IllegalArgumentException("Cannot retrieve encoder value with BOTH axis. Use allEncoderReadings() or specify an axis other than BOTH.")
        }
    }

    override fun allEncoderReadings(): DoubleArray {
        return doubleArrayOf(
            x.currentPosition.toDouble() + positions[Encoder.Axis.X]!!,
            y.currentPosition.toDouble() + positions[Encoder.Axis.Y]!!,
            travelledMM(Encoder.Axis.X),
            travelledMM(Encoder.Axis.Y)
        )
    }

    override fun targetReached(encoder: Encoder.Axis, goal: Double): Boolean {
        return abs(travelledMM(encoder)) >= abs(goal)
    }

    companion object {
        // These inputs are for the BQLZR 600P/R encoders, using two-phase output
        // https://www.amazon.com.au/Signswise-Incremental-Encoder-Dc5-24v-Voltage/dp/B00UTIFCVA
        private const val TICKS_PER_REVOLUTION = 2400
        private const val WHEEL_DIAMETER_MM = 50

        // Count any errors that occur in the self test. If the values of the encoders are not
        // moving after a task multiple times, then we can assume the encoder is not working.
        private const val SELF_TEST_ERROR_THRESHOLD = 2
        var selfTestErrorCount = 0
    }
}
