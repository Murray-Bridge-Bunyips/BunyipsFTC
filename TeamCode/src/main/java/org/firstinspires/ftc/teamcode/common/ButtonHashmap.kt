package org.firstinspires.ftc.teamcode.common

/**
 * Convenience class to ask for user input from a controller in order to determine a pre-determined
 * set of instructions before Autonomous. Supports up to 4 options and empty strings in the static
 * constructor will be ignored and not shown.
 */
object ButtonHashmap {
    fun map(opMode: BunyipsOpMode, A: String, B: String, Y: String, X: String): ButtonControl {
        val buttonMap = HashMap<ButtonControl, String>()

        // Array of buttons which correlate each button to each task set
        val button1 = ButtonControl.A
        val button2 = ButtonControl.B
        val button3 = ButtonControl.Y
        val button4 = ButtonControl.X

        // Default button
        var selectedButton = ButtonControl.A
        if (!A.isEmpty()) {
            buttonMap[button1] = A
        }
        if (!B.isEmpty()) {
            buttonMap[button2] = B
        }
        if (!Y.isEmpty()) {
            buttonMap[button3] = Y
        }
        if (!X.isEmpty()) {
            buttonMap[button4] = X
        }
        val autoClearState = opMode.telemetry.isAutoClear
        opMode.telemetry.isAutoClear = false
        opMode.telemetry.addLine("PRESSING ONE OF THE FOLLOWING BUTTONS WILL INITIALISE THE ROBOT TO THE RELEVANT OPERATION MODE:")
        for ((key, value) in buttonMap) {
            // System.out.printf("%s: %s%n", es.getKey().name(), es.getValue());
            opMode.telemetry.addLine(String.format("%s: %s", key.name, value))
            if (ButtonControl.isSelected(opMode.gamepad1, key)) {
                selectedButton = key
            }
        }
        opMode.telemetry.addLine(
            String.format(
                "IF NO BUTTON IS PRESSED WITHIN 3 SECONDS, *%s* WILL BE RUN",
                buttonMap[selectedButton]
            )
        )
        opMode.telemetry.update()
        val startTime = System.nanoTime()
        outerLoop@ while (System.nanoTime() < startTime + 3000000000L) {
            for ((key) in buttonMap) {
                if (ButtonControl.isSelected(opMode.gamepad1, key)) {
                    selectedButton = key
                    break@outerLoop
                }
            }
            opMode.idle()
        }
        opMode.telemetry.addLine(
            String.format(
                "%s was selected: Running %s",
                selectedButton.name,
                buttonMap[selectedButton]
            )
        )
        opMode.telemetry.addLine("IF THIS SELECTION IS INCORRECT, QUIT THE OPMODE AND RESELECT NOW")
        opMode.telemetry.update()
        opMode.telemetry.isAutoClear = autoClearState
        opMode.telemetry.addLine(
            String.format(
                "ButtonHashmap has returned button '%s' to the OpMode.",
                selectedButton.name
            )
        )
        return selectedButton
    }
}