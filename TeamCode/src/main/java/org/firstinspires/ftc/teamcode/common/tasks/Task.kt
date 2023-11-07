package org.firstinspires.ftc.teamcode.common.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode

/**
 * Abstract implementation class to use custom ArrayDeque tasks in an
 * activeLoop, simulating multiple different looping operations in a linear sense.
 */
abstract class Task : AutoTask {
    protected var time: Double
    private var startTime = 0.0

    protected var opMode: BunyipsOpMode

    @Volatile
    var taskFinished = false

    private var finisherFired = false

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
     * Somewhere in your polling loop you must call isFinished() to determine when the task is finished.
     * (AutonomousBunyipsOpMode will handle this automatically and checking isFinished() is not required)
     * @see onFinish()
     */
    abstract override fun run()

    /**
     * Finalising function to run once the task is finished.
     */
    abstract override fun onFinish()

    /**
     * Override to this method to add custom criteria if a task should be considered finished.
     * You **must** call `super.isFinished()` if you override this method, otherwise you need to handle
     * safety timeout, `init()` and `onFinish()` yourself.
     * @return bool expression indicating whether the task is finished or not
     */
    override fun isFinished(): Boolean {
        if (taskFinished) {
            if (!finisherFired)
                onFinish()
            finisherFired = true
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
            if (!finisherFired)
                onFinish()
            finisherFired = true
            taskFinished = true
        }
        return taskFinished
    }

    protected val currentTime: Double
        get() = System.nanoTime() / NANOS_IN_SECONDS

    protected val deltaTime: Double
        get() = currentTime - startTime

    companion object {
        const val NANOS_IN_SECONDS = 1000000000.0
    }
}