package org.murraybridgebunyips.glados.autonomous;

import static org.murraybridgebunyips.bunyipslib.external.units.Units.FieldTile;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.murraybridgebunyips.bunyipslib.tasks.RoadRunnerTask;

/**
 * Backdrop Placer Autonomous for Right Parking
 *
 * @author Lucas Bubner, 2024
 */
@Autonomous(name = "Backdrop Placer (Right Park)")
public class GLaDOSBackdropPlacerRightPark extends GLaDOSBackdropPlacerLeftPark {
    @Override
    protected RoadRunnerTask afterPixelDropDriveAction(RoadRunnerTrajectoryTaskBuilder builder) {
        return builder
                .strafeRight(0.95 * FIELD_TILE_SCALE, FieldTile)
                .buildTask();
    }
}
