package org.firstinspires.ftc.teamcode.example.examplerobot.autonomous;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.example.examplerobot.components.ExampleConfig;
import org.firstinspires.ftc.teamcode.example.examplerobot.components.ExampleDrive;
import org.firstinspires.ftc.teamcode.example.examplerobot.tasks.ExampleTimeDriveTask;

import java.util.List;

public class ExampleAuto extends AutonomousBunyipsOpMode {
    private ExampleDrive drive;
    private final ExampleConfig config = new ExampleConfig();

    @Override
    protected void onInitialisation() {
        config.init(this, hardwareMap);
        drive = new ExampleDrive(this, config.leftMotor, config.rightMotor);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return null;
    }

    @Override
    protected AutoTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        addTask(new ExampleTimeDriveTask(this, 5.0, drive));
    }
}
