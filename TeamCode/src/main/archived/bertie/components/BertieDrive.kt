package org.firstinspires.ftc.archived.bertie.components

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
class BertieDrive(
    override val opMode: BunyipsOpMode,
    private val frontLeftMotor: DcMotor?, private val frontRightMotor: DcMotor?,
    private val backLeftMotor: DcMotor?, private val backRightMotor: DcMotor?,
    showTelemetry: Boolean
) : BunyipsComponent(opMode) {
    enum class MecanumDriveMode {
        NORMALIZED, ROTATION_PRIORITY_NORMALIZED
    }

    private var driveMode = MecanumDriveMode.NORMALIZED

    // X - forwards/backwards direction - positive in the forward direction
    // Y - right/right direction - positive in the right direction
    // R - Rotation - positive clockwise
    private var speedX = 0.0
    private var speedY = 0.0
    private var speedR = 0.0
    private var item: Telemetry.Item? = null
    private var showTelemetry = true

    init {
        this.showTelemetry = showTelemetry
        if (showTelemetry) {
            item = opMode.telemetry.addData(
                "Mecanum", "Forward: %.2f, Strafe: %0.02f, Rotate: %.2f", speedX, speedY,
                speedR
            )
            item?.setRetained(true)
        } else {
            item = null
        }
    }

    fun setDriveMode(mode: MecanumDriveMode) {
        driveMode = mode
    }

    fun setSpeedXYR(speedX: Double, speedY: Double, speedR: Double) {
        this.speedX = clipMotorPower(speedX)
        this.speedY = clipMotorPower(speedY)
        this.speedR = clipMotorPower(speedR)
    }

    fun setSpeedPolarR(speed: Double, direction: Double, speedR: Double) {
        val radians = Math.toRadians(direction)
        speedX = clipMotorPower(speed * Math.cos(radians))
        speedY = clipMotorPower(speed * Math.sin(radians))
        this.speedR = clipMotorPower(speedR)
    }

    /**
     * Update motors with latest state
     */
    fun update() {
        when (driveMode) {
            MecanumDriveMode.NORMALIZED -> updateNormalized()
            MecanumDriveMode.ROTATION_PRIORITY_NORMALIZED -> rotationPriorityNormalized()
        }
        if (item != null) {
            item?.setValue("Forward: %.2f, Strafe: %.2f, Rotate: %.2f", speedX, speedY, speedR)
        }
    }

    private fun updateNormalized() {
        // Calculate motor powers
        var frontLeftPower = speedX + speedY - speedR
        var frontRightPower = speedX - speedY + speedR
        var backLeftPower = speedX - speedY - speedR
        var backRightPower = speedX + speedY + speedR
        val maxPower = Math.max(
            Math.abs(frontLeftPower),
            Math.max(
                Math.abs(frontRightPower),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))
            )
        )
        // If the maximum number is greater than 1.0, then normalise by that number
        if (maxPower > 1.0) {
            frontLeftPower = frontLeftPower / maxPower
            frontRightPower = frontRightPower / maxPower
            backLeftPower = backLeftPower / maxPower
            backRightPower = backRightPower / maxPower
        }
        frontLeftMotor?.power = frontLeftPower
        frontRightMotor?.power = frontRightPower
        backLeftMotor?.power = backLeftPower
        backRightMotor?.power = backRightPower
    }

    /**
     * Calculate rotational speed first, and use remaining headway for translation.
     */
    private fun rotationPriorityNormalized() {
        // calculate motor powers
        val translationValues = doubleArrayOf(
            speedX + speedY,
            speedX - speedY,
            speedX - speedY,
            speedX + speedY
        )
        val rotationValues = doubleArrayOf(
            -speedR,
            speedR,
            -speedR,
            speedR
        )
        var scaleFactor = 1.0
        var tmpScale = 1.0

        // Solve this equation backwards:
        // MotorX = TranslationX * scaleFactor + RotationX
        // to find scaleFactor that ensures -1 <= MotorX <= 1 and 0 < scaleFactor <= 1
        for (i in 0..3) {
            if (Math.abs(translationValues[i] + rotationValues[i]) > 1) {
                tmpScale = (1 - rotationValues[i]) / translationValues[i]
            } else if (translationValues[i] + rotationValues[i] < -1) {
                tmpScale = (rotationValues[i] - 1) / translationValues[i]
            }
            if (tmpScale < scaleFactor) {
                scaleFactor = tmpScale
            }
        }
        val frontLeftPower = translationValues[0] * scaleFactor + rotationValues[0]
        val frontRightPower = translationValues[1] * scaleFactor + rotationValues[1]
        val backLeftPower = translationValues[2] * scaleFactor + rotationValues[2]
        val backRightPower = translationValues[3] * scaleFactor + rotationValues[3]
        frontLeftMotor?.power = frontLeftPower
        frontRightMotor?.power = frontRightPower
        backLeftMotor?.power = backLeftPower
        backRightMotor?.power = backRightPower
    }

    private fun clipMotorPower(p: Double): Double {
        return Range.clip(p, -1.0, 1.0)
    }
}