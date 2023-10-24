package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.Odometer
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import kotlin.math.abs

/**
 * Full-featured task for driving to a specific distance, with fail safes in case configuration is not available.
 * This supports movement throughout the 2D plane, and can be used to move in any one direction
 *
 * This system is being replaced by the new vector system in [JerryVectorDriveTask], which will allow for more complex movement,
 * but this is still maintained due to it's rigorous testing and reliability.
 *
 * @author Lucas Bubner, 2023
 */
class JerryPrecisionDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive?,
    private val imu: IMUOp?,
    private val x: Odometer?,
    private val y: Odometer?,
    private val distance_mm: Double,
    private val direction: Directions,
    private var power: Double,
    private val tolerance: Double = 3.0 // Optional tolerance can be specified if 3 degrees is inadequate
) : Task(opMode, time), AutoTask {

    init {
        try {
            assert(drive != null)
        } catch (e: AssertionError) {
            opMode.addTelemetry(
                "Failed to initialise a drive task as the drive system is unavailable.",
                true
            )
        }
        // Use absolute values of power to ensure that the robot moves correctly and is not fed with negative values
        // This is because the task will handle the power management and determine whether the value
        // to the motor should be negative or not
        this.power = abs(power)
    }

    enum class Directions {
        LEFT, RIGHT, FORWARD, BACKWARD
    }

    override fun isFinished(): Boolean {
        // Check if the task is done by checking if it has timed out in the super call or if the target has been reached
        // by the respective deadwheel. If the deadwheel is not available, then we cannot check if the target has been
        // reached, so we will just rely on the timeout.
        val evaluating = if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            x?.travelledMM()
        } else {
            y?.travelledMM()
        }
        return super.isFinished() || evaluating != null && evaluating >= distance_mm
    }

    override fun init() {
        super.init()
        // Capture vectors and start tracking
        imu?.startCapture()
        x?.track()
        y?.track()
    }

    override fun run() {
        drive?.setSpeedUsingController(
            if (direction == Directions.LEFT) -power else if (direction == Directions.RIGHT) power else 0.0,
            if (direction == Directions.FORWARD) -power else if (direction == Directions.BACKWARD) power else 0.0,
            imu?.getRPrecisionSpeed(0.0, tolerance) ?: 0.0
        )

        // Encoders will continue to track automatically
        drive?.update()
        imu?.tick()

        // Add telemetry of current operation
        opMode.addTelemetry(
            "Distance progress: ${
                if (direction == Directions.LEFT || direction == Directions.RIGHT) {
                    String.format("%.2f", x?.travelledMM())
                } else {
                    String.format("%.2f", y?.travelledMM())
                }
            }/$distance_mm"
        )

        opMode.addTelemetry(
            "Axis correction: ${
                String.format("%.2f", imu?.capture?.minus(tolerance))
            } <= ${
                String.format("%.2f", imu?.heading)
            } <= ${
                String.format("%.2f", imu?.capture?.plus(tolerance))
            }"
        )

    }

    override fun onFinish() {
        drive?.stop()
    }
}