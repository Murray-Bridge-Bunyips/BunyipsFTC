package org.murraybridgebunyips.pbody.autonomous;


import static org.murraybridgebunyips.bunyipslib.external.units.Units.Degrees;
import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.AutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.Controls;
import org.murraybridgebunyips.bunyipslib.Reference;
import org.murraybridgebunyips.bunyipslib.RoadRunner;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.pbody.components.PbodyConfig;

/**
 * Primary Autonomous OpMode (WIP)
 */
@Autonomous(name = "Autonomous")
public class PbodyAuton extends AutonomousBunyipsOpMode implements RoadRunner {
    private final PbodyConfig config = new PbodyConfig();
    private MecanumDrive drive;
    private HoldableActuator arm;

    @Override
    protected void onInitialise() {
        config.init();
        arm = new HoldableActuator(config.arm);
        drive = new MecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
        setOpModes(StartingPositions.use());
    }

    @Override
    protected void onReady(@Nullable Reference<?> selectedOpMode, Controls selectedButton) {
        if (selectedOpMode == null) return;
        StartingPositions position = (StartingPositions) selectedOpMode.require();
        // TODO: Make paths (painfully)
        switch (position) {
            case STARTING_RED_LEFT:
                break;
            case STARTING_RED_RIGHT:
                makeTrajectory()
                        .splineTo(new Vector2d(1.0, -1.0), FieldTiles, -90.0, Degrees)
                        .addTask();
                break;
            case STARTING_BLUE_LEFT:
                break;
            case STARTING_BLUE_RIGHT:
                break;
        }
    }

    @NonNull
    @Override
    public MecanumDrive getDrive() {
        return drive;
    }
}
