package org.firstinspires.ftc.teamcode.lisa.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

/**
 * Full-featured task for driving to a specific distance, with fail safes in case configuration is not available.
 * Mirror of JerryPrecisionDriveTask, accustomed for the components of the minibot instead of deadwheels.
 * @author Lucas Bubner, 2023
 */
class LisaPrecisionDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: LisaDrive?,
    private val imu: IMUOp?,
    private val distance_mm: Double,
    private var power: Double, // Power is used to determine direction
    private val tolerance: Double = 3.0 // Optional tolerance can be specified if 3 degrees is inadequate
) : Task(opMode, time), TaskImpl {
    private var imuFault = false

    override fun isFinished(): Boolean {
        return super.isFinished() || drive?.reachedGoal(distance_mm) == true
    }

    override fun init() {
        super.init()
        if (imu == null || !imu.selfTest()) {
            imuFault = true
            return
        }
        imu.startCapture()
        drive?.resetEncoders()
        drive?.setEncoders(true)
    }

    override fun run() {
        if (isFinished()) {
            drive?.setPower(0.0, 0.0)
            drive?.update()
            imu?.resetCapture()
            return
        }

        val correction = imu?.getRPrecisionSpeed(0.0, tolerance) ?: 0.0
        drive?.setPower(power + correction, power - correction)

        drive?.update()
        imu?.tick()

        opMode.telemetry.addLine("PrecisionDrive is active with IMU fault? $imuFault")
        opMode.telemetry.addLine("Distance progress: ${drive?.getTravelledDist().toString()}/$distance_mm")
        opMode.telemetry.addLine(
            "Axis correction: ${imu?.capture?.minus(tolerance) ?: "N/A"} <= ${imu?.heading ?: "N/A"} <= ${
                imu?.capture?.plus(
                    tolerance
                ) ?: "N/A"
            }"
        )
    }

}