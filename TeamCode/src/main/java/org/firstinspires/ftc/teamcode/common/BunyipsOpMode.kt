package org.firstinspires.ftc.teamcode.common

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.Telemetry.Item
import org.firstinspires.ftc.teamcode.common.Text.formatString
import kotlin.math.roundToInt

/**
 * Base class for all OpModes that provides a number of useful methods and utilities for development.
 * Includes a lifecycle that is similar to an iterative lifecycle, but includes logging, error catching,
 * and abstraction to provide phased code execution.
 * @noinspection
 * @author Lucas Bubner, 2023
 */
abstract class BunyipsOpMode : LinearOpMode() {
    var movingAverageTimer: MovingAverageTimer? = null
        private set

    private var operationsCompleted = false
    private var operationsPaused = false

    /**
     * Implement telemetry to be a MultipleTelemetry object that sends data to both the Driver Station and the Dashboard.
     * FtcDashboard cannot be utilised during a match according to <RS09>, so this is only for development.
     * It is allowed during the Pits or Practice Field, or in any other non-match situation.
     */
    val telem: MultipleTelemetry =
        MultipleTelemetry(super.telemetry, FtcDashboard.getInstance().telemetry)

    /**
     * One-time setup for operations that need to be done for every OpMode
     * This method is not exception protected!
     */
    private fun setup() {
        telem.log().displayOrder = Telemetry.Log.DisplayOrder.OLDEST_FIRST
        telem.captionValueSeparator = ""
        // Uncap the telemetry log limit to ensure we capture everything
        telem.log().capacity = 999999
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
     * This method is not exception protected!
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
                Dbg.log("===== BUNYIPSOPMODE =====")
                telem.log().add("")
                log("status changed: from idle to setup")
                Dbg.log("BunyipsOpMode: setting up...")
                // Run BunyipsOpMode setup
                setup()
                log("status changed: from setup to static_init")
                Dbg.log("BunyipsOpMode: firing onInit()...")
                // Store telemetry objects raised by onInit() by turning off auto-clear
                setTelemetryAutoClear(false)
                addTelemetry("===== BUNYIPSOPMODE =====")
                // Run user-defined setup
                try {
                    onInit()
                } catch (ie: InterruptedException) {
                    throw ie
                } catch (e: Throwable) {
                    ErrorUtil.handleCatchAllException(e, ::log)
                }
                if (!gamepad1.atRest() || !gamepad2.atRest()) {
                    log("warning: a gamepad was not zeroed during init. please ensure controllers zero out correctly.")
                }
                telem.update()
                log("status changed: from static_init to dynamic_init")
                Dbg.log("BunyipsOpMode: starting onInitLoop()...")
                // Run user-defined dynamic initialisation
                while (opModeInInit()) {
                    try {
                        // Run until onInitLoop returns true or the opMode is continued
                        if (onInitLoop()) break
                        telem.update()
                    } catch (ie: InterruptedException) {
                        // Don't swallow InterruptedExceptions, let the superclass handle them
                        throw ie
                    } catch (e: Throwable) {
                        ErrorUtil.handleCatchAllException(e, ::log)
                    }
                }
                log("status changed: from dynamic_init to finish_init")
                Dbg.log("BunyipsOpMode: firing onInitDone()...")
                // Run user-defined final initialisation
                onInitDone()
                telem.addData("BUNYIPSOPMODE : ", "INIT COMPLETE -- PLAY WHEN READY.")
                telem.update()
            } catch (ie: InterruptedException) {
                throw ie
            } catch (e: Throwable) {
                ErrorUtil.handleCatchAllException(e, ::log)
            }
            log("status changed: from finish_init to ready")
            Dbg.log("BunyipsOpMode: ready.")
            // Ready to go.
            waitForStart()
            setTelemetryAutoClear(true)
            clearTelemetry()
            movingAverageTimer?.reset()
            log("status changed: from ready to running")
            Dbg.log("BunyipsOpMode: starting activeLoop()...")
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
                    // Update telemetry and timers
                    movingAverageTimer?.update()
                    telem.update()
                } catch (ie: InterruptedException) {
                    throw ie
                } catch (e: Throwable) {
                    // Let the error logger handle any other exceptions
                    ErrorUtil.handleCatchAllException(e, ::log)
                }
            }
            log("status changed: from running to finished")
            Dbg.log("BunyipsOpMode: finished.")
            // Wait for user to hit stop
            while (opModeIsActive()) {
                idle()
            }
        } catch (t: Throwable) {
            Dbg.error("BunyipsOpMode: unhandled throwable! <${t.message}>")
            Dbg.sendStacktrace(t)
        } finally {
            Dbg.log("BunyipsOpMode: cleaning up...")
            log("status changed: from finished to cleanup")
            onStop()
            Dbg.log("BunyipsOpMode: exiting...")
        }
    }

    private fun makeTelemetryObject(value: String): Item {
        // Add data to the telemetry object with runtime data
        var prefix = "T+${movingAverageTimer?.elapsedTime()?.div(1000)?.roundToInt() ?: 0.0}s : "
        if (prefix == "T+0s : ") {
            // Don't bother making a prefix if the time is zero
            prefix = ""
        }
        return telem.addData("", prefix + value)
    }

    /**
     * Add data to the telemetry object
     * @param value A string to add to telemetry
     * @return The telemetry item added to the Driver Station
     */
    fun addTelemetry(value: String): Item {
        return makeTelemetryObject(value)
    }

    /**
     * Add data to the telemetry object using a custom format string
     * @param fstring A format string to add to telemetry
     * @param objs The objects to format into the string
     * @return The telemetry item added to the Driver Station
     */
    fun addTelemetry(fstring: String, vararg objs: Any): Item {
        if (objs.isEmpty()) {
            return addTelemetry(fstring)
        }
        return addTelemetry(formatString(fstring, objs.asList()))
    }

    /**
     * Add retained non-auto-clearing data to the telemetry object
     * @param value A string to add to telemetry
     * @return The telemetry item added to the Driver Station
     */
    fun addRetainedTelemetry(value: String): Item {
        return makeTelemetryObject(value).setRetained(true)
    }

    /**
     * Add a data to the telemetry object using a custom format string
     * @param fstring A format string to add to telemetry
     * @param objs The objects to format into the string
     * @return The telemetry item added to the Driver Station
     */
    fun addRetainedTelemetry(fstring: String, vararg objs: Any): Item {
        if (objs.isEmpty()) {
            return addRetainedTelemetry(fstring)
        }
        return addRetainedTelemetry(formatString(fstring, objs.asList()))
    }

    /**
     * Log a message to the telemetry log
     * @param message The message to log
     */
    fun log(message: String) {
        telem.log().add(message)
    }

    /**
     * Log a message to the telemetry log using a format string
     * @param fstring A format string to add to telemetry
     * @param objs The objects to format into the string
     */
    fun log(fstring: String, vararg objs: Any) {
        if (objs.isEmpty()) {
            return log(fstring)
        }
        return log(formatString(fstring, objs.asList()))
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
            val res = telem.removeItem(item)
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
        telem.clearAll()
    }

    /**
     * Clear telemetry on screen, not including retention
     */
    fun clearTelemetry() {
        telem.clear()
    }

    /**
     * Set telemetry auto clear status
     */
    fun setTelemetryAutoClear(autoClear: Boolean) {
        telem.isAutoClear = autoClear
    }

    /**
     * Get auto-clear status of telemetry
     */
    fun getTelemetryAutoClear(): Boolean {
        return telem.isAutoClear
    }

    fun getSDKTelemetry(): Telemetry {
        return super.telemetry
    }

    fun getDashboardTelemetry(): Telemetry {
        return FtcDashboard.getInstance().telemetry
    }

    fun getTelemetry(): MultipleTelemetry {
        return telem
    }

    /**
     * Call to prevent hardware loop from calling activeLoop(), indicating an OpMode that is finished.
     */
    fun finish() {
        if (operationsCompleted) {
            return
        }
        operationsCompleted = true
        Dbg.log("BunyipsOpMode: activeLoop() terminated by finish().")
        telem.addData("BUNYIPSOPMODE : ", "activeLoop terminated. All operations completed.")
        telem.update()
    }

    /**
     * Call to temporarily halt all activeLoop-related updates from running.
     * Note this will pause all MovingAverageTimer and telemetry updates. These events
     * must be handled manually if needed, which include any conditional calls to resume().
     */
    fun halt() {
        if (operationsPaused) {
            return
        }
        operationsPaused = true
        log("status: from running to halted")
        Dbg.log("BunyipsOpMode: activeLoop() halted.")
        telem.addData("BUNYIPSOPMODE : ", "activeLoop halted. Operations paused.")
        telem.update()
    }

    /**
     * Call to resume the activeLoop after a halt() call.
     */
    fun resume() {
        if (!operationsPaused) {
            return
        }
        operationsPaused = false
        log("status changed: from halted to running")
        Dbg.log("BunyipsOpMode: activeLoop() resumed.")
        telem.addData("BUNYIPSOPMODE : ", "activeLoop resumed. Operations resumed.")
        telem.update()
    }
}