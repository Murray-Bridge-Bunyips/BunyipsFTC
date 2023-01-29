package org.firstinspires.ftc.teamcode.lisa.tasks

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.Velocity
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

// PrecisionDrive Algorithm II, Lucas Bubner, 2022
class LisaPrecisionDriveTask(
    opMode: BunyipsOpMode,
    private val drive: LisaDrive?,
    private val imu: BNO055IMU?,
    time: Double,
    distanceCM: Double,
    private val speed: Double,
    tolerance: Double,
    reduction: Double
) : Task(opMode, time), TaskImpl {
    private val distanceCM: Double
    private val tolerance: Float
    private val reduction: Float
    private var captureAngle: Orientation? = null

    init {
        this.tolerance = tolerance.toFloat()
        this.reduction = reduction.toFloat()

        // UltraPlanetary HD Hex Motors revolution count
        // TODO: Check these formulas
        val TICKS_PER_REVOLUTION = 28.0
        val WHEEL_DIAMETER_CM = 8.5
        this.distanceCM =
            distanceCM * 10 * TICKS_PER_REVOLUTION / (WHEEL_DIAMETER_CM / 10 * Math.PI)
    }

    override fun init() {
        super.init()
        drive!!.setEncoder(true)
        imu!!.startAccelerationIntegration(Position(), Velocity(), 50)
        captureAngle =
            imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)
        // Start driving with a distance goal
        drive.setTargetPosition(distanceCM, distanceCM)
        drive.setPower(speed, speed)
        drive.update()
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || drive!!.targetPositionReached()
    }

    override fun run() {

        // Stop if we've finished the distance or time goal
        if (isFinished()) {
            drive!!.setPower(0.0, 0.0)
            drive.update()
            drive.setEncoder(false)
            imu!!.stopAccelerationIntegration()
            return
        }

        // Update angles and see if we need to adjust them
        val currentAngle =
            imu!!.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)

        // Account for if we're travelling backwards
        val reducedspeed = if (speed > 0) speed - reduction else speed + reduction
        if (currentAngle.firstAngle > captureAngle!!.firstAngle + tolerance) {
            // Rotate CCW
            drive!!.setPower(speed, reducedspeed)
            drive.update()
        } else if (currentAngle.firstAngle < captureAngle!!.firstAngle - tolerance) {
            // Rotate CW
            drive!!.setPower(reducedspeed, speed)
            drive.update()
        } else {
            // Continue straight
            drive!!.setPower(speed, speed)
            drive.update()
        }

        // Telemetry data
        opMode.telemetry.addData("Original Angle", captureAngle!!.firstAngle)
        opMode.telemetry.addData("Current Angle", currentAngle.firstAngle)
    }
}