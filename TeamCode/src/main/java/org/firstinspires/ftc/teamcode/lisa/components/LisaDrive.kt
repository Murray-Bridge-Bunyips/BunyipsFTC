package org.firstinspires.ftc.teamcode.lisa.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.EncoderMotor

/**
 * Drive control system for Lisa, controlling movement.
 */
class LisaDrive(
    opMode: BunyipsOpMode?,
    private val left: DcMotorEx?,
    private val right: DcMotorEx?
) : BunyipsComponent(opMode) {
    private var leftPower = 0.0
    private var rightPower = 0.0

    private val leftMotor = EncoderMotor(left!!, null, null)
    private val rightMotor = EncoderMotor(right!!, null, null)
    /*
        These might be helpful for later, defined before in the original codebase

        UltraPlanetary HD Hex Motor
        val TICKS_PER_REVOLUTION = 28.0
        val WHEEL_DIAMETER_CM = 8.5
     */

    fun setPower(left: Double, right: Double) {
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

    @SuppressLint("DefaultLocale")
    fun update() {
        leftMotor.power = leftPower
        rightMotor.power = rightPower
    }

    fun targetPositionReached(): Boolean {
        return !(leftMotor.isBusy || rightMotor.isBusy)
    }

    fun setTargetPosition(leftDistance: Double, rightDistance: Double) {
        val newLeftTarget = leftMotor.currentPosition + leftDistance.toInt()
        val newRightTarget = rightMotor.currentPosition + rightDistance.toInt()

        leftMotor.targetPosition = newLeftTarget
        rightMotor.targetPosition = newRightTarget

        leftMotor.mode = RunMode.RUN_TO_POSITION
        rightMotor.mode = RunMode.RUN_TO_POSITION
    }
}