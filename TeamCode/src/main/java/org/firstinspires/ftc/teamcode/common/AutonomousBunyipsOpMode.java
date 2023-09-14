package org.firstinspires.ftc.teamcode.common;

import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;

/**
 * OpMode abstraction for Autonomous operation using the BunyipsOpMode ecosystem.
 *
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */

abstract public class AutonomousBunyipsOpMode extends BunyipsOpMode {

    private final ArrayDeque<AutoTask> tasks = new ArrayDeque<>();
    private final ArrayDeque<AutoTask> queuedTasks = new ArrayDeque<>();
    private int currentTaskNumber = 1;
    private int taskLength = 0;

    /**
     * This list defines OpModes that should be selectable by the user. This will then
     * be used to determine your tasks in {@link #onQueueReady(OpModeSelection)}.
     * For example, you may have configurations for RED_LEFT, RED_RIGHT, BLUE_LEFT, BLUE_RIGHT.
     * By default, this will be empty, and the user will not be prompted for a selection.
     *
     * @see #setOpModes()
     */
    protected ArrayList<OpModeSelection> opModes = new ArrayList<>();
    private AutoTask initTask = null;
    private boolean hasGottenCallback = false;

    private Unit callback(OpModeSelection selectedOpMode) {
        hasGottenCallback = true;
        // Interface Unit to be void
        onQueueReady(selectedOpMode);
        // Add any queued tasks
        tasks.addAll(queuedTasks);
        return Unit.INSTANCE;
    }

    @Override
    protected final void onInit() {
        // Run user-defined hardware initialisation
        onInitialisation();
        // Set user-defined initTask
        initTask = setInitTask();
        List<OpModeSelection> userSelections = setOpModes();
        if (userSelections != null) {
            // User defined OpModeSelections
            opModes.addAll(userSelections);
        }
        // Convert user defined OpModeSelections to varargs
        OpModeSelection[] varargs = this.opModes.toArray(new OpModeSelection[0]);
        if (varargs.length == 0) {
            opModes.add(new OpModeSelection("DEFAULT"));
        }
        if (varargs.length > 1) {
            // Run task allocation if OpModeSelections are defined
            // This will run asynchronously, and the callback will be called
            // when the user has selected an OpMode
            new UserSelection<>(this, this::callback, varargs).start();
        } else {
            // There are no OpMode selections, so just run the callback with the default OpMode
            callback(opModes.get(0));
        }
    }

    @Override
    protected final void activeLoop() {
        // Run any code defined by the user
        onActiveLoop();
        if (!hasGottenCallback) {
            // Not ready to run tasks yet
            return;
        }
        // Run the queue of tasks
        AutoTask currentTask = tasks.peekFirst();

        if (currentTask == null) {
            finish();
            return;
        }

        // Why does it have to be like this
        addTelemetry(String.format(Locale.getDefault(), "Current Task(%d/%d): %s", currentTaskNumber, taskLength, currentTask.getClass().getSimpleName())); // His ass is NOT within line length conventions!

        currentTask.run();
        if (currentTask.isFinished()) {
            tasks.removeFirst();
            log(String.format(Locale.getDefault(), "Task No. %d(%s) has finished", currentTaskNumber, currentTask.getClass().getSimpleName()));
            currentTaskNumber++;
        }
    }

    /**
     * Run code in a loop AFTER onBegin() has completed, until
     * start is pressed on the Driver Station or the {@link #setInitTask initTask} is done.
     * If not implemented, the opMode will try to run your initTask, and if that is null,
     * the dynamic_init phase will be skipped.
     * You may also override this method if you want to do something else in the dynamic_init phase.
     *
     * @see #setInitTask
     */
    @Override
    protected boolean onInitLoop() {
        if (initTask != null) {
            initTask.run();
            return initTask.isFinished();
        }
        return true;
    }

    /**
     * Can be called to add custom tasks in a robot's autonomous
     *
     * @param newTask task to add to the run queue
     */
    public void addTask(AutoTask newTask) {
        tasks.add(newTask);

        // Used to count task list size, specifically for activeLoop
        taskLength++;
        log(String.format(Locale.getDefault(), "Task %s has been added as Task No. %d", newTask, taskLength));
    }

