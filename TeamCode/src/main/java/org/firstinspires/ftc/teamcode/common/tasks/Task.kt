package org.firstinspires.ftc.teamcode.common.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Abstract implementation class to use custom ArrayDeque tasks in an
 * activeLoop, simulating multiple different looping operations in a linear sense.
 */
abstract class Task : TaskImpl {
    protected var time: Double

    @Volatile
    var taskFinished = false
    protected var startTime = 0.0
    protected var opMode: BunyipsOpMode

    constructor(opMode: BunyipsOpMode, time: Double) {
        this.time = time
        this.opMode = opMode
    }

    // Overloading base Task to allow any tasks that may not want to use a time restriction,
    // such as an init-loop task or some other always-active task. This will perform the same
    // as a task also defined with a time of 0 seconds.
    constructor(opMode: BunyipsOpMode) {
        this.opMode = opMode
        time = 0.0
    }

    /**
     * Define code to run once, upon when the task is started.
     */
    override fun init() {
        // Define global init code here, use with a super call if overriding
    }

    /**
     * Override to this method to add custom criteria if a task should be considered finished.
     * Recommended to override with a super call even if not using a time restriction.
     * @return bool expression indicating whether the task is finished or not
     */
    override fun isFinished(): Boolean {
        if (taskFinished) {
            return taskFinished
        }
        if (startTime == 0.0) {
            init()
            startTime = currentTime
        }
        if (currentTime > startTime + time && time != 0.0 || opMode.isStopRequested) {
            taskFinished = true
        }
        return taskFinished
    }

    private val currentTime: Double
        get() = System.nanoTime() / NANOS_IN_SECONDS

    companion object {
        const val NANOS_IN_SECONDS = 1000000000.0
    }
}