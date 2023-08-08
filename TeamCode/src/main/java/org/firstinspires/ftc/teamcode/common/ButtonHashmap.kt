package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Convenience object to ask for user input from a controller in order to determine a pre-determined
 * set of instructions before Autonomous.
 *
 * Updated to use dynamic button mapping and generics 04/08/23.
 */
object ButtonHashmap {
    fun <T> map(opMode: BunyipsOpMode, vararg opmodes: T): T {
        if (opmodes.isEmpty()) {
            throw IllegalArgumentException("Must provide at least one operation mode.")
        }

        val buttons: HashMap<T, ButtonControl> = ButtonControl.mapArgs(opmodes)

        // Default button, use the first vararg as the default
        var selectedButton: ButtonControl = buttons.values.first()
        // User selected operation mode
        var selectedOpMode: T? = null

        // Disable auto clear if it is enabled
        val autoClearState = opMode.getTelemetryAutoClear()
        opMode.setTelemetryAutoClear(false)

        // Log available buttons to press
        opMode.addTelemetry("PRESSING ONE OF THE FOLLOWING GAMEPAD1 BUTTONS WILL INITIALISE THE ROBOT TO THE RELEVANT OPERATION MODE:")
        for ((name, button) in buttons) {
            opMode.addTelemetry(String.format("%s: %s", button.name, name))
        }

        opMode.addTelemetry(
            String.format(
                "IF NO BUTTON IS PRESSED WITHIN 3 SECONDS, *%s* WILL BE RUN",
                selectedButton
            )
        )

        // Must manually call telemetry pushes as the BYO is no longer handling it
        opMode.telemetry.update()

        val timeout = ElapsedTime()
        while (timeout.seconds() < 3.0 && selectedOpMode == null && opMode.opModeInInit()) {
            for ((str, button) in buttons) {
                if (ButtonControl.isSelected(opMode.gamepad1, button)) {
                    selectedButton = button
                    selectedOpMode = str
                    break
                }
            }
            opMode.idle()
        }

        opMode.addTelemetry(
            String.format(
                "%s was selected: Running '%s'",
                selectedButton.name,
                selectedOpMode ?: buttons.keys.first()
            )
        )

        opMode.addTelemetry("IF THIS SELECTION IS INCORRECT, QUIT THE OPMODE AND RESELECT NOW")
        opMode.telemetry.update()
        opMode.setTelemetryAutoClear(autoClearState)

        // Default to the first OpMode if none were selected
        return selectedOpMode ?: buttons.keys.first()
    }
}