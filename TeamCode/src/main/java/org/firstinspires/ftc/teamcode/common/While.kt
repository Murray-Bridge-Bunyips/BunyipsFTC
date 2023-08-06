package org.firstinspires.ftc.teamcode.common

import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Perform non-blocking loops using an evaluator to run the loop.
 * This should be paired with an update() or activeLoop().
 * @param condition The condition or function to evaluate as an exit. Return false to exit the loop.
 * @param run The function to run on each loop iteration.
 * @param callback The callback to run once [condition] is met.
 * @param timeoutSeconds Optional timeout in seconds. If the timeout is reached, the loop will exit.
 *
 * @author Lucas Bubner, 2023
 */
class While(val condition: () -> Boolean, val run: () -> Unit = {}, val callback: () -> Unit = {}, val timeoutSeconds: Double = 0.0) {
    private var timer: ElapsedTime? = null
    private var evalLatch = false

    /**
     * Notify the loop that it should run.
     */
    fun engage() {
        evalLatch = true
    }

    /**
     * Notify the loop that it should stop now.
     */
    fun terminate() {
        evalLatch = false
    }

    /**
     * Run the evaluator loop if required.
     * @return True if the loop was run, false if it was not.
     */
    fun run(): Boolean {
        if (!evalLatch) {
            return false
        }
        if (timer == null) {
            timer = ElapsedTime()
        }
        if (condition() && timer!!.seconds() < timeoutSeconds) {
            run()
            return true
        }
        evalLatch = false
        timer = null
        callback()
        return false
    }

    /**
     * Get the last status of the evaluator without running it.
     */
    fun running(): Boolean {
        return evalLatch
    }
}