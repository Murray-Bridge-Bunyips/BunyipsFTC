package org.firstinspires.ftc.teamcode.jerry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.GetAprilTagTask;
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask;
import org.firstinspires.ftc.teamcode.common.tasks.Task;
import org.firstinspires.ftc.teamcode.jerry.config.JerryArm;
import org.firstinspires.ftc.teamcode.jerry.config.JerryConfig;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;
import org.firstinspires.ftc.teamcode.common.tasks.GetQRSleeveTask;
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryBaseDriveTask;
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryDeadwheelDriveTask;

import java.util.ArrayDeque;

@Autonomous(name="<JERRY> POWERPLAY Autonomous")
public class JerryAutonomous extends BunyipsOpMode {

    private JerryConfig config;
    private CameraOp cam = null;
    private JerryDrive drive;
    private JerryArm arm;
    private GetAprilTagTask Krankenhaus;
    private final ArrayDeque<Task> tasks = new ArrayDeque<>();

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

        // Check if we have deadwheel capabilities, if we do, use the respective tasks with
        // deadwheel field positioning, otherwise we will need to use time as that is our only option
        if (config.x != null && config.y != null) {
            telemetry.addLine("Deadwheels are available. Using Precision/Deadwheel tasks.");
        } else {
            telemetry.addLine("No deadwheels available. Using BaseDrive/IMU tasks only.");
        }

        // Initialisation of guaranteed task loading completed. We can now dedicate our
        // CPU cycles to the init-loop and find the Signal position.
        Krankenhaus = new GetAprilTagTask(this, cam);
    }

    @Override
    protected boolean onInitLoop() {
        // Using CameraOp OPENCV and AprilTags in order to detect the Signal sleeve
        Krankenhaus.run();
        return Krankenhaus.isFinished();
    }

    @Override
    protected void onInitDone() {
        // Determine our final task based on the parking position from the camera
        switch (Krankenhaus.getPosition()) {
            case LEFT:

                break;
            default:
            case CENTER:

                break;
            case RIGHT:

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
