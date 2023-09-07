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
    private val stickyTelemetryObjects = mutableListOf<Pair<Int, Item>>()

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
     */
    protected abstract fun onInit()

    /**
     * Run code to in a loop AFTER onInit has completed, until
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
    override fun runOpMode() {
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
                    } catch (e: Throwable) {
                        ErrorUtil.handleCatchAllException(e, ::log)
                    }
                }
                log("status changed: from dynamic_init to finish_init")
                // Run user-defined final initialisation
                onInitDone()
                telemetry.addData("BUNYIPSOPMODE : ", "INIT COMPLETE -- PLAY WHEN READY.")
                telemetry.update()
            } catch (e: Throwable) {
                ErrorUtil.handleCatchAllException(e, ::log)
            }
            log("status changed: from finish_init to ready")
            // Ready to go.
            waitForStart()
            setTelemetryAutoClear(true)
            clearTelemetryData()
            movingAverageTimer?.reset()
            log("status changed: from ready to running")
            try {
                // Run user-defined start operations
                onStart()
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
                    // Preemptively exit the OpMode if an interrupt is thrown
                    throw ie
                } catch (e: Throwable) {
                    // Otherwise, let the handler manage them
                    ErrorUtil.handleCatchAllException(e, ::log)
                }
                // Update telemetry and timers
                movingAverageTimer?.update()
                telemetry.update()
                idle()
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
     * Clear data from the telemetry cache.
     */
    private fun clearTelemetryData() {
        if (telemetry.isAutoClear) {
            telemetry.clear()
        } else {
            telemetry.clearAll()
        }
        if (opModeIsActive()) {
            idle()
        }
    }

    /**
     * Add data to the telemetry object
     * @param value A string to add to telemetry
     * @param retained Optional parameter to retain the data on the screen
     * @return Index of the telemetry object printed to the screen if retained, otherwise -1
     */
    fun addTelemetry(value: String, retained: Boolean = false): Int {
        // Add data to the telemetry object with runtime data
        var prefix = "T+${movingAverageTimer?.elapsedTime()?.div(1000)?.roundToInt() ?: 0.0}s : "
        if (prefix == "T+0s : ") {
            // Don't bother making a prefix if the time is zero
            prefix = ""
        }
        val item = telemetry.addData("", prefix + value)
        var idx = -1
        if (retained) {
            // Set retained to true if the data is sticky
            item.setRetained(true)
            idx = stickyTelemetryObjects.size + 1
            stickyTelemetryObjects.add(Pair(idx, item))
        }
        return idx
    }

    fun addTelemetry(value: String) {
        addTelemetry(value, false)
    }

    /**
     * Log a message to the telemetry log
     */
    fun log(message: String) {
        telemetry.log().add(message)
    }

    /**
     * Remove an entry from the telemetry object if it is sticky
     */
    fun removeTelemetryIndex(index: Int) {
        if (index > 0 && index <= stickyTelemetryObjects.size) {
            telemetry.removeItem(stickyTelemetryObjects[index - 1].second)
            stickyTelemetryObjects.removeAt(index - 1)
        }
        // Update indexes of sticky telemetry objects
        for (i in stickyTelemetryObjects.indices) {
            stickyTelemetryObjects[i] = Pair(i + 1, stickyTelemetryObjects[i].second)
        }
    }

    /**
     * Reset telemetry data, including retention
     */
    fun resetTelemetry() {
        telemetry.clearAll()
        stickyTelemetryObjects.clear()
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
        clearTelemetryData()
        log("status changed: from running to finished")
        telemetry.addData("BUNYIPSOPMODE : ", "activeLoop terminated. All operations completed.")
        telemetry.update()
    }

    /**
     * Call to temporarily halt the activeLoop from running.
     */
    fun halt() {
        operationsPaused = true
        clearTelemetryData()
        log("status: from running to halted")
        telemetry.addData("BUNYIPSOPMODE : ", "activeLoop halted. Operations paused.")
        telemetry.update()
    }

    /**
     * Call to resume the activeLoop after a halt() call.
     */
    fun resume() {
        operationsPaused = false
        clearTelemetryData()
        log("status changed: from halted to running")
        telemetry.addData("BUNYIPSOPMODE : ", "activeLoop resumed. Operations resumed.")
        telemetry.update()
    }
}