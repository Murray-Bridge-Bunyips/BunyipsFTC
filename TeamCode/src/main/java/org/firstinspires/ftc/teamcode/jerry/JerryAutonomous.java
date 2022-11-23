package org.firstinspires.ftc.teamcode.jerry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.jerry.config.JerryArm;
import org.firstinspires.ftc.teamcode.jerry.config.JerryConfig;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;
import org.firstinspires.ftc.teamcode.common.tasks.GetQRSleeveTask;
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryBaseDriveTask;
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryDeadwheelDriveTask;

import java.util.ArrayDeque;

@Autonomous(name="<JERRY> Autonomous Testing")
public class JerryAutonomous extends BunyipsOpMode {

    private JerryConfig config;
    private CameraOp cam = null;
    private JerryDrive drive;
    private JerryArm arm;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

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


        // 1. Detect signal position (with OpenCV) and save it to a variable in OpMode GlobalStorage
        // Run this as soon as we hit init and armed the tasks, as we are permitted to do so.
        Task Krankenhaus = new GetQRSleeveTask(this, 7, cam);
        while (!Krankenhaus.isFinished())
            Krankenhaus.run();

        // 2. Check if we have deadwheel capabilities, if we do, use the respective tasks with
        // deadwheel field positioning, otherwise we will need to use time as that is our only option
        if (config.x != null && config.y != null) {
            // Deadwheels are available
            tasks.add(new JerryDeadwheelDriveTask(this, 5, drive, config.x, config.y, 100, 100, 1, 1));
        } else {
            // Deadwheels are NOT available
            tasks.add(new JerryBaseDriveTask(this, 2, drive, 1, 0, 0));
        }

        // 3. Park based on position saved in GlobalStorage
        switch (globalStorage.getItem("PARKING_POSITION")) {
            case "LEFT":

                break;
            case "CENTER":

                break;
            case "RIGHT":
                
                break;
        }
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        Task currentTask = tasks.peekFirst();
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
