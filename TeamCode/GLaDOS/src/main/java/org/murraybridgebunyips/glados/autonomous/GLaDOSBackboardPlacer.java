package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Inches;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.Seconds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.DualDeadwheelMecanumDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.DualServos;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.bunyipslib.tasks.WaitTask;
import org.murraybridgebunyips.glados.components.GLaDOSConfigCore;

/**
 * Backboard Placer Autonomous
 * @author Lucas Bubner, 2024
 */
@Config
@Autonomous(name = "Backboard Placer (Left Park)")
public class GLaDOSBackboardPlacer extends AutonomousBunyipsOpMode implements RoadRunner {
    /**
     * Multiplicative scale for all RoadRunner distances.
     */
    public static double FIELD_TILE_SCALE = 1.5;
    private final GLaDOSConfigCore config = new GLaDOSConfigCore();
    private DualDeadwheelMecanumDrive drive;
    private HoldableActuator arm;
    private DualServos claws;

    @Override
    protected void onInitialise() {
        config.init();
        drive = new DualDeadwheelMecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.frontLeft, config.frontRight, config.backLeft, config.backRight, config.localizerCoefficients, config.parallelDeadwheel, config.perpendicularDeadwheel);
        arm = new HoldableActuator(config.arm).withMovingPower(0.5);
        claws = new DualServos(config.leftPixel, config.rightPixel, 1.0, 0.0, 0.0, 1.0);

        setOpModes(StartingPositions.use());
        addSubsystems(drive, arm, claws);
    }

    @NonNull
    @Override
    public RoadRunnerDrive getDrive() {
        return drive;
    }

    @Override
    protected void onReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null)
            return;

        switch ((StartingPositions) selectedOpMode.getObj()) {
            case STARTING_RED_LEFT:
                addNewTrajectory()
                        .forward(Inches.convertFrom(1.8 * FIELD_TILE_SCALE, FieldTiles))
                        .build();
                addNewTrajectory()
                        .strafeRight(Inches.convertFrom(2.8 * FIELD_TILE_SCALE, FieldTiles))
                        .turn(-Math.PI / 2)
                        .build();
                addNewTrajectory()
                        .strafeRight(Inches.convertFrom(1 * FIELD_TILE_SCALE, FieldTile))
                        .build();
                break;
            case STARTING_RED_RIGHT:
                addNewTrajectory()
                        .lineToLinearHeading(new Pose2d(Inches.convertFrom(1 * FIELD_TILE_SCALE, FieldTiles), Inches.convertFrom(-1 * FIELD_TILE_SCALE, FieldTiles), Math.toRadians(-90.0)))
                        .build();
                break;
        }

        addTask(arm.deltaTask(1500));
        addTask(claws.openTask(DualServos.ServoSide.BOTH));
        addTask(new WaitTask(Seconds.of(1)));
        addTask(arm.deltaTask(-1500));

        addNewTrajectory()
                .strafeLeft(Inches.convertFrom(0.95 * FIELD_TILE_SCALE, FieldTile))
                .build();
        addNewTrajectory()
                .forward(Inches.convertFrom(1.1 * FIELD_TILE_SCALE, FieldTiles))
                .build();
    }
}
