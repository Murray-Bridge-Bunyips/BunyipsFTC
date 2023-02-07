package org.firstinspires.ftc.teamcode.lisa.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

class LisaDrive(
    opMode: BunyipsOpMode?,
    private val left: DcMotorEx?,
    private val right: DcMotorEx?
) : BunyipsComponent(opMode) {
    private var leftPower = 0.0
    private var rightPower = 0.0
    fun setPower(left: Double, right: Double) {
        leftPower = -left
        rightPower = -right
    }

    fun setToBrake() {
        left?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        right?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
    }

    fun setToFloat() {
        left?.zeroPowerBehavior = ZeroPowerBehavior.FLOAT
        right?.zeroPowerBehavior = ZeroPowerBehavior.FLOAT
    }

    fun setEncoder(encoderState: Boolean) {
        if (encoderState) {
            left?.mode = RunMode.STOP_AND_RESET_ENCODER
            right?.mode = RunMode.STOP_AND_RESET_ENCODER
            left?.mode = RunMode.RUN_USING_ENCODER
            right?.mode = RunMode.RUN_USING_ENCODER
            return
        }
        left?.mode = RunMode.RUN_WITHOUT_ENCODER
        right?.mode = RunMode.RUN_WITHOUT_ENCODER
    }

    @SuppressLint("DefaultLocale")
    fun update() {
        left?.power = leftPower
        right?.power = rightPower
        opMode!!.telemetry.addLine(
            String.format(
                "Left Encoder: %d, Right Encoder: %d",
                left?.currentPosition, right?.currentPosition
            )
        )
    }

    fun targetPositionReached(): Boolean {
        return !(left!!.isBusy || right!!.isBusy)
    }

    fun setTargetPosition(leftDistance: Double, rightDistance: Double) {
        val newLeftTarget: Int
        val newRightTarget: Int
        newLeftTarget = left?.currentPosition?.plus(leftDistance.toInt()) ?: leftDistance.toInt()
        newRightTarget = right?.currentPosition?.plus(rightDistance.toInt()) ?: rightDistance.toInt()
        left?.targetPosition = newLeftTarget
        right?.targetPosition = newRightTarget
        left?.mode = RunMode.RUN_TO_POSITION
        right?.mode = RunMode.RUN_TO_POSITION
    }
}