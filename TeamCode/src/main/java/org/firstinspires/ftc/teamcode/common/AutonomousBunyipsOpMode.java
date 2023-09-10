package org.firstinspires.ftc.teamcode.common;

import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;

import java.util.ArrayDeque;

/**
 * OpMode abstraction for Autonomous operation using the BunyipsOpMode ecosystem.
 * @author Lucas Bubner, 2023
 * @author Lachlan Paul, 2023
 */

abstract public class AutonomousBunyipsOpMode extends BunyipsOpMode {

    private final ArrayDeque<AutoTask> tasks = new ArrayDeque<>();

    /**
     * Can be called to add custom tasks in a robot's autonomous
     * @param newTask task to add to the run queue
     */
    public void addTask(AutoTask newTask) {
        tasks.add(newTask);
    }

    @Override
    protected void activeLoop(){
        AutoTask currentTask = tasks.peekFirst();
        if (currentTask == null) {
            finish();
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()) {
            tasks.removeFirst();
        }
    }

}