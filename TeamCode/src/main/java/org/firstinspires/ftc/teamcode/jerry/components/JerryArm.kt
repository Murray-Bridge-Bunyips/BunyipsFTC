package org.firstinspires.ftc.teamcode.jerry.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Arm controller for Jerry bot. Includes a claw and a lift.
 * @author Lucas Bubner, 2022
 */
@Deprecated("Use JerryLift instead as the index-based system is not ideal for use.")
class JerryArm(
    opMode: BunyipsOpMode,
    private var claw: Servo,
    private var arm1: DcMotorEx,
    private var arm2: DcMotorEx,
    private var limit: TouchSensor
) : BunyipsComponent(opMode) {
    private var offset: Int = 0
    private var liftIndex = 0
    private var liftPower: Double = 0.0
    private val motors = arrayOf(arm1, arm2)
    private var isCalibrating = false
    private var alreadyCalibrated = false

    init {
        for (motor in motors) {
            motor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        }

        // When the arm is first initialised, calibrate it (as the arm may not already be zeroed)
        // This will also set the motors to their correct mode.
        liftCalibrate()
    }

    private fun liftCalibrate() {
        liftCalibrate(0.1)
    }

    /**
     * Use the configured hard-limit switch to calibrate and zero out the encoders. Will also use
     * no-movement detection in the event the hard-limit switch isn't working correctly.
     * This will lockout the update method while it runs.
     */
    @SuppressLint("DefaultLocale")
    fun liftCalibrate(power: Double) {
        // Lock down the instance to have full control over the arm
        isCalibrating = true
        alreadyCalibrated = true
        for (motor in motors) {
            // Ensure motors are running in the correct mode
            motor.mode = RunMode.RUN_USING_ENCODER

            // Set the motor power to something low as we will be descending and don't want to damage
            // any gears or parts with excessive force. Gravity should do the heavy work.
            motor.power = -power
        }

        // Now we wait for the limit switch to be hit, or if the movement of the arm stops for long enough,
        // which means either the button failed or the motors had already triggered
        // the bounds detection. Either way works and it won't hurt to use both, incase the limit
        // switch breaks for some reason. Press right bumper to cancel the loop.
        // Using reversed operation as pressing results in the limit switch reporting false
        var e = 0
        while (!limit.isPressed && !opMode.gamepad2.right_bumper) {
            opMode.addTelemetry(
                String.format(
                    "ARM IS CALIBRATING... PRESS GAMEPAD2.RIGHT_BUMPER TO CANCEL! ENCODER VALUES: %d, %d",
                    arm1.currentPosition,
                    arm2.currentPosition
                )
            )
            opMode.telemetry.update()

            // Check if the delta is above 0 for 10 consecutive loops, if so, we have hit the limit
            val prev = ((motors[0].currentPosition + motors[1].currentPosition) / 2)
            val delta = ((motors[0].currentPosition + motors[1].currentPosition) / 2) - prev
            if (delta >= 0) {
                e++
            } else {
                e = 0
            }

            if (e > 10) break
        }
        for (motor in motors) {
            // Finally, we reset the motors and we are now zeroed out again.
            motor.power = 0.0
            motor.mode = RunMode.STOP_AND_RESET_ENCODER
            motor.targetPosition = 0
        }

        // Prevent 'slingshotting' the arm back to whatever position the arm was originally in,
        // then return arm control permission back to the instance.
        liftIndex = 0
        isCalibrating = false
        opMode.addTelemetry("Arm has been calibrated.")
    }

    /**
     * Call to open claw system and allow picking up of items (allow servos to swing to open)
     */
    fun clawOpen() {
        claw.position = 0.0
        opMode.addTelemetry("Claws are opening...")
    }

    /**
     * Call to close claw system and pick up an object by setting the target position to the closed
     * position. Using Servo mode as opposed to CRServo mode.
     */
    fun clawClose() {
        claw.position = 1.0
        opMode.addTelemetry("Claws are closing...")
    }

    /**
     * Lift arm motor up by one index of the LIFT_POSITIONS index
     * Adding offset ensures that the offset is taken into account when adjusting position
     */
    fun liftUp() {
        liftIndex++
        if (liftIndex > LIFT_POSITIONS.size - 1) {
            liftIndex = LIFT_POSITIONS.size - 1
        }
        for (motor in motors) {
            motor.targetPosition = LIFT_POSITIONS[liftIndex] + offset
        }
    }

    /**
     * Lift arm motor down by one index of the LIFT_POSITIONS index
     * Adding offset ensures that the offset is taken into account when adjusting position
     */
    fun liftDown() {
        liftIndex--
        if (liftIndex <= 0) {
            liftIndex = 0
        }
        for (motor in motors) {
            motor.targetPosition = LIFT_POSITIONS[liftIndex] + offset
        }
    }

    /**
     * Set arm motor to index 0 of the LIFT_POSITIONS index (lowest position), then calls an
     * automatic calibration of the motors by zeroing out the encoders at the limit switch.
     */
    fun liftReset() {
        liftIndex = 0
        offset = 0
        liftCalibrate()
    }

    /**
     * Manually control arm speed by setting an 'offset' that will be reflected in the target position
     * @param o Offset to add to the target position, multiplied by 100 to allow for faster control
     */
    fun liftAdjustOffset(o: Double) {
        offset += (o * 100).toInt()
        update()
    }

    /**
     * Set arm power for positional movements
     * @param power desired power
     */
    fun liftSetPower(power: Double) {
        // Cap powers at -1 to 1
        liftPower = power.coerceIn(-1.0, 1.0)
    }

    /**
     * Manually set arm position index. Returns a boolean indicating if the position was set or not.
     * @param pos Arm position index in LIFT_POSITIONS
     */
    fun liftSetPosition(pos: Int): Boolean {
        // Avoid positions that are outside of the length of the array itself
        if (pos < 0 || pos >= LIFT_POSITIONS.size) return false
        liftIndex = pos
        return true
    }

    /**
     * Call to update desired arm motor speeds
     */
    @SuppressLint("DefaultLocale")
    fun update() {
        // If the arm is in the process of calibration, don't interrupt it
        if (isCalibrating) return

        // Ensure we don't get stuck in a loop of calibration
        if (limit.isPressed && !alreadyCalibrated) liftCalibrate()
        if (!limit.isPressed) alreadyCalibrated = false

        // Over-bounds detection algorithm for offset
        val target = LIFT_POSITIONS[liftIndex] + offset
        val current = ((motors[0].currentPosition + motors[1].currentPosition) / 2)
        if (current + MOTOR_OFFSET_LIMIT < target || current - MOTOR_OFFSET_LIMIT > target) {
            offset = 0
        }
        if (current + MOTOR_EMER_LIMIT < target || current - MOTOR_EMER_LIMIT > target) {
            // Take manual control to correct an over-swung arm
            liftCalibrate(0.5)
        }

        opMode.addTelemetry(
            String.format(
                "Arms (pos1, pos2, index, offset): %d, %d, %s, %s",
                arm1.currentPosition,
                arm2.currentPosition,
                liftIndex,
                offset
            )
        )

        for (motor in motors) {
            motor.power = liftPower
            motor.mode = RunMode.RUN_TO_POSITION
        }
    }

    /*
        LIFT_POSITIONS index
        0: Zero height
        1: Cone obtaining/neutral height
        2: Above cone, allows for drive to position cone under arm
        3: Pole position 1
        4: Pole position 2
        5: Pole position 3
        6: Maximum theoretical safe height
    */
    companion object {
        private val LIFT_POSITIONS = intArrayOf(0, 25, 50, 85, 130, 200, 230)
        private const val MOTOR_OFFSET_LIMIT = 250
        private const val MOTOR_EMER_LIMIT = MOTOR_OFFSET_LIMIT * 2
    }
}