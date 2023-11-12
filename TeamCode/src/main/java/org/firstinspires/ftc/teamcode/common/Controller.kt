package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.Gamepad

/**
 * Enum control class for the different button controls on the gamepad.
 * Used for adding additional abstraction to the current gamepad control system.
 */
enum class Controller {
    A, B, X, Y, START, BACK, DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT, LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON, NONE;

    companion object {
        fun isSelected(gamepad: Gamepad, buttonControl: Controller?): Boolean {
            var buttonPressed = false
            when (buttonControl) {
                DPAD_UP -> buttonPressed = gamepad.dpad_up
                DPAD_DOWN -> buttonPressed = gamepad.dpad_down
                DPAD_LEFT -> buttonPressed = gamepad.dpad_left
                DPAD_RIGHT -> buttonPressed = gamepad.dpad_right
                A ->
                    // Ignore if start is also pressed to avoid triggering when initialising the
                    // controllers
                    if (!gamepad.start) {
                        buttonPressed = gamepad.a
                    }

                B ->
                    // Ignore if start is also pressed to avoid triggering when initialising the
                    // controllers
                    if (!gamepad.start) {
                        buttonPressed = gamepad.b
                    }

                X -> buttonPressed = gamepad.x
                Y -> buttonPressed = gamepad.y
                START -> buttonPressed = gamepad.start
                BACK -> buttonPressed = gamepad.back
                LEFT_BUMPER -> buttonPressed = gamepad.left_bumper
                RIGHT_BUMPER -> buttonPressed = gamepad.right_bumper
                LEFT_STICK_BUTTON -> buttonPressed = gamepad.left_stick_button
                RIGHT_STICK_BUTTON -> buttonPressed = gamepad.right_stick_button

                NONE -> {}
                else -> {}
            }
            return buttonPressed
        }

        /**
         * Map an array of arguments to controller buttons.
         * @author Lucas Bubner, 2023
         */
        fun <T> mapArgs(args: Array<out T>): HashMap<T, Controller> {
            // Map strings of args to every controller enum in order
            if (args.size >= values().size) {
                throw IllegalArgumentException("Controller: Number of args exceeds number of possible gamepad buttons (14).")
            }
            val map = HashMap<T, Controller>()
            for (i in args.indices) {
                // For every arg, map it to the corresponding enum
                map[args[i]] = values()[i]
                if (args[i] is OpModeSelection) {
                    (args[i] as OpModeSelection).assignedButton = values()[i]
                }
            }
            return map
        }

        /**
         * Convert the gamepad movement values to a Cartesian robot vector.
         * This is done by un-rotating (90 deg) the vectors provided by the controller.
         * @author Lucas Bubner, 2023
         */
        fun makeVector(x: Double, y: Double, r: Double): RobotVector {
            return RobotVector(x, -y, r)
        }
    }
}