package org.firstinspires.ftc.teamcode.common

/**
 * Async thread to ask for user input from a controller in order to determine a pre-determined
 * set of instructions before an OpMode starts.
 *
 * You really should only be running one of these threads at a time.
 *
 * Keep in mind this thread runs in the background so it is not guaranteed to be ready during any
 * specific phase of your init-cycle. It is recommended to start this thread at the start of your
 * cycle and checking for `thread.isAlive()` or `thread.result != null` in your `onInitLoop()`.
 * This is not required but it does let BunyipsOpMode know that you are waiting for a result.
 *
 * The result of this thread will be stored in the `result` property, which you can access yourself
 * or you can attach a callback to the `callback` property to be run once the thread is complete.
 * This callback will still be run if the OpMode moves to a running state without a selection. In
 * the event a user does not make a selection, the callback result and `result` property will be
 * null.
 *
 * ```
 *    private val selector: UserSelection<String> = UserSelection(this, { if (it == "POV") initPOVDrive() else initFCDrive() }, "POV", "FIELD-CENTRIC")
 *
 *    override fun onInit() {
 *      selector.start()
 *    }
 * ```
 *
 * Updated to use dynamic button mapping and generics 04/08/23.
 * Updated to be async and removed time restriction 07/09/23.
 *
 * @see While
 * @param opmodes Modes to map to buttons. Will be casted to strings for display and return back in type `T`.
 * @author Lucas Bubner, 2023
 */
class UserSelection<T>(
    private val opMode: BunyipsOpMode,
    var callback: (res: T?) -> Unit,
    private vararg val opmodes: T
) :
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
            callback(null)
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
            stickyObjects.add(opMode.addTelemetry(String.format("%s: %s", button.name, if (name is OpModeSelection) name.name else name), true))
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
        if (result == null) {
            opMode.addTelemetry("No selection made. Result was handled by the OpMode.", true)
        } else {
            opMode.addTelemetry(
                "'${selectedButton?.name}' registered. Running OpMode: '${if (selectedOpMode is OpModeSelection) selectedOpMode.name else selectedOpMode.toString()}'",
                true
            )
        }

        // Clean up telemetry and reset auto clear
        for (id in stickyObjects) {
            opMode.removeTelemetryIndex(id)
        }
        opMode.telemetry.update()
        opMode.setTelemetryAutoClear(autoClearState)

        callback(selectedOpMode)
    }
}