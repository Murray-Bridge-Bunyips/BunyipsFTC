package org.firstinspires.ftc.teamcode.common.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Abstract implementation class to use custom ArrayDeque tasks in an
 * activeLoop, simulating multiple different looping operations in a linear sense.
 */
abstract class Task : AutoTask {
    protected var time: Double

    @Volatile
    var taskFinished = false
    protected var startTime = 0.0
    protected var opMode: BunyipsOpMode

    /**
     * @param time Maximum timeout (sec) of the task. If set to zero this will serve as an indefinite init-task.
     */
    constructor(opMode: BunyipsOpMode, time: Double) {
        this.time = time
        this.opMode = opMode
    }

    /**
     * Overloading base Task allows any tasks that may not want to use a time restriction,
     * such as an init-loop task. This will perform the same as a task also defined with a time of 0 seconds.
     * Note that tasks with no time restriction can only be used in the INIT phase.
     *
     * It is not recommended to use this constructor for tasks that will be used in an activeLoop.
     * If this is desired, you may be better off using BunyipsOpMode standalone, as tasks
     * are blocking and will not allow other tasks to run until they are finished.
     */
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
     * To run as an activeLoop during this task's duration.
     * You must call a check to `isFinished()` at some point in this method to prevent an infinite loop.
     */
    abstract override fun run()

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
        // Finish tasks that exceed a time limit, if the OpMode is stopped, or if the task is
        // set to run indefinitely and the OpMode is not in init-phase.
        // In order to prevent an infinite running task we prohibit indefinite tasks outside of init
        if ((currentTime > startTime + time && time != 0.0) || opMode.isStopRequested || (time == 0.0 && !opMode.opModeInInit())) {
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