    /**
     * Add a task to the run queue, but after onReady() has processed tasks. This is useful to call
     * when working with tasks that should be queued at the very end of the autonomous, while still
     * being able to add tasks asynchronously with user input in onReady().
     */
    public void addTaskLast(AutoTask newTask) {
        if (!hasGottenCallback) {
            queuedTasks.addLast(newTask);
            return;
        }
        tasks.addLast(newTask);
        taskLength++;
        log(String.format(Locale.getDefault(), "%s has been added to the end of the queue", newTask));
    }


    /**
     * Add a task to the very start of the queue. This is useful to call when working with tasks that
     * should be queued at the very start of the autonomous, while still being able to add tasks
     * asynchronously with user input in onReady().
     */
    public void addTaskFirst(AutoTask newTask) {
        tasks.addFirst(newTask);
        taskLength++;
        log("A new task was added to the front of the queue");
    }

    /**
     * Removes whatever task is at the given array index
     *
     * @param taskIndex the array index to be removed (did I really need to tell you?)
     */
    public void removeTaskIndex(Integer taskIndex) {
        Iterator iterator = tasks.iterator();
        int counter = 0;

        if (taskIndex < 0) {
            throw new IllegalArgumentException("Cannot remove items starting from last index, this isn't Python");
        }

        if (taskIndex > tasks.size()) {
            throw new IllegalArgumentException("Given index exceeds array size");
        }

        /*
        In the words of the great Lucas Bubner:
            You've made an iterator for all those tasks
            which is the goofinator car that can drive around your array
            calling .next() on your car will move it one down the array
            then if you call .remove() on your car it will remove the element wherever it is
         */

        while (iterator.hasNext()) {
            if (counter == taskIndex) {
                iterator.remove();
                break;
            }

            iterator.next();
            counter++;

            taskLength--;
            log(String.format(Locale.getDefault(), "A task has been removed from index %d", taskIndex));

        }
    }

    /**
     * Removes the last task in the task queue
     */
    public void removeTaskLast() {
        tasks.removeLast();
        taskLength--;
        log("The task at the end of the queue was removed");
    }

    /**
     * Removes the first task in the task queue
     */
    public void removeTaskFirst() {
        tasks.removeFirst();
        taskLength--;
        log("The task at the start of the queue was removed");
    }


    /**
     * Runs upon the pressing of the INIT button on the Driver Station.
     * This is where your hardware should be initialised. You may also add specific tasks to the queue
     * here, but it is recommended to use {@link #setInitTask()} or {@link #onQueueReady(OpModeSelection)} instead.
     *
     * @see #onInit()
     */
    protected abstract void onInitialisation();

    /**
     * Implement to define your OpModeSelections, if you list any, then the user will be prompted to select
     * an OpMode before the OpMode begins. If you do not define any OpModeSelections, then the user will not
     * be prompted for a selection, and the OpMode will move to task-ready state immediately.
     * <pre><code>
     *     protected List<OpModeSelection> setOpModes() {
     *         return List.of(
     *                 new OpModeSelection("LEFT_BLUE"),
     *                 new OpModeSelection("RIGHT_BLUE"),
     *                 new OpModeSelection("LEFT_RED"),
     *                 new OpModeSelection("RIGHT_RED")
     *         );
     *     }
     * </code></pre>
     */
    protected abstract List<OpModeSelection> setOpModes();

    /**
     * Return a task that will run as an init-task. This will run
     * after your onInitialisation() has completed, allowing you to initialise hardware first.
     * <p>
     * You should store any running variables inside the task itself, and keep the instance of the task
     * defined as a field in your OpMode. You can then use this value in your onInitDone() to do
     * what you need to after the init-task has finished.
     * </p>
     * Note that if you do not define an initTask, then the dynamic_init phase will be skipped.
     * This method should be paired with {@link #onInitDone()} to do anything after the initTask has finished.
     *
     * @see #onInitDone()
     * @see #addTaskFirst(AutoTask)
     * @see #addTaskLast(AutoTask)
     */
    protected abstract AutoTask setInitTask();

    /**
     * Called when the OpMode is ready to process tasks.
     * This will happen when the user has selected an OpMode, or if no OpModeSelections were defined,
     * in which case it will run immediately after onInitialisation() has completed.
     * This is where you should add your tasks to the run queue.
     *
     * @param selectedOpMode the OpMode selected by the user, if applicable. Will be "DEFAULT" if no OpModeSelections were defined, or
     *                       NULL if the user did not select an OpMode.
     * @see #addTask(AutoTask)
     */
    protected abstract void onQueueReady(OpModeSelection selectedOpMode);

    /**
     * Override to this method to add extra code to the activeLoop.
     */
    protected void onActiveLoop() {
    }
}