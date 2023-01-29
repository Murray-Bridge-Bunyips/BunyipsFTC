package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.hardware.Gamepad

enum class ButtonControl {
    DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT, A, B, X, Y, START, BACK, LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON, NONE;

    companion object {
        fun isSelected(gamepad: Gamepad, buttonControl: ButtonControl?): Boolean {
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
    }
}