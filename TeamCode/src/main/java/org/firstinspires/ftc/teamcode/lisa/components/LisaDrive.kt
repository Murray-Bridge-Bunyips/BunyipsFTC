package org.firstinspires.ftc.teamcode.lisa.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.EncoderMotor
import kotlin.math.abs

/**
 * Drive control system for Lisa, controlling movement.
 */
class LisaDrive(
    opMode: BunyipsOpMode,
    left: DcMotorEx,
    right: DcMotorEx
) : BunyipsComponent(opMode) {
    private var leftPower = 0.0
    private var rightPower = 0.0

    private val leftMotor = EncoderMotor(left, null, null)
    private val rightMotor = EncoderMotor(right, null, null)
    // TODO: Define these values
    /*
        These might be helpful for later, defined before in the original codebase

        UltraPlanetary HD Hex Motor
        val TICKS_PER_REVOLUTION = 28.0
        val WHEEL_DIAMETER_CM = 8.5
     */

    fun setPower(left: Double, right: Double) {
        // TODO: Consider using power curves to make the robot more controllable
        leftPower = -left
        rightPower = -right
    }

    fun setToBrake() {
        leftMotor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        rightMotor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
    }

    fun setToFloat() {
        leftMotor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT
        rightMotor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT
    }

    fun setEncoders(state: Boolean) {
        if (state) {
            leftMotor.enableTracking()
            rightMotor.enableTracking()
        } else {
            leftMotor.disableTracking()
            rightMotor.disableTracking()
        }
    }

    fun resetEncoders() {
        leftMotor.resetTracking()
        rightMotor.resetTracking()
    }

    fun reachedGoal(goalMM: Double): Boolean {
        return abs(leftMotor.travelledMM()) >= abs(goalMM) && abs(rightMotor.travelledMM()) >= abs(
            goalMM
        )
    }

    fun getTravelledDist(): Pair<Double, Double> {
        return Pair(leftMotor.travelledMM(), rightMotor.travelledMM())
    }

    fun getEncoderValues(): Pair<Double, Double> {
        return Pair(leftMotor.encoderReading(), rightMotor.encoderReading())
    }

    @SuppressLint("DefaultLocale")
    fun update() {
        // TODO: Add telemetry
        leftMotor.power = leftPower
        rightMotor.power = rightPower
    }
}