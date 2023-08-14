package org.firstinspires.ftc.teamcode.jerry.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.IMUOp
import org.firstinspires.ftc.teamcode.common.Odometer
import org.firstinspires.ftc.teamcode.common.RelativeVector
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.jerry.components.JerryDrive
import org.firstinspires.ftc.teamcode.common.RobotVector
import kotlin.math.abs

/**
 * Revamped autonomous drive to use vector-based movement.
 * @param T The type of vector to use, must be a subclass of [RobotVector].
 * @distance_mm The distance to travel in millimetres. Note if using a vector that is not one of four
 *              simple axis directions, the dead wheels will not be able to track the distance and
 *              will ignore this value.
 * @param vector The vector to travel in.
 * @author Lucas Bubner, 2023
 */
class JerryVectorDriveTask<T>(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: JerryDrive?,
    private val imu: IMUOp?,
    private val x: Odometer?,
    private val y: Odometer?,
    private val distanceMM: Double,
    vector: T,
    private var power: Double,
    private val tolerance: Double = 2.0 // Optional tolerance
) : Task(opMode, time), TaskImpl {

    private var normalisedVector: RobotVector = when (vector) {
        is RelativeVector -> vector.vector
        is RobotVector -> vector
        else -> throw IllegalArgumentException("Vector must be a subclass of RobotVector")
    }
    private var direction: RelativeVector

    init {
        try {
            assert(drive != null)
        } catch (e: AssertionError) {
            opMode.addTelemetry(
                "Failed to initialise a drive task as the drive system is unavailable.",
                true
            )
        }

        // Ensure that the robot moves correctly and is not fed with negative values
        power = abs(power)

        // Normalise vector by desired speed
        normalisedVector.setXY(power)

        // Convert vector to relative vector
        direction = if (vector is RobotVector) {
            RelativeVector.convert(normalisedVector)
        } else {
            vector as RelativeVector
        }
    }

    override fun isFinished(): Boolean {
        when (direction) {
            RelativeVector.LEFT -> {
                return super.isFinished() || (x != null && x.travelledMM() >= distanceMM)
            }
            RelativeVector.RIGHT -> {
                return super.isFinished() || (x != null && x.travelledMM() <= distanceMM)
            }
            RelativeVector.FORWARD -> {
                return super.isFinished() || (y != null && y.travelledMM() >= distanceMM)
            }
            RelativeVector.BACKWARD -> {
                return super.isFinished() || (y != null && y.travelledMM() <= distanceMM)
            }
            else -> {
                // Cannot calculate if the dead wheels are not available or if the direction can't
                // be tracked through the two dead wheels
                return super.isFinished()
            }
        }
    }

    override fun init() {
        super.init()
        imu?.startCapture()
        x?.track()
        y?.track()
    }

    override fun run() {
        if (isFinished()) {
            drive?.deinit()
            return
        }

//        normalisedVector.r = imu?.getRPrecisionSpeed(0.0, 2.0) ?: 0.0
//        val correctedVector = imu?.getCorrectedVector(normalisedVector) ?: normalisedVector
        drive?.setVector(normalisedVector)

        // Encoders will continue to track automatically
        drive?.update()
        imu?.tick()

        // Add telemetry of current operation
        opMode.addTelemetry(
            "Distance progress: ${
                if (direction == RelativeVector.LEFT || direction == RelativeVector.RIGHT) {
                    String.format("%.2f", x?.travelledMM())
                } else if (direction == RelativeVector.FORWARD || direction == RelativeVector.BACKWARD) {
                    String.format("%.2f", y?.travelledMM())
                } else {
                    "N/A"
                }
            }/$distanceMM"
        )

        opMode.addTelemetry(
            "Target vector: ${
                String.format("%.2f", normalisedVector.x)
            }, ${
                String.format("%.2f", normalisedVector.y)
            }, ${
                String.format("%.2f", normalisedVector.r)
            }"
        )
    }
}