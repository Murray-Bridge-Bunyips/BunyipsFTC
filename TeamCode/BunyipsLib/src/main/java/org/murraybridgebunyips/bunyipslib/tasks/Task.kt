package org.murraybridgebunyips.bunyipslib.tasks

/**
 * A task, or command is an action that can be performed by a robot. This has been designed
 * to reflect closely the command-based programming style used in FRC, while still being
 * reflective of the past nature of how the Task system was implemented in BunyipsLib.
 * @author Lucas Bubner, 2022-2024
 */
abstract class Task : Command {
    protected var time: Double
    private var startTime = 0.0

    @Volatile
    var taskFinished = false

    private var finisherFired = false

    /**
     * @param time Maximum timeout (sec) of the task. If set to zero this will serve as an indefinite task.
     */
    constructor(time: Double) {
        this.time = time
    }

    /**
     * Overloading base Task allows any tasks that may not want to use a time restriction,
     * such as an init-loop task. This will perform the same as a task also defined with a time of 0 seconds.
     */
    constructor() {
        time = 0.0
    }

    /**
     * Define code to run once, when the task is started.
     */
    abstract override fun init()

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
     * Return a boolean to this method to add custom criteria if a task should be considered finished.
     * @return bool expression indicating whether the task is finished or not, timeout and OpMode state are handled automatically.
     */
    abstract fun isTaskFinished(): Boolean

    final override fun isFinished(): Boolean {
        if (startTime == 0.0) {
            init()
            startTime = currentTime
        }
        if (taskFinished || isTaskFinished()) {
            if (!finisherFired)
                onFinish()
            finisherFired = true
            taskFinished = true
            return true
        }
        // Finish tasks that exceed a time limit defined by the task
        if (time != 0.0 && currentTime > startTime + time) {
            if (!finisherFired)
                onFinish()
            finisherFired = true
            taskFinished = true
        }
        return taskFinished && finisherFired
    }

    /**
     * Reset a task to an uninitialised and unfinished state.
     */
    fun reset() {
        startTime = 0.0
        taskFinished = false
        finisherFired = false
    }

    /**
     * @return Whether the task is currently running
     */
    fun isRunning(): Boolean {
        return startTime != 0.0 && !taskFinished
    }

    protected val currentTime: Double
        get() = System.nanoTime() / NANOS_IN_SECONDS

    protected val deltaTime: Double
        get() = currentTime - startTime

    companion object {
        const val NANOS_IN_SECONDS = 1000000000.0
    }
}