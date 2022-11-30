package org.firstinspires.ftc.teamcode.jerry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.jerry.config.JerryArm;
import org.firstinspires.ftc.teamcode.jerry.config.JerryConfig;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;
import org.firstinspires.ftc.teamcode.common.tasks.GetQRSleeveTask;

import java.util.ArrayDeque;

@Autonomous(name="<JERRY> POWERPLAY Autonomous")
public class JerryAutonomous extends BunyipsOpMode {

    private JerryConfig config;
    private CameraOp cam = null;
    private JerryDrive drive;
    private JerryArm arm;
    private ArrayDeque<TaskImpl> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry);
        try {
            cam = new CameraOp(this, config.webcam, config.monitorID, CameraOp.CamMode.OPENCV);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Camera Operation.");
        }
        try {
            drive = new JerryDrive(this, config.bl, config.br, config.fl, config.fr);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Drive System.");
        }
        try {
            arm = new JerryArm(this, config.claw1, config.claw2, config.arm1, config.arm2, config.limit);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Arm System.");
        }

        // 1. Check if we have deadwheel capabilities, if we do, use the respective tasks with
        // deadwheel field positioning, otherwise we will need to use time as that is our only option
        if (config.x != null && config.y != null) {
            // Deadwheels are available
//            tasks.add(new JerryDeadwheelDriveTask(this, 5, drive, config.x, config.y, 100, 100, 1, 1));
            tasks.add(new MessageTask(this, 2, "Deadwheels available"));
        } else {
            // Deadwheels are NOT available
//            tasks.add(new JerryBaseDriveTask(this, 2, drive, 1, 0, 0));
            tasks.add(new MessageTask(this, 2, "No deadwheels available"));
        }

        // 2. Detect signal position (with OpenCV) and save the result to the task instance
        // Run this as soon as we hit init and armed the tasks, as we are permitted to do so.
        GetQRSleeveTask Krankenhaus = new GetQRSleeveTask(this, 7, cam, arm);

        // Init-loop that will stop either once the task has finished or the driver ends Init section
        while (!Krankenhaus.isFinished() && opModeInInit())
            Krankenhaus.run();

        // 3. Park based on position saved in the task instance, or resort to center if the task was
        // prematurely ended (assuming the value may be null if play was pressed before isFinished activated)
        GetQRSleeveTask.ParkingPosition position = Krankenhaus.getPosition();

        if (position == null)
            position = GetQRSleeveTask.ParkingPosition.CENTER;

        switch (position) {
            case LEFT:
                tasks.add(new MessageTask(this, 1, "Loaded LEFT"));
                break;
            default:
            case CENTER:
                tasks.add(new MessageTask(this, 1, "Loaded CENTER"));
                break;
            case RIGHT:
                tasks.add(new MessageTask(this, 1, "Loaded RIGHT"));
                break;
        }

        telemetry.addLine("Ready to go. Parking position has been set to: " + String.valueOf(position));
        telemetry.update();
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        TaskImpl currentTask = tasks.peekFirst();
        if (currentTask == null) {
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()) {
            tasks.removeFirst();
        }
        if (tasks.isEmpty()) {
            drive.deinit();
        }
    }
}
