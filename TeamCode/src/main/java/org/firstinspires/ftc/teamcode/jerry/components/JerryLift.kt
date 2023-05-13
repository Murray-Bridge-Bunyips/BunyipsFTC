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
    var claw1: Servo?,
    var claw2: Servo?,
    var arm1: DcMotorEx?,
    var arm2: DcMotorEx?,
    var limit: TouchSensor?
) : BunyipsComponent(opMode) {
    var power: Double = 0.2
        set(power) {
            field = power.coerceIn(-1.0, 1.0)
        }
    private val motors = arrayOfNulls<DcMotorEx>(2)
    private var targetPosition: Int
    private var holdPosition: Int
    private var isResetting: Boolean = false

    init {
        motors[0] = arm1
        motors[1] = arm2
        holdPosition = 0
        targetPosition = 0
        try {
            assert(claw1 != null && claw2 != null && arm1 != null && arm2 != null)
        } catch (e: AssertionError) {
            opMode.telemetry.addLine("Failed to initialise Arm System, check config for all components.")
        }

        // Set directions of motors so they move the correct way
        claw1?.direction = Servo.Direction.FORWARD
        claw2?.direction = Servo.Direction.REVERSE
        arm1?.direction = DcMotorSimple.Direction.FORWARD
        arm2?.direction = DcMotorSimple.Direction.REVERSE
        for (motor in motors) {
            motor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
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
        for (motor in motors) {
            motor?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            // Allow gravity and light motor power to pull the arm down
            motor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            motor?.power = 0.1
        }

        var e = 0
        while (!limit!!.isPressed && !opMode!!.gamepad2.right_bumper) {
            opMode.telemetry.addLine(
                String.format(
                    "ARM IS RESETTING... PRESS GAMEPAD2.RIGHT_BUMPER TO CANCEL! ENCODER VALUES: %d, %d",
                    arm1?.currentPosition,
                    arm2?.currentPosition
                )
            )
            opMode.telemetry.update()

            // Check if the delta is above 0 for 15 consecutive loops, if so, we have hit the limit
            val prev = ((motors[0]!!.currentPosition + motors[1]!!.currentPosition) / 2)
            val delta = ((motors[0]!!.currentPosition + motors[1]!!.currentPosition) / 2) - prev
            if (delta >= 0) {
                e++
            } else {
                e = 0
            }

            if (e > 15) break
        }

        for (motor in motors) {
            // Finally, we reset the motors and we are now zeroed out again.
            motor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            motor?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor?.targetPosition = 0
        }

        isResetting = false
    }

    /**
     * Call to open claw system and allow picking up of items (allow servos to swing to open)
     */
    fun open() {
        claw1?.position = 0.0
        claw2?.position = 1.0
        opMode!!.telemetry.addLine("Claws are opening...")
    }

    /**
     * Call to close claw system and pick up an object by setting the target position to the closed
     * position. Using Servo mode as opposed to CRServo mode.
     */
    fun close() {
        claw1?.position = 0.0
        claw2?.position = 0.0
        opMode!!.telemetry.addLine("Claws are closing...")
    }

    /**
     * Primary function to move the arm during TeleOp, takes in a controller input delta and adjusts the arm position
     */
    fun adjust(delta: Double) {
        targetPosition += (-delta * 50).toInt()
    }

    /**
     * Autonomous method of adjusting the arm position, directly setting the target position
     */
    fun set(target: Int) {
        targetPosition = target
    }

    fun update() {
        if (isResetting) return

        opMode!!.telemetry.addLine(
            String.format(
                "Lift (pos1, pos2, target, capture): %d, %d, %s, %s",
                arm1?.currentPosition,
                arm2?.currentPosition,
                targetPosition,
                holdPosition
            )
        )

        for (motor in motors) {
            motor?.power = power
            motor?.mode = DcMotor.RunMode.RUN_TO_POSITION
            motor?.targetPosition = targetPosition
        }
    }
}