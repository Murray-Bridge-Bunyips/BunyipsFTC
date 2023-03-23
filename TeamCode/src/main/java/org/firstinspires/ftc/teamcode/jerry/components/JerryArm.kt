package org.firstinspires.ftc.teamcode.jerry.components

import android.annotation.SuppressLint
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.teamcode.common.BunyipsComponent
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

class JerryArm(
    opMode: BunyipsOpMode,
    var claw1: Servo?,
    var claw2: Servo?,
    var arm1: DcMotorEx?,
    var arm2: DcMotorEx?,
    var limit: TouchSensor?
) : BunyipsComponent(opMode) {
    private var liftIndex = 0
    private var liftPower: Double
    private val motors = arrayOfNulls<DcMotorEx>(2)
    private var isCalibrating = false
    private var alreadyCalibrated = false

    init {
        motors[0] = arm1
        motors[1] = arm2
        liftPower = 0.0
        try {
            assert(claw1 != null && claw2 != null && arm1 != null && arm2 != null)
        } catch (e: AssertionError) {
            opMode.telemetry.addLine("Failed to initialise Arm System, check config for all components.")
        }
        
        // Set directions of motors so they move the correct way
        claw1?.direction = Servo.Direction.FORWARD
        claw2?.direction = Servo.Direction.REVERSE
        arm1?.direction = Direction.FORWARD
        arm2?.direction = Direction.REVERSE
        for (motor in motors) {
            motor?.zeroPowerBehavior = ZeroPowerBehavior.BRAKE
        }

        // When the arm is first initialised, calibrate it (as the arm may not already be zeroed)
        // This will also set the motors to their correct mode.
        liftCalibrate()
    }

    /**
     * Use the configured hard-limit switch to calibrate and zero out the encoders. Will also use
     * no-movement detection in the event the hard-limit switch isn't working correctly.
     * This will lockout the update method while it runs.
     */
    @SuppressLint("DefaultLocale")
    fun liftCalibrate() {
        // Lock down the instance to have full control over the arm
        isCalibrating = true
        alreadyCalibrated = true
        for (motor in motors) {
            // Ensure motors are running in the correct mode
            motor!!.mode = RunMode.RUN_USING_ENCODER

            // Set the motor power to something low as we will be descending and don't want to damage
            // any gears or parts with excessive force. Gravity should do the heavy work.
            motor.power = -0.1
        }

        // Now we wait for the limit switch to be hit, or if the movement of the arm stops for long enough,
        // which means either the button failed or the motors had already triggered
        // the bounds detection. Either way works and it won't hurt to use both, incase the limit
        // switch breaks for some reason. Press right bumper to cancel the loop.
        // Using reversed operation as pressing results in the limit switch reporting false
        var e = 0
        while (!limit!!.isPressed && !opMode!!.gamepad2.right_bumper) {
            opMode.telemetry.addLine(
                String.format(
                    "ARM IS CALIBRATING... PRESS GAMEPAD2.RIGHT_BUMPER TO CANCEL! ENCODER VALUES: %d, %d",
                    arm1?.currentPosition,
                    arm2?.currentPosition
                )
            )
            opMode.telemetry.update()

            // Check if the delta is above 0 for 10 consecutive loops, if so, we have hit the limit
            val prev = ((motors[0]!!.currentPosition + motors[1]!!.currentPosition) / 2)
            val delta = ((motors[0]!!.currentPosition + motors[1]!!.currentPosition) / 2) - prev
            if (delta >= 0) {
                e++
            } else {
                e = 0
            }

            if (e > 10) break
        }
        for (motor in motors) {
            // Finally, we reset the motors and we are now zeroed out again.
            motor!!.power = 0.0
            motor.mode = RunMode.STOP_AND_RESET_ENCODER
            motor.targetPosition = 0
        }

        // Prevent 'slingshotting' the arm back to whatever position the arm was originally in,
        // then return arm control permission back to the instance.
        liftIndex = 0
        isCalibrating = false
        opMode!!.telemetry.addLine("Arm has been calibrated.")
    }

    /**
     * Call to open claw system and allow picking up of items (allow servos to swing to open)
     */
    fun clawOpen() {
        claw1?.position = 0.0
        claw2?.position = 1.0
        opMode!!.telemetry.addLine("Claws are opening...")
    }

    /**
     * Call to close claw system and pick up an object by setting the target position to the closed
     * position. Using Servo mode as opposed to CRServo mode.
     */
    fun clawClose() {
        claw1?.position = 0.0
        claw2?.position = 0.0
        opMode!!.telemetry.addLine("Claws are closing...")
    }

    /**
     * Lift arm motor up by one index of the LIFT_POSITIONS index
     */
    fun liftUp() {
        liftIndex++
        if (liftIndex > LIFT_POSITIONS.size - 1) {
            liftIndex = LIFT_POSITIONS.size - 1
        }
        for (motor in motors) {
            motor?.targetPosition = LIFT_POSITIONS[liftIndex]
        }
    }

    /**
     * Lift arm motor down by one index of the LIFT_POSITIONS index
     */
    fun liftDown() {
        liftIndex--
        if (liftIndex <= 0) {
            liftIndex = 0
        }
        for (motor in motors) {
            motor?.targetPosition = LIFT_POSITIONS[liftIndex]
        }
    }

    /**
     * Set arm motor to index 0 of the LIFT_POSITIONS index (lowest position), then calls an
     * automatic calibration of the motors by zeroing out the encoders at the limit switch.
     */
    fun liftReset() {
        liftIndex = 0
        liftCalibrate()
    }

    /**
     * Manually control arm speed by setting the position manually through variable input
     * Try not to call this method, and instead use the Up/Down methods. Should only be used
     * for manual debugging of the arm motors.
     */
    fun liftControl(o: Double) {
        var offset = o
        offset *= 100.0
        val positions = (arm1?.currentPosition?.plus(arm2?.currentPosition!!))?.div(2)
        for (motor in motors) {
            if (positions != null) {
                motor?.targetPosition = (positions - offset).toInt()
            }
        }
    }

    /**
     * Set arm power for positional movements
     * @param power desired power
     */
    fun liftSetPower(power: Double) {
        liftPower = power
    }

    /**
     * Manually set arm position index
     * @param pos Arm position index in LIFT_POSITIONS
     */
    fun liftSetPosition(pos: Int) {
        // Avoid positions that are outside of the length of the array itself
        if (pos < 0 || pos >= LIFT_POSITIONS.size) return
        liftIndex = pos
    }

    /**
     * Call to update desired arm motor speeds
     */
    @SuppressLint("DefaultLocale")
    fun update() {
        // If the arm is in the process of calibration, don't interrupt it
        if (isCalibrating) return

        // To make sure we don't accidentally get stuck in a loop of infinite calibration
        // Also zeroes out the encoders if we hit the switch.
        if (limit!!.isPressed && !alreadyCalibrated) liftCalibrate()
        if (!limit!!.isPressed) alreadyCalibrated = false
        opMode!!.telemetry.addLine(
            String.format(
                "Arms (pos1, pos2, index): %d, %d, %s",
                arm1?.currentPosition,
                arm2?.currentPosition,
                liftIndex
            )
        )
        for (motor in motors) {
            motor?.power = liftPower
            motor?.mode = RunMode.RUN_TO_POSITION
        }
    }

    /*
        LIFT_POSITIONS index
        0: Cone obtaining/neutral height
        1: Above cone, allows for drive to position cone under arm
        2: Pole position 1
        3: Pole position 2
        4: Pole position 3
    */
    companion object {
        // These arm positions have been roughly calibrated, but are not fully accurate
        private val LIFT_POSITIONS = intArrayOf(0, 20, 80, 130, 150, 175, 200, 230)
        // i am contributing nothing to the project -lachlan
        // i am contributing a little bit to the project -lachlan
        // i am contributing a lot to the project -lachlan
        // i am contributing everything to the project -lachlan
        // i am contributing nothing to the project -lachlan
    }
}