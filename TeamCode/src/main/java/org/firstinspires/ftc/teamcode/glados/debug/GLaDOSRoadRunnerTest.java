package org.firstinspires.ftc.teamcode.glados.debug;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.common.AutonomousBunyipsOpMode;
import org.firstinspires.ftc.teamcode.common.Inches;
import org.firstinspires.ftc.teamcode.common.MecanumDrive;
import org.firstinspires.ftc.teamcode.common.OpModeSelection;
import org.firstinspires.ftc.teamcode.common.RobotConfig;
import org.firstinspires.ftc.teamcode.common.tasks.AutoTask;
import org.firstinspires.ftc.teamcode.common.tasks.RoadRunnerTask;
import org.firstinspires.ftc.teamcode.glados.components.GLaDOSConfigCore;
import org.firstinspires.ftc.teamcode.wheatley.components.WheatleyConfig;

import java.util.List;

@Autonomous(name = "GLaDOS: RoadRunner Test", group = "GLADOS")
public class GLaDOSRoadRunnerTest extends AutonomousBunyipsOpMode {
    private GLaDOSConfigCore config = new GLaDOSConfigCore();
    private MecanumDrive drive;
    private final Trajectory testTrajectory = drive.trajectoryBuilder(drive.getPoseEstimate())
            .forward(Inches.fromM(1))
            .build();

    @Override
    protected void onInitialisation() {
        config = (GLaDOSConfigCore) RobotConfig.newConfig(this, config, hardwareMap);
        drive = new MecanumDrive(this, config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
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
