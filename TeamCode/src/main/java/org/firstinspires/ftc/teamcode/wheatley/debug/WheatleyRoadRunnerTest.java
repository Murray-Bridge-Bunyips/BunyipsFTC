package org.firstinspires.ftc.teamcode.wheatley.debug;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.RoadRunnerTask;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

import java.util.List;

@Autonomous(name = "WHEATLEY: RoadRunner Test", group = "WHEATLEY")
public class WheatleyRoadRunnerTest extends AutonomousBunyipsOpMode {
    private final WheatleyConfig config = new WheatleyConfig();
    private MecanumDrive drive;
    private Trajectory testTrajectory;

    @Override
    protected void onInitialisation() {
        config.init(this, hardwareMap);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
        testTrajectory = drive.trajectoryBuilder(drive.getPoseEstimate())
                .forward(Inches.fromM(1))
                .build();
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
        addTask(new RoadRunnerTask(this, 5, drive, testTrajectory));
    }

    @Override
    protected void onStop() {
        drive.teardown();
    }
}
