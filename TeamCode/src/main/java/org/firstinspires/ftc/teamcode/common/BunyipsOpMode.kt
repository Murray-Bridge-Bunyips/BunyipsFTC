package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.Telemetry.Item
import kotlin.math.roundToInt

/**
 * Base class for all OpModes that provides a number of useful methods and utilities for development.
 * Includes a lifecycle that is similar to an iterative lifecycle, but includes logging, error catching,
 * and abstraction to provide phased code execution.
 * @author Lucas Bubner, 2023
 */
abstract class BunyipsOpMode : LinearOpMode() {
    private var movingAverageTimer: MovingAverageTimer? = null
    var loopCount: Long = 0
        private set
    private var operationsCompleted = false
    private var operationsPaused = false

    /**
     * One-time setup for operations that need to be done for the opMode
     */
    private fun setup() {
        telemetry.log().displayOrder = Telemetry.Log.DisplayOrder.OLDEST_FIRST
        telemetry.captionValueSeparator = ""
        // Uncap the telemetry log limit to ensure we capture everything
        telemetry.log().capacity = 999
        movingAverageTimer = MovingAverageTimer(100)
    }

    /**
     * Runs upon the pressing of the INIT button on the Driver Station.
     * This is where your hardware should be initialised.
     */
    protected abstract fun onInit()

    /**
     * Run code in a loop AFTER onInit has completed, until
     * start is pressed on the Driver Station or true is returned to this method.
     * If not implemented, the opMode will continue on as normal and wait for start.
     */
    protected open fun onInitLoop(): Boolean {
        return true
    }

    /**
     * Allow code to execute once after all initialisation has finished.
     * Note: this method is always called even if initialisation is cut short by the driver station.
     */
    protected open fun onInitDone() {
    }

    /**
     * Perform one time operations after start is pressed.
     * Unlike onInitDone, this will only execute once play is hit and not when initialisation is done.
     */
    protected open fun onStart() {
    }

    /**
     * Code to run when the START button is pressed on the Driver Station.
     * This method will be called on each hardware cycle.
     */
    protected abstract fun activeLoop()

    /**
     * Perform one time clean-up operations after the OpMode finishes.
     */
    protected open fun onStop() {
    }

    /**
     * Main method overridden from LinearOpMode that handles the opMode lifecycle.
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    final override fun runOpMode() {
        try {
            try {
                telemetry.log().add("")
                log("status changed: from idle to setup")
                // Run BunyipsOpMode setup
                setup()
                log("status changed: from setup to static_init")
                // Run user-defined setup
                onInit()
                log("status changed: from static_init to dynamic_init")
                // Store telemetry objects raised by onInit() by turning off auto-clear
                setTelemetryAutoClear(false)
                // Run user-defined dynamic initialisation
                while (opModeInInit()) {
                    try {
                        // Run until onInitLoop returns true or the opMode is continued
                        if (onInitLoop()) break
                        telemetry.update()
                    } catch (ie: InterruptedException) {
                        // Don't swallow InterruptedExceptions, let the superclass handle them
                        throw ie
                    } catch (e: Throwable) {
                        ErrorUtil.handleCatchAllException(e, ::log)
                    }
                }
                log("status changed: from dynamic_init to finish_init")
                // Run user-defined final initialisation
                onInitDone()
                telemetry.addData("BUNYIPSOPMODE : ", "INIT COMPLETE -- PLAY WHEN READY.")
                telemetry.update()
            } catch (ie: InterruptedException) {
                throw ie
            } catch (e: Throwable) {
                ErrorUtil.handleCatchAllException(e, ::log)
            }
            log("status changed: from finish_init to ready")
            // Ready to go.
            waitForStart()
            setTelemetryAutoClear(true)
            clearTelemetry()
            movingAverageTimer?.reset()
            log("status changed: from ready to running")
            try {
                // Run user-defined start operations
                onStart()
            } catch (ie: InterruptedException) {
                throw ie
            } catch (e: Throwable) {
                ErrorUtil.handleCatchAllException(e, ::log)
            }
            while (opModeIsActive() && !operationsCompleted) {
                if (operationsPaused) {
                    // If the opMode is paused, skip the loop and wait for the next hardware cycle
                    idle()
                    continue
                }
                try {
                    // Run user-defined active loop
                    activeLoop()
                    loopCount++
                } catch (ie: InterruptedException) {
                    throw ie
                } catch (e: Throwable) {
                    // Let the error logger handle any other exceptions
                    ErrorUtil.handleCatchAllException(e, ::log)
                }
                // Update telemetry and timers
                movingAverageTimer?.update()
                telemetry.update()
            }
            log("status changed: from running to finished")
            // Wait for user to hit stop
            while (opModeIsActive()) {
                idle()
            }
        } finally {
            log("status changed: from finished to cleanup")
            onStop()
        }
    }

    /**
     * Add data to the telemetry object
     * @param value A string to add to telemetry
     * @param retained Optional parameter to retain the data on the screen
     * @return The telemetry item added to the Driver Station
     */
    fun addTelemetry(value: String, retained: Boolean = false): Item {
        // Add data to the telemetry object with runtime data
        var prefix = "T+${movingAverageTimer?.elapsedTime()?.div(1000)?.roundToInt() ?: 0.0}s : "
        if (prefix == "T+0s : ") {
            // Don't bother making a prefix if the time is zero
            prefix = ""
        }
        val item = telemetry.addData("", prefix + value)
        item.setRetained(retained)
        return item
    }

