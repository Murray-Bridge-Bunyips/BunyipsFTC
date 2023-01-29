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

// TODO: Check if negative values are needed for CW rotation, and change tolerance code
class LisaIMUTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: LisaDrive,
    private val speed: Double,
    private val imu: BNO055IMU,
    angle: Double
) : Task(opMode, time), TaskImpl {
    private val angle: Float
    private var angles: Orientation? = null

    init {
        this.angle = angle.toFloat()
    }

    override fun init() {
        super.init()
        imu.startAccelerationIntegration(Position(), Velocity(), 50)
        angles =
            imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)
        drive.setPower(speed, -speed)
        drive.update()
    }

    override fun run() {
        if (isFinished()) {
            drive.setPower(0.0, 0.0)
            drive.update()
            imu.stopAccelerationIntegration()
            return
        }
        val currentAngles =
            imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)
        opMode.telemetry.addData("Original Angle", angles!!.firstAngle)
        opMode.telemetry.addData("Current Angle", currentAngles.firstAngle)
        // 15 degree automatic tolerance
        if (currentAngles.firstAngle > angles!!.firstAngle + if (angle > 0) angle - 15 else angle + 15) {
            drive.setPower(0.0, 0.0)
            drive.update()
            taskFinished = true
        }
    }
}