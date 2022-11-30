package org.firstinspires.ftc.teamcode.jerry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.ButtonControl;
import org.firstinspires.ftc.teamcode.common.ButtonHashmap;
import org.firstinspires.ftc.teamcode.common.CameraOp;
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl;
import org.firstinspires.ftc.teamcode.jerry.config.JerryArm;
import org.firstinspires.ftc.teamcode.jerry.config.JerryConfig;
import org.firstinspires.ftc.teamcode.jerry.config.JerryDrive;
import org.firstinspires.ftc.teamcode.jerry.tasks.JerryTimeDriveTask;

import java.util.ArrayDeque;

@Autonomous(name = "<JERRY> POWERPLAY BASIC PARK Autonomous")
public class JerryBasicGuaranteeAutonomous extends BunyipsOpMode {

    private JerryConfig config;
    private CameraOp cam;
    private JerryDrive drive;
    private JerryArm arm;
    private final ArrayDeque<TaskImpl> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = JerryConfig.newConfig(hardwareMap, telemetry);
        try {
            drive = new JerryDrive(this, config.bl, config.br, config.fl, config.fr);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise Drive System.");
        }

        ButtonControl selectedButton = ButtonHashmap.map(this, "Red Drive Left", "Blue Drive Right", "", "");
        switch (selectedButton) {
            case A:
                // Move left
                tasks.add(new JerryTimeDriveTask(this, 1.5, drive, 1, 0, 0));
                break;
            case B:
                // Move right
                tasks.add(new JerryTimeDriveTask(this, 1.5, drive, -1, 0, 0));
                break;
        }

        telemetry.addLine("Ready to go under config: " + selectedButton);
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
