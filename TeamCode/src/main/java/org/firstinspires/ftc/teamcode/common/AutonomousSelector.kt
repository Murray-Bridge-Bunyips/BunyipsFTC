package org.firstinspires.ftc.teamcode.common

/**
 * Async thread to ask for user input from a controller in order to determine a pre-determined
 * set of instructions before Autonomous.
 *
 * You really should only be running one of these threads at a time.
 *
 * Keep in mind this thread runs in the background so it is not guaranteed to be ready during any
 * specific phase of your init-cycle. It is recommended to start this thread at the start of your
 * cycle and non-null checking throughout your `onInitLoop()` with a final evaluation at `onStart()`,
 * as the user will need to have selected an option by then. In the event a user has not selected
 * an option, your thread.result will be `null`.
 *
 *
 * ```
 *     val thread = AutonomousSelector(this, "Option 1", "Option 2")
 *
 *     override fun onInit() {
 *         thread.start()
 *         // Your other init code
 *     }
 *
 *     override fun onInitLoop(): Boolean {
 *         if (thread.result != null) {
 *             // Do something with thread.result, knowing that the user has selected an option
 *             return true
 *         }
 *         return false
 *     }
 *
 *     override fun onStart() {
 *         if (thread.result == null) {
 *             // Use a default action as the user has not selected an option
 *         }
 *
 *         // You can also instead use your thread.result result here without busy-waiting, but
 *         // it will only be run on OpMode start and should not be initialising heavy components such
 *         // as a drive system or camera. It is ideal for tasks.
 *     }
 * ```
 *
 * Updated to use dynamic button mapping and generics 04/08/23.
 * Updated to be async and removed time restriction 07/09/23.
 *
 * @see While
 * @param opmodes Modes to map to buttons. Will be casted to strings for display and return back in type `T`.
 * @author Lucas Bubner, 2023
 */
class AutonomousSelector<T>(private val opMode: BunyipsOpMode, private vararg val opmodes: T) :
    Thread(), Runnable {

    @Volatile
    var result: T? = null
        private set

    /**
     * Maps a set of operation modes to a set of buttons.
     * @param opmodes The operation modes to map to buttons.
     * @return A HashMap of operation modes to buttons.
     */
    override fun run() {
        if (opmodes.isEmpty()) {
            throw IllegalArgumentException("Must provide at least one operation mode.")
        }

        val buttons: HashMap<T, ButtonControl> = ButtonControl.mapArgs(opmodes)

        // Default options for button selection and operation mode
        var selectedButton: ButtonControl? = null
        var selectedOpMode: T? = null

        // Disable auto clear if it is enabled, we might accidentally clear out static telemetry
        val autoClearState = opMode.getTelemetryAutoClear()
        opMode.setTelemetryAutoClear(false)

        val stickyObjects = mutableListOf<Int>()
        stickyObjects.add(opMode.addTelemetry("---------!!!--------", true))
        stickyObjects.add(
            opMode.addTelemetry(
                "ACTION REQUIRED: INIT YOUR OPMODE USING GAMEPAD1",
                true
            )
        )
        for ((name, button) in buttons) {
            stickyObjects.add(opMode.addTelemetry(String.format("%s: %s", button.name, name), true))
        }
        stickyObjects.add(opMode.addTelemetry("---------!!!--------", true))

        // Must manually call telemetry push as the BYO may not be handling them
        // This will not clear out any other telemetry as auto clear is disabled
        opMode.telemetry.update()

        while (selectedOpMode == null && opMode.opModeInInit()) {
            for ((str, button) in buttons) {
                if (ButtonControl.isSelected(opMode.gamepad1, button)) {
                    selectedButton = button
                    selectedOpMode = str
                    break
                }
            }
            sleep(10)
            opMode.idle()
        }

        result = selectedOpMode
        opMode.addTelemetry("'${selectedButton?.name}' registered. Running OpMode: '$selectedOpMode'", true)

        // Clean up telemetry and reset auto clear
        for (id in stickyObjects) {
            opMode.removeTelemetryIndex(id)
        }
        opMode.telemetry.update()
        opMode.setTelemetryAutoClear(autoClearState)
    }
}