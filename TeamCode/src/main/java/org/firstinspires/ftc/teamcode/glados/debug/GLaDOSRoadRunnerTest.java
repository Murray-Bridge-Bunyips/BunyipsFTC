package org.firstinspires.ftc.teamcode.glados.debug;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.GlobalStorage;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.RoadRunnerTask;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

import java.util.List;

@Autonomous(name = "GLaDOS: RoadRunner Test", group = "GLADOS")
public class GLaDOSRoadRunnerTest extends AutonomousBunyipsOpMode {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private Trajectory testTrajectory;

    @Override
    protected void onInitialisation() {
        config.init(this, hardwareMap);
        GlobalStorage.remove("lastPose");
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
        testTrajectory = drive.trajectoryBuilder(drive.getPoseEstimate())
                .splineTo(new Vector2d(10, 0), 0)
                .splineTo(new Vector2d(0, 10), 0)
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
//        addTask(new RoadRunnerTurnTask(this, 20, drive, 90));
    }

    @Override
    protected void onStop() {
        drive.teardown();
    }
}
