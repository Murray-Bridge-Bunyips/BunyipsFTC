package org.firstinspires.ftc.teamcode.example.examplerobot.autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.example.examplerobot.components.ExampleConfig;
import org.firstinspires.ftc.teamcode.example.examplerobot.components.ExampleDrive;
import org.firstinspires.ftc.teamcode.example.examplerobot.tasks.ExampleTimeDriveTask;

import java.util.ArrayDeque;

/**
 * Example autonomous using Task system.
 */
public class ExampleAuto extends BunyipsOpMode {
    // See TeleOp before autonomous.
    // This is an example autonomous that will drive forward for 5 seconds, then stop.

    // Declare drive and config components
    private ExampleDrive drive;
    private ExampleConfig config = new ExampleConfig();

    // !! The major difference between autonomous and teleop is that autonomous uses tasks to run
    // code, whereas teleop uses activeLoop() to run code. View ExampleTimeDriveTask.java for more
    // information on tasks. Here we will make a queue, which will store tasks to run.
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        // Initialise config and components
        config = (ExampleConfig) RobotConfig.newConfig(this, config, hardwareMap);
        drive = new ExampleDrive(this, config.leftMotor, config.rightMotor);

        // Add tasks to the queue
        // MessageTask is a common task that will print a message to the Driver Station
        tasks.add(new MessageTask(this, 2.0, "This is a test!"));
        // ExampleTimeDriveTask is a custom task that we have written, where we pass the
        // drive component to.
        tasks.add(new ExampleTimeDriveTask(this, 5.0, drive));
    }

    @Override
    protected void activeLoop() {
        // This code is required in every autonomous OpMode, as it will run the tasks in the queue.
        // This will run a task until it is done, then move to the next one.
        Task currentTask = tasks.peekFirst();
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
