package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.Odometer
import org.firstinspires.ftc.teamcode.common.RelativeVector
import org.firstinspires.ftc.teamcode.common.RobotVector
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import kotlin.math.abs

/**
 * Full-featured task for driving to a specific distance, with fail safes in case configuration is not available.
 * This supports movement throughout the 2D plane, and can be used to move in any one direction
 * @author Lucas Bubner, 2023
 */
class JerryPrecisionDriveTask<T>(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive?,
    private val imu: IMUOp?,
    private val x: Odometer?,
    private val y: Odometer?,
    private val distance_mm: Double,
    private val direction: T,
    private var power: Double,
    private val tolerance: Double = 3.0 // Optional tolerance can be specified if 3 degrees is inadequate
) : Task(opMode, time), TaskImpl {

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

        if (direction !is RelativeVector) {
            throw NotImplementedError("Currently not supported.")
        }

        // TODO: Normalise vectors with motor speed
    }

    override fun isFinished(): Boolean {
        // Check if the task is done by checking if it has timed out in the super call or if the target has been reached
        // by the respective deadwheel. If the deadwheel is not available, then we cannot check if the target has been
        // reached, so we will just rely on the timeout.
        val evaluating = if (direction == RelativeVector.LEFT || direction == RelativeVector.RIGHT) {
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
        if (isFinished()) {
            drive?.deinit()
            return
        }

        // TODO: Use vectors instead of this
        // I will do this after the scrimmage as this may be a breaking change
        // This will also allow for more complex movement, such as 8-way movement
        drive?.setSpeedXYR(
            if (direction == RelativeVector.LEFT) -power else if (direction == RelativeVector.RIGHT) power else 0.0,
            if (direction == RelativeVector.FORWARD) -power else if (direction == RelativeVector.BACKWARD) power else 0.0,
            imu?.getRPrecisionSpeed(0.0, tolerance) ?: 0.0
        )

        // Encoders will continue to track automatically
        drive?.update()
        imu?.tick()

        // Add telemetry of current operation
        opMode.addTelemetry(
            "Distance progress: ${
                if (direction == RelativeVector.LEFT || direction == RelativeVector.RIGHT) {
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
}