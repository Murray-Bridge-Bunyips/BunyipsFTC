package org.firstinspires.ftc.teamcode.jerry.components

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Improved and refactored arm control system for Jerry robot, using relative capture instead of index-based movement
 * @author Lucas Bubner, 2023
 */
class JerryLift(
    opMode: BunyipsOpMode,
    private var claw: Servo,
    private var arm1: DcMotorEx,
    private var arm2: DcMotorEx,
    private var limit: TouchSensor
) : BunyipsComponent(opMode) {
    var power: Double = 0.2
        set(power) {
            field = power.coerceIn(-1.0, 1.0)
        }
    private val motors = arrayOf(arm1, arm2)
    private var targetPosition: Double = 0.0
    private var holdPosition: Int? = null
    private var isResetting: Boolean = false

    init {
        // Set directions of motors so they move the correct way
        claw.direction = Servo.Direction.FORWARD
        arm1.direction = DcMotorSimple.Direction.FORWARD
        arm2.direction = DcMotorSimple.Direction.REVERSE
        for (motor in motors) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            motor.power = power
        }
        // Run an initial calibration
        reset()
    }

    /**
     * Reset the arm back to a zeroed position with bounds detection/limit switch cutoff.
     * This will ensure that a saved position is still accurate even after encoder drift.
     */
    fun reset() {
        isResetting = true
        close()
        for (motor in motors) {
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            // Allow gravity and light motor power to pull the arm down
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            motor.power = -0.1
        }

        var e = 0
        while (!limit.isPressed && !opMode.gamepad2.right_bumper) {
            opMode.addTelemetry(
                String.format(
                    "ARM IS RESETTING... PRESS GAMEPAD2.RIGHT_BUMPER TO CANCEL! ENCODER VALUES: %d, %d",
                    arm1.currentPosition,
                    arm2.currentPosition
                )
            )
            opMode.telemetry.update()

            // Check if the delta is above 0 for 30 consecutive loops, if so, we have hit the limit
            val prev = ((motors[0].currentPosition + motors[1].currentPosition) / 2)
            val delta = ((motors[0].currentPosition + motors[1].currentPosition) / 2) - prev
            if (delta >= 0) {
                e++
            } else {
                e = 0
            }

            if (e > 30) break
        }

        for (motor in motors) {
            // Finally, we reset the motors and we are now zeroed out again.
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.targetPosition = 0
        }

        isResetting = false
        targetPosition = 0.0
    }

    /**
     * Call to open claw system and allow picking up of items (allow servos to swing to open)
     */
    fun open() {
        claw.position = 0.0
        opMode.addTelemetry("Claws are opening...")
    }

    /**
     * Call to close claw system and pick up an object by setting the target position to the closed
     * position. Using Servo mode as opposed to CRServo mode.
     */
    fun close() {
        claw.position = 1.0
        opMode.addTelemetry("Claws are closing...")
    }

    /**
     * Primary function to move the arm during TeleOp, takes in a controller input delta and adjusts the arm position
     */
    fun adjust(delta: Double) {
        if (targetPosition + delta > HARD_LIMIT) {
            return
        }
        if (delta < 0 && claw.position == 0.0) {
            // Auto close claw when it is moving upwards
            close()
        }
        targetPosition += -delta * 0.5
    }

    /**
     * Autonomous method of adjusting the arm position, directly setting the target position
     */
    fun set(target: Int) {
        if (targetPosition > HARD_LIMIT) {
            return
        }
        targetPosition = target.toDouble()
    }

    /**
     * Set a capture of the arm position
     */
    fun capture() {
        holdPosition = targetPosition.toInt()
        opMode.log("lift captured at $holdPosition")
    }

    /**
     * Return the arm to the last captured position
     */
    fun release() {
        if (holdPosition == null) {
            opMode.log("lift released but no capture found")
            return
        }
        targetPosition = holdPosition?.toDouble() ?: targetPosition
        opMode.log("lift released to $targetPosition")
        holdPosition = null
    }

    fun update() {
        // FIXME: Input latency within this lift system which is above tolerable levels
        if (isResetting) return

        opMode.addTelemetry(
            String.format(
                "Lift (pos1, pos2, target, capture): %d, %d, %s, %s",
                arm1.currentPosition,
                arm2.currentPosition,
                targetPosition.toInt(),
                holdPosition ?: "N/A"
            )
        )

        for (motor in motors) {
            motor.targetPosition = targetPosition.toInt()
            motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        }

        // Ensure the arm does not overswing
        if ((arm1.currentPosition + arm2.currentPosition) / 2 > HARD_LIMIT) {
            targetPosition = (HARD_LIMIT - (HARD_LIMIT / 6)).toDouble()
        }
    }

    companion object {
        private const val HARD_LIMIT = 250
    }
}