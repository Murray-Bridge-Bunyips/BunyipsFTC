package org.firstinspires.ftc.teamcode.lisa.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.Encoder
import org.firstinspires.ftc.teamcode.common.Odometer
import java.util.Locale
import kotlin.math.abs

/**
 * Drive control system for Lisa, controlling movement.
 */
class LisaDrive(
    opMode: BunyipsOpMode,
    private val leftMotor: DcMotorEx,
    private val rightMotor: DcMotorEx
) : BunyipsComponent(opMode) {
    private var leftPower = 0.0
    private var rightPower = 0.0

    // TODO: Define these values
    private val leftEncoder = Odometer(opMode, leftMotor, null, null)
    private val rightEncoder = Odometer(opMode, rightMotor, null, null)
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

    fun setEncoders() {
        leftEncoder.track()
        rightEncoder.track()
    }

    fun resetEncoders() {
        leftEncoder.reset()
        rightEncoder.reset()
    }

    fun reachedGoal(goalMM: Double, scope: Encoder.Scope = Encoder.Scope.RELATIVE): Boolean {
        return abs(leftEncoder.travelledMM(scope)) >= abs(goalMM) && abs(
            rightEncoder.travelledMM(
                scope
            )
        ) >= abs(
            goalMM
        )
    }

    fun getTravelledDist(scope: Encoder.Scope = Encoder.Scope.RELATIVE): Pair<Double, Double> {
        return Pair(leftEncoder.travelledMM(scope), rightEncoder.travelledMM(scope))
    }

    fun getEncoderValues(scope: Encoder.Scope = Encoder.Scope.RELATIVE): Pair<Double, Double> {
        return Pair(leftEncoder.position(scope), rightEncoder.position(scope))
    }

    @SuppressLint("DefaultLocale")
    fun update() {
        leftMotor.power = leftPower
        rightMotor.power = rightPower
        opMode.addTelemetry(
            String.format(
                Locale.getDefault(),
                "Drive: L: %.2f, R: %.2f",
                leftPower,
                rightPower
            )
        )
    }
}