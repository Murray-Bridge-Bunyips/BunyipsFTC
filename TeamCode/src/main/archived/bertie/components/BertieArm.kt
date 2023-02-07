package org.firstinspires.ftc.archived.bertie.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
class BertieArm @SuppressLint("DefaultLocale") constructor(
    opMode: BunyipsOpMode,
    private val liftMotor: DcMotor?
) : BunyipsComponent(opMode) {
    private var liftPower: Double
    private var liftIndex = 0

    init {
        liftMotor?.direction = Direction.FORWARD
        liftMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftMotor?.mode = RunMode.STOP_AND_RESET_ENCODER
        liftMotor?.targetPosition = 0
        liftPower = 0.0
    }

    fun liftUp() {
        liftIndex++
        if (liftIndex > lift_positions.size - 1) {
            liftIndex = lift_positions.size - 1
        }
        liftMotor?.targetPosition = lift_positions[liftIndex]
    }

    fun liftDown() {
        liftIndex--
        if (liftIndex < 0) {
            liftIndex = 0
        }
        liftMotor?.targetPosition = lift_positions[liftIndex]
    }

    fun liftReset() {
        liftIndex = 0
        liftMotor?.targetPosition = lift_positions[0]
    }

    fun setPower(power: Double) {
        liftPower = power
    }

    @SuppressLint("DefaultLocale")
    fun update() {
        opMode?.telemetry?.addLine(String.format("Arm Position: %d", liftMotor?.currentPosition))
        liftMotor?.power = liftPower
        liftMotor?.mode = RunMode.RUN_TO_POSITION
    }

    companion object {
        // Adjust these numbers for encoder positions
        private val lift_positions = intArrayOf(0, 100, 400, 800, 1100)
    }
}