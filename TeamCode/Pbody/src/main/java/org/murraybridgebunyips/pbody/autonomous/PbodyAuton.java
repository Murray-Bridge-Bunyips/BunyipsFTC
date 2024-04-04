package org.murraybridgebunyips.pbody.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.Inches;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask;
import org.murraybridgebunyips.pbody.components.PbodyConfig;

import java.util.List;

/**
 * Primary Autonomous OpMode (WIP)
 */
@Autonomous(name = "Autonomous")
public class PbodyAuton extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final PbodyConfig config = new PbodyConfig();

    @Nullable
    @Override
    protected List<OpModeSelection> setOpModes() {
        return StartingPositions.use();
    }

    @Nullable
    @Override
    protected RobotTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingPositions position = (StartingPositions) selectedOpMode.getObj();
        // TODO: After RR tuning make paths
        switch (position) {
            case STARTING_RED_LEFT:
                addNewTrajectory()
                        .forward(Inches.fromM(1))
                        .build();
                break;
            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .lineTo(new Vector2d(18, 18))
                        .build();
                break;
            case STARTING_BLUE_LEFT:
                addNewTrajectory(new Pose2d(position.getVector(), Math.toRadians(-90)))
                        .forward(1)
                        .build();
                break;
            case STARTING_BLUE_RIGHT:
                addNewTrajectory(new Pose2d(position.getVector(), Math.toRadians(-90)))
                        .build();
                break;
        }
    }

    @Override
    protected void onInitialise() {
        config.init();
    }

    @Override
    protected MecanumDrive setDrive() {
        return new MecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
    }
}
