package org.murraybridgebunyips.vance.teleop;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.murraybridgebunyips.bunyipslib.roadrunner.drive.MecanumRoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.RoadRunnerDrive;
import org.murraybridgebunyips.bunyipslib.roadrunner.drive.tuning.RoadRunnerTuningOpMode;
import org.murraybridgebunyips.vance.VanceConfig;

/**
 * brrrrrrrrrrrrrm, brrrrrrrrrrrrrrm, brrrrrrrrrrrrrrrrrrrm
 *
 * @author Lachlan Paul, 2024
 */
@TeleOp
public class VanceRoadRunnerTuning extends RoadRunnerTuningOpMode {
    private final VanceConfig config = new VanceConfig();
    private MecanumRoadRunnerDrive drive;

    @NonNull
    @Override
    protected RoadRunnerDrive getBaseRoadRunnerDrive() {
        config.init(this);
        drive = new MecanumRoadRunnerDrive(config.driveConstants, config.mecanumCoefficients, hardwareMap.voltageSensor,
                config.imu, config.fl, config.fr, config.bl, config.br);
        return drive;
    }
}
