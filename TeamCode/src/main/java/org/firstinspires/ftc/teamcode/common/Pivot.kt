package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.DcMotor

class Pivot(
    opMode: BunyipsOpMode,
    private var pivot: DcMotor,
    override val ticksPerRevolution: Double,
) : BunyipsComponent(opMode), Encoder {
    override val wheelDiameterMM = null
    override var snapshot: Double = 0.0

    override fun track() {
        this.snapshot = pivot.currentPosition.toDouble()
        pivot.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun reset() {
        this.snapshot = 0.0
        pivot.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        pivot.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun position(scope: Encoder.Scope): Double {
        return if (scope == Encoder.Scope.RELATIVE) {
            pivot.currentPosition.toDouble() - snapshot
        } else {
            pivot.currentPosition.toDouble()
        }
    }

    override fun travelledMM(scope: Encoder.Scope): Double {
        throw NotImplementedError("noop, use getDegrees() instead")
    }

    fun getDegrees(scope: Encoder.Scope): Double {
        return (position(scope) / ticksPerRevolution) * 360
    }

}