    fun addTelemetry(value: String): Item {
        return addTelemetry(value, false)
    }

    /**
     * Log a message to the telemetry log
     * @param message The message to log
     */
    fun log(message: String) {
        telemetry.log().add(message)
    }

    /**
     * Remove an entry from the telemetry object. Useful for removing retained data without clearing.
     * This method should be combined with the return value of addTelemetry.
     * Note this method is not needed if you are using unretained data, as it will be cleared automatically
     * if auto-clear is enabled, which it is by default.
     * @param items The telemetry items to remove
     */
    fun removeTelemetryItems(vararg items: Item) {
        for (item in items) {
            val res = telemetry.removeItem(item)
            if (!res) {
                log("failed to remove telemetry item: $item")
            }
        }
    }

    fun removeTelemetryItems(items: List<Item>) {
        removeTelemetryItems(*items.toTypedArray())
    }

    /**
     * Reset telemetry data, including retention
     */
    fun resetTelemetry() {
        telemetry.clearAll()
    }

    /**
     * Clear telemetry on screen, not including retention
     */
    fun clearTelemetry() {
        telemetry.clear()
    }

    /**
     * Set telemetry auto clear status
     */
    fun setTelemetryAutoClear(autoClear: Boolean) {
        telemetry.isAutoClear = autoClear
    }

    /**
     * Get auto-clear status of telemetry
     */
    fun getTelemetryAutoClear(): Boolean {
        return telemetry.isAutoClear
    }

    /**
     * Call to prevent hardware loop from calling activeLoop(), indicating an OpMode that is finished.
     */
    fun finish() {
        operationsCompleted = true
        telemetry.addData("BUNYIPSOPMODE : ", "activeLoop terminated. All operations completed.")
        telemetry.update()
    }

    /**
     * Call to temporarily halt the activeLoop from running.
     */
    fun halt() {
        operationsPaused = true
        log("status: from running to halted")
        telemetry.addData("BUNYIPSOPMODE : ", "activeLoop halted. Operations paused.")
        telemetry.update()
    }

    /**
     * Call to resume the activeLoop after a halt() call.
     */
    fun resume() {
        operationsPaused = false
        log("status changed: from halted to running")
        telemetry.addData("BUNYIPSOPMODE : ", "activeLoop resumed. Operations resumed.")
        telemetry.update()
    }
}