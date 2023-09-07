package org.firstinspires.ftc.teamcode.bertie;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.bertie.components.BertieArm;
import org.firstinspires.ftc.teamcode.bertie.components.BertieConfig;
import org.firstinspires.ftc.teamcode.bertie.components.BertieDrive;
import org.firstinspires.ftc.teamcode.bertie.tasks.BertieTimeDriveTask;
import org.firstinspires.ftc.teamcode.common.BunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.ButtonControl;
import org.firstinspires.ftc.teamcode.common.AutonomousSelector;
import org.firstinspires.ftc.teamcode.common.tasks.MessageTask;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;

import java.util.ArrayDeque;

/*
    Robot BERTIE no longer exists; new robot is now JERRY
    This code now remains for archival purposes only.
 */
@Autonomous(name = "<BERTIE> Autonomous Testing")
@Disabled
public class BertieAutonomous extends BunyipsOpMode {
    private BertieConfig config;
    private BertieDrive drive = null;
    private BertieArm lift = null;
    private ArrayDeque<TaskImpl> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = BertieConfig.newConfig(hardwareMap, telemetry);

        try {
            drive = new BertieDrive(this,
                    config.frontLeft, config.frontRight,
                    config.backLeft, config.backRight,
                    false);
        } catch (Exception e) {
            telemetry.addLine("Failed to initialise motors.");
        }

        ButtonControl selectedButton = ButtonHashmap.map(this, "Red", "Blue", "", "");
        switch (selectedButton) {
            case A:
                tasks.add(new MessageTask(this, 1, "Loaded red"));
                tasks.add(new BertieTimeDriveTask(this, 0.5, drive, 1, 0, 0));
                tasks.add(new BertieTimeDriveTask(this, 0.5, drive, 0, 1, 0));
                tasks.add(new BertieTimeDriveTask(this, 1, drive, -1, 0, 0));
                tasks.add(new BertieTimeDriveTask(this, 1, drive, 0, -1, 0));
                break;
            case B:
                tasks.add(new MessageTask(this, 1, "Loaded blue"));
                break;
        }
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
            drive.setSpeedXYR(0, 0, 0);
            drive.update();
        }
    }
}