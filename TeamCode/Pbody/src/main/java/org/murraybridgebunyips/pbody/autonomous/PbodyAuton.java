package org.murraybridgebunyips.pbody.autonomous;


import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.OpModeSelection;
import org.murraybridgebunyips.bunyipslib.RoadRunnerAutonomousBunyipsOpMode;
import org.murraybridgebunyips.bunyipslib.StartingPositions;
import org.murraybridgebunyips.bunyipslib.drive.MecanumDrive;
import org.murraybridgebunyips.bunyipslib.subsystems.HoldableActuator;
import org.murraybridgebunyips.pbody.components.PbodyConfig;

/**
 * Primary Autonomous OpMode (WIP)
 */
@Autonomous(name = "Autonomous")
public class PbodyAuton extends RoadRunnerAutonomousBunyipsOpMode<MecanumDrive> {
    private final PbodyConfig config = new PbodyConfig();
    private HoldableActuator arm;

    @Override
    protected void onInitialise() {
        config.init();
        arm = new HoldableActuator(config.arm);
        setOpModes(StartingPositions.use());
    }

    @Override
    protected void onQueueReady(@Nullable OpModeSelection selectedOpMode) {
        if (selectedOpMode == null) return;
        StartingPositions position = (StartingPositions) selectedOpMode.getObj();
        // TODO: After RR tuning make paths
        switch (position) {
            case STARTING_RED_LEFT:
                break;
            case STARTING_RED_RIGHT:
                addNewTrajectory()

                        .build();
                break;
            case STARTING_BLUE_LEFT:
                break;
            case STARTING_BLUE_RIGHT:
                break;
        }
    }

    @Override
    protected MecanumDrive setDrive() {
        return new MecanumDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor, config.imu, config.fl, config.fr, config.bl, config.br);
    }
}
