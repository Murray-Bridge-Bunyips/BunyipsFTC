package org.firstinspires.ftc.teamcode.glados.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.RoadRunnerTask;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;

import java.util.List;

/**
 * Test to learn RoadRunner
 *
 * @author Lachlan Paul, 2023
 */
public class GLaDOSRoadRunnerTest extends AutonomousBunyipsOpMode {
    private GLaDOSConfigCore config;
    private MecanumDrive drive;
    private Trajectory trajectory;
    double x1 = 10.0;
    double y1 = 10.0;
    @Override
    protected void onInitialisation() {
        config.init(this);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.bl, config.fr, config.br);
        trajectory = drive.trajectoryBuilder(new Pose2d(0, 0, 0))
                .splineTo(new Vector2d(x1, y1), Math.toRadians(90))
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
        addTask(new RoadRunnerTask(this, 5, drive, trajectory));
    }
}
