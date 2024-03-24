package org.murraybridgebunyips.glados.autonomous;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.RobotConfig;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.tasks.bases.RobotTask;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

import java.util.List;

/**
 * Test to learn RoadRunner
 *
 * @author Lachlan Paul, 2023
 */
@Autonomous(name = "RoadRunner Test")
@Disabled
public class GLaDOSRoadRunnerTest extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();

    @Override
    protected void onInitialise() {
        config.init();
        RobotConfig.setLastKnownPosition(null);
    }

    @Override
    protected MecanumDrive setDrive() {
        return new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
    }

    @Override
    protected List<OpModeSelection> setOpModes() {
        return null;
    }

    @Override
    protected RobotTask setInitTask() {
        return null;
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        // Start red-left
        addNewTrajectory(new Pose2d(-37.95, -71.22, Math.toRadians(90.00)))
                .splineTo(new Vector2d(-35.77, -13.74), Math.toRadians(87.16))
                .splineTo(new Vector2d(3.44, 3.12), Math.toRadians(27.30))
                .splineTo(new Vector2d(45.61, 58.10), Math.toRadians(52.51))
                .splineTo(new Vector2d(41.39, -3.28), Math.toRadians(264.54))
                .splineTo(new Vector2d(37.02, -40.76), Math.toRadians(181.15))
                .splineTo(new Vector2d(-49.20, -36.23), Math.toRadians(180.18))
                .splineTo(new Vector2d(-37.95, -61.07), Math.toRadians(-87.75))
                .build();

    }
